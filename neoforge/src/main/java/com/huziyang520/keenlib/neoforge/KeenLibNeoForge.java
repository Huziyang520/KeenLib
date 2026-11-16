package com.huziyang520.keenlib.neoforge;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.KeenLib;
import com.huziyang520.keenlib.neoforge.platform.NeoForgeConfigSpecHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class KeenLibNeoForge {

    public KeenLibNeoForge(IEventBus eventBus, ModContainer container) {

        Constants.LOG.debug("KeenNeoForge constructor");
        KeenLib.init();
        NeoForgeConfigSpecHelper.install(eventBus);
        if (isClient()) {
            KeenLibNeoForgeClient.registerConfigScreen(container);
        }
    }

    private static boolean isClient() {
        try {
            Class.forName("net.neoforged.neoforge.client.gui.IConfigScreenFactory");
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
