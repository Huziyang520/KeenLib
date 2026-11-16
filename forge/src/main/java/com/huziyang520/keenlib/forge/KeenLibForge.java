package com.huziyang520.keenlib.forge;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.KeenLib;
import com.huziyang520.keenlib.forge.platform.ForgeConfigSpecHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class KeenLibForge {

    public KeenLibForge() {
        KeenLib.init();
        ForgeConfigSpecHelper.install(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(KeenLibLootEvents.class);
        if (isClient()) {
            KeenLibForgeClient.init(ModLoadingContext.get());
        }
    }

    private static boolean isClient() {
        try {
            Class.forName("net.minecraftforge.client.ConfigScreenHandler");
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
