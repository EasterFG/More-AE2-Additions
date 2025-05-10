package com.easterfg.mae2a.common.menu;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import appeng.core.definitions.AEItems;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantics;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.util.inv.AppEngInternalInventory;

import com.easterfg.mae2a.api.slot.PreviewSlot;
import com.easterfg.mae2a.common.menu.host.PatternModifyHost;
import com.easterfg.mae2a.util.PatternUtils;

/**
 * @author EasterFG on 2025/4/6
 */
public class PatternPreviewListMenu extends AEBaseMenu {
    public static final MenuType<PatternPreviewListMenu> TYPE = MenuTypeBuilder
            .create(PatternPreviewListMenu::new, PatternModifyHost.class)
            .build("pattern_preview_list");

    private final static String ACTION_CONFIRM = "confirm";
    private final static String ACTION_SWITCH = "switch";
    private static final String ACTION_DIVIDE = "divide";
    private static final String ACTION_MULTIPLY = "multiply";
    private static final String ACTION_SELECT = "select";

    protected final PatternModifyHost host;

    private boolean select = true;

    public PatternPreviewListMenu(int id, Inventory playerInventory, PatternModifyHost host) {
        super(TYPE, id, playerInventory, host);
        this.host = host;

        var patterns = host.getPatterns();
        if (patterns != null) {
            var tempInv = new AppEngInternalInventory(patterns.size());
            for (int i = 0; i < patterns.size(); i++) {
                var stack = patterns.get(i);
                PreviewSlot slot = new PreviewSlot(tempInv, i);
                if (!stack.isEmpty()) {
                    slot.set(stack);
                }
                this.addSlot(slot, SlotSemantics.ENCODED_PATTERN);
            }
        }
        registerClientAction(ACTION_CONFIRM, this::confirm);
        registerClientAction(ACTION_SWITCH, Integer.class, this::switchEnable);
        registerClientAction(ACTION_MULTIPLY, Integer.class, this::multiply);
        registerClientAction(ACTION_DIVIDE, Integer.class, this::divide);
        registerClientAction(ACTION_SELECT, this::switchSelect);
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
                if (slot instanceof PreviewSlot previewSlot) {
                    if (!slot.hasItem() || select != previewSlot.isEnable())
                        continue;
                    ItemStack slotItem = previewSlot.getItem();
                    if (slotItem.getItem() != AEItems.PROCESSING_PATTERN.asItem()) {
                        continue;
                    }
                    patternInv.setItemDirect(slot.getSlotIndex(), slotItem);
                    count++;
                }
            }
        }
        if (count > 0) {
            getPlayer().displayClientMessage(Component.translatable("tools.mae2a.one_patter_result", count), true);
        }
    }

    public void switchEnable(int index) {
        if (isClientSide()) {
            sendClientAction(ACTION_SWITCH, index);
        }

        var current = slots.get(index);

        if (current instanceof PreviewSlot previewSlot) {
            previewSlot.setStatus(previewSlot.isEnable() ? PreviewSlot.Status.DISABLE : PreviewSlot.Status.ENABLE);
        }
    }

    public void switchSelect() {
        if (isClientSide()) {
            sendClientAction(ACTION_SELECT);
        }
        select = !select;
    }

    @Nullable
    private ItemStack processingSlot(PreviewSlot slot, int times, boolean multiplyMode) {
        var stack = slot.getItem();
        if (stack.isEmpty() || stack.is(AEItems.BLANK_PATTERN.asItem()))
            return null;
        if (stack.getItem() instanceof ProcessingPatternItem ppi) {
            AEProcessingPattern details = ppi.decode(stack, getPlayer().level(), false);
            if (details == null)
                return null;
            return PatternUtils.apply(details, times, true, multiplyMode, details.getPrimaryOutput());
        }
        return null;
    }

    public void divide(int times) {
        if (isClientSide()) {
            sendClientAction(ACTION_DIVIDE, times);
            return;
        }
        List<Slot> patterns = this.getSlots(SlotSemantics.ENCODED_PATTERN);
        for (Slot slot : patterns) {
            if (slot instanceof PreviewSlot previewSlot) {
                if (select != previewSlot.isEnable())
                    continue;
                var result = processingSlot(previewSlot, times, false);
                if (result != null) {
                    slot.set(result);
                }
            }
        }
    }

    public void multiply(int times) {
        if (isClientSide()) {
            sendClientAction(ACTION_MULTIPLY, times);
            return;
        }
        List<Slot> patterns = this.getSlots(SlotSemantics.ENCODED_PATTERN);
        for (Slot slot : patterns) {
            if (slot instanceof PreviewSlot previewSlot) {
                if (select != previewSlot.isEnable())
                    continue;
                var result = processingSlot(previewSlot, times, true);
                if (result != null) {
                    slot.set(result);
                }
            }
        }
    }
}
