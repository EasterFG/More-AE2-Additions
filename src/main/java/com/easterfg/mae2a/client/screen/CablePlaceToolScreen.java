package com.easterfg.mae2a.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import appeng.api.util.AEColor;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;

import com.easterfg.mae2a.MoreAE2Additions;
import com.easterfg.mae2a.client.gui.widget.CablePickerWidget;
import com.easterfg.mae2a.client.gui.widget.ColorPickerWidget;
import com.easterfg.mae2a.client.gui.widget.CustomIconButton;
import com.easterfg.mae2a.common.menu.CablePlaceToolMenu;
import com.easterfg.mae2a.network.NetworkHandler;
import com.easterfg.mae2a.network.packet.UpdateSettingPackage;

/**
 * @author EasterFG on 2025/4/10
 */
public class CablePlaceToolScreen extends AEBaseScreen<CablePlaceToolMenu> {

    private final CablePickerWidget cablePicker;
    private final ForgeSlider pickBlock;

    public CablePlaceToolScreen(CablePlaceToolMenu menu, Inventory playerInventory, Component title,
            ScreenStyle style) {
        super(menu, playerInventory, title, style);
        var colorPicker = new ColorPickerWidget(this.getMenu().getHost().getColor());
        colorPicker.setOnColorChanged(this::changeColor);
        widgets.add("colors", colorPicker);
        cablePicker = new CablePickerWidget(this.getMenu().getHost().getCable(), () -> menu.getHost().getColor());
        cablePicker.setOnSelectionChanged(menu::changeCable);
        widgets.add("cables", cablePicker);
        pickBlock = new ForgeSlider(0, 0, 0, 0,
                Component.translatable("gui.mae2a.picker.select_range"),
                Component.translatable("gui.mae2a.picker.unit"),
                1, 64, this.getMenu().getHost().getPicker(), true);
        widgets.add("pick_distance", pickBlock);
        var switchReplace = new CustomIconButton(
                (__) -> NetworkHandler.INSTANCE.sendToServer(new UpdateSettingPackage(!menu.isReplace())),
                MoreAE2Additions.id("textures/guis/replace.png"),
                Component.translatable("gui.mae2a.mode.on"),
                Component.translatable("gui.mae2a.mode.off"));
        switchReplace.setStatusSupplier(menu::isReplace);
        switchReplace.setMessage(Component.translatable("gui.mae2a.picker.replace"));
        addToLeftToolbar(switchReplace);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
        if (pickBlock.isMouseOver(mouseX, mouseY)) {
            if (pickBlock.mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY)) {
                return true;
            }
        }

        return super.mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // save the pick distance
        this.menu.changePicker(pickBlock.getValueInt());
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void drawFG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY) {
        guiGraphics.vLine(cablePicker.getX() - 3 - offsetX, cablePicker.getY() - offsetY, cablePicker.getHeight() + 8,
                0xFFFFFFFF);
    }

    public void changeColor(AEColor color) {
        menu.changeColor(color);
        cablePicker.setColorSupplier(() -> color);
    }
}
