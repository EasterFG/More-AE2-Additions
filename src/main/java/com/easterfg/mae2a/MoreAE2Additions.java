package com.easterfg.mae2a;

import com.easterfg.mae2a.client.register.CellRegister;
import com.easterfg.mae2a.client.register.GuiRegister;
import com.easterfg.mae2a.client.register.NetworkRegister;
import com.easterfg.mae2a.common.ModCreativeModTabs;
import com.easterfg.mae2a.common.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author EasterFG on 2024/9/17
 */
@Mod(MoreAE2Additions.MOD_ID)
public class MoreAE2Additions {
    public static final String MOD_ID = "mae2a";
    public static final Logger LOGGER = LogManager.getLogger();

    public static MoreAE2Additions INSTANCE;

    public MoreAE2Additions() {
        INSTANCE = this;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        GuiRegister.register();
        NetworkRegister.register();
        CellRegister.register();
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }
}