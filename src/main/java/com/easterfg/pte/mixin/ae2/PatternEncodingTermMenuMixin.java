package com.easterfg.pte.mixin.ae2;

import appeng.api.stacks.GenericStack;
import appeng.api.storage.ITerminalHost;
import appeng.helpers.IMenuCraftingPacket;
import appeng.helpers.IPatternTerminalMenuHost;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.util.ConfigInventory;
import com.easterfg.pte.api.PatterEncodingTermMenuModify;
import com.easterfg.pte.gui.ModifyData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author EasterFG on 2024/9/12
 */
@Mixin(PatternEncodingTermMenu.class)
public abstract class PatternEncodingTermMenuMixin extends MEStorageMenu implements IMenuCraftingPacket, PatterEncodingTermMenuModify {

    @Shadow(remap = false)
    @Final
    private ConfigInventory encodedInputsInv;

    @Shadow(remap = false)
    @Final
    private ConfigInventory encodedOutputsInv;

    public PatternEncodingTermMenuMixin(MenuType<?> menuType, int id, Inventory ip, ITerminalHost host) {
        super(menuType, id, ip, host);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/inventory/MenuType;ILnet/minecraft/world/entity/player/Inventory;Lappeng/helpers/IPatternTerminalMenuHost;Z)V",
            at = @At("TAIL"), remap = false)
    public void initHooks(MenuType<?> menuType, int id, Inventory ip, IPatternTerminalMenuHost host, boolean bindInventory, CallbackInfo ci) {
        this.registerClientAction("modifyPatter", ModifyData.class,
                this::patternTerminalExtended$modifyPatter);
    }

    @Override
    public void patternTerminalExtended$modifyPatter(ModifyData data) {
        if (this.isClientSide()) {
            this.sendClientAction("modifyPatter", data);
        } else {
            // modify
            var output = patternTerminalExtended$valid(this.encodedOutputsInv, data);
            if (output == null) {
                return;
            }
            var input = patternTerminalExtended$valid(this.encodedInputsInv, data);
            if (input == null) {
                return;
            }
            for (int slot = 0; slot < output.length; ++slot) {
                if (output[slot] != null) {
                    this.encodedOutputsInv.setStack(slot, output[slot]);
                }
            }
            for (int slot = 0; slot < input.length; ++slot) {
                if (input[slot] != null) {
                    this.encodedInputsInv.setStack(slot, input[slot]);
                }
            }
        }
    }

    @Unique
    private GenericStack[] patternTerminalExtended$valid(ConfigInventory inv, ModifyData data) {
        GenericStack[] result = new GenericStack[inv.size()];
        for (int slot = 0; slot < inv.size(); ++slot) {
            GenericStack stack = inv.getStack(slot);
            if (stack != null) {
                switch (data.getType()) {
                    case MULTIPLY -> {
                        if (data.getAmount() * stack.amount() <= 0) {
                            return null;
                        } else {
                            result[slot] =
                                    new GenericStack(stack.what(), stack.amount() * data.getAmount());
                        }
                    }
                    case DIVISION -> {
                        if (stack.amount() % data.getAmount() != 0) {
                            return null;
                        } else {
                            // 除尽
                            result[slot] =
                                    new GenericStack(stack.what(), stack.amount() / data.getAmount());
                        }
                    }
                }
            }
        }
        return result;
    }
}
