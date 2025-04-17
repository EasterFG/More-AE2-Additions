package com.easterfg.mae2a.common.menu;

import java.util.Collections;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import appeng.core.definitions.AEItems;
import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantics;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.util.inv.AppEngInternalInventory;

import com.easterfg.mae2a.client.gui.slot.OnlyShowSlot;
import com.easterfg.mae2a.common.menu.host.PatternModifyHost;

/**
 * @author EasterFG on 2025/4/6
 */
public class PatternListMenu extends AEBaseMenu {
    public static final MenuType<PatternListMenu> TYPE = MenuTypeBuilder
            .create(PatternListMenu::new, PatternModifyHost.class)
            .build("pattern_list");

    private final static String ACTION_CONFIRM = "confirm";
    private final static String ACTION_SWITCH = "switch";

    protected final PatternModifyHost host;

    public PatternListMenu(int id, Inventory playerInventory, PatternModifyHost host) {
        super(TYPE, id, playerInventory, host);
        this.host = host;

        var patterns = host.getPatterns();
        if (patterns != null) {
            var tempInv = new AppEngInternalInventory(patterns.size());
            for (int i = 0; i < patterns.size(); i++) {
                var stack = patterns.get(i);
                tempInv.setItemDirect(i, stack);
                this.addSlot(new OnlyShowSlot(tempInv, i), SlotSemantics.ENCODED_PATTERN);
            }
        }
        registerClientAction(ACTION_CONFIRM, this::confirm);
        registerClientAction(ACTION_SWITCH, Integer.class, this::switchEnable);
    }

    public List<ItemStack> getPatterns() {
        var patterns = this.host.getPatterns();
        if (patterns == null) {
            return null;
        }
        return Collections.unmodifiableList(patterns);
    }

    public void confirm() {
        if (isClientSide()) {
            sendClientAction(ACTION_CONFIRM);
            return;
        }

        int count = 0;
        var patternLogin = host.getProviderLogic();
        if (patternLogin != null) {
            var patternInv = patternLogin.getPatternInv();
            List<Slot> patterns = this.getSlots(SlotSemantics.ENCODED_PATTERN);
            for (Slot slot : patterns) {
                var stack = slot.getItem();
                if (stack.isEmpty() || stack.is(Items.BARRIER) || stack.is(AEItems.BLANK_PATTERN.asItem()))
                    continue;
                patternInv.setItemDirect(slot.getSlotIndex(), stack);
                count++;
            }
        }
        getPlayer().displayClientMessage(Component.translatable("tools.mae2a.one_patter_result", count), true);
    }

    public void switchEnable(int index) {
        if (isClientSide()) {
            sendClientAction(ACTION_SWITCH, index);
            return;
        }

        var current = slots.get(index);
        var stack = current.getItem();

        if (stack.is(Items.BARRIER)) {
            current.set(this.host.getPatterns().get(index));
        } else {
            current.set(Items.BARRIER.getDefaultInstance());
        }
    }
}
