package com.easterfg.mae2a.common.items;

import appeng.api.config.FuzzyMode;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.items.AEBaseItem;
import com.easterfg.mae2a.common.cells.InfiniteDyesCellHandler;
import com.easterfg.mae2a.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author EasterFG on 2024/10/12
 */
public class InfiniteDyesCellItem extends AEBaseItem implements ICellWorkbenchItem {

    public InfiniteDyesCellItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack itemStack) {
        return FuzzyMode.IGNORE_ALL;
    }

    @Override
    public void setFuzzyMode(ItemStack itemStack, FuzzyMode fuzzyMode) {
    }

    @Override
    public boolean isEditable(ItemStack is) {
        return false;
    }

    @Override
    public void addToMainCreativeTab(CreativeModeTab.Output output) {
        super.addToMainCreativeTab(output);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> lines, @NotNull TooltipFlag advancedTooltips) {
        lines.add(Component.translatable("item.mae2a.infinite_dyes_cell.tooltip").withStyle(style ->
                style.withColor(TooltipHelper.GRADIENT.getCurrent())));
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack is) {
        return Optional.of(InfiniteDyesCellHandler.INSTANCE.DEFAULT_IMAGE_TOOLTIP);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack is) {
        return Component.translatable("item.mae2a.infinite_dyes_cell")
                .withStyle(ChatFormatting.AQUA);
    }
}
