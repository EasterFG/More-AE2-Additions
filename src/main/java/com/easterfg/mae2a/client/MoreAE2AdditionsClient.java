package com.easterfg.mae2a.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import appeng.init.client.InitScreens;

import com.easterfg.mae2a.client.forge.ForgeClientEvent;
import com.easterfg.mae2a.client.render.PackageClientTooltipComponent;
import com.easterfg.mae2a.client.screen.CablePlaceToolScreen;
import com.easterfg.mae2a.client.screen.PatternModifyScreen;
import com.easterfg.mae2a.client.screen.PatternPreviewListScreen;
import com.easterfg.mae2a.client.screen.PatternProviderPlusScreen;
import com.easterfg.mae2a.common.definition.ModItems;
import com.easterfg.mae2a.common.items.PackageItemTooltipComponent;
import com.easterfg.mae2a.common.menu.CablePlaceToolMenu;
import com.easterfg.mae2a.common.menu.PatternModifyMenu;
import com.easterfg.mae2a.common.menu.PatternPreviewListMenu;
import com.easterfg.mae2a.common.menu.PatternProviderPlusMenu;
import com.easterfg.mae2a.network.NetworkHandler;
import com.easterfg.mae2a.network.packet.OpenMenuPacket;
import com.easterfg.mae2a.network.packet.PlaceCablePacket;

/**
 * @author EasterFG on 2025/4/1
 */
public class MoreAE2AdditionsClient {

    public static final MoreAE2AdditionsClient INSTANCE = new MoreAE2AdditionsClient();
    private static boolean isPressed;

    public MoreAE2AdditionsClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::registerClientTooltipComponents);
        modEventBus.addListener(this::onKeyRegister);
        MinecraftForge.EVENT_BUS.addListener(this::handleEventInput);
        MinecraftForge.EVENT_BUS.register(ForgeClientEvent.class);
    }

    public void init() {
        this.registerGui();
    }

    private void registerGui() {
        InitScreens.register(
                PatternModifyMenu.TYPE,
                PatternModifyScreen::new,
                "/screens/pattern_modify.json");

        InitScreens.register(
                PatternProviderPlusMenu.TYPE,
                PatternProviderPlusScreen::new,
                "/screens/pattern_provider_plus.json");

        InitScreens.register(
                PatternPreviewListMenu.TYPE,
                PatternPreviewListScreen::new,
                "/screens/pattern_preview_list.json");

        InitScreens.register(
                CablePlaceToolMenu.TYPE,
                CablePlaceToolScreen::new,
                "/screens/cable_place_tool.json");
    }

    public void registerClientTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(PackageItemTooltipComponent.class, PackageClientTooltipComponent::new);
    }

    public void onKeyRegister(RegisterKeyMappingsEvent event) {
        for (KeyMapping mapping : KeyBindings.getKeyBindings()) {
            event.register(mapping);
        }
    }

    public void handleEventInput(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null || mc.player == null || mc.level == null)
            return;

        ItemStack tool = mc.player.getMainHandItem();
        if (tool.isEmpty() && !tool.is(ModItems.CABLE_PLACE_TOOL.asItem()))
            return;

        if (KeyBindings.OPEN_CABLE_UI.isDown()) {
            if (!isPressed) {
                isPressed = true;
                NetworkHandler.INSTANCE
                        .sendToServer(new OpenMenuPacket("ae2:cable_place_tool", mc.player.getUsedItemHand()));
            }
        } else if (KeyBindings.PLACE_CABLE.isDown()) {
            if (!isPressed) {
                isPressed = true;
                NetworkHandler.INSTANCE.sendToServer(new PlaceCablePacket(mc.player.getUsedItemHand()));
            }
        } else {
            isPressed = false;
        }
    }
}
