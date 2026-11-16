package com.huziyang520.keenlib.fabric.client;

import com.huziyang520.keenlib.Constants;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

/** Holds the only reference to Forge Config API Port's screen class, so it loads only when that mod is present. */
final class FcapScreenBridge {

    private FcapScreenBridge() {
    }

    static Screen create(Screen parent) {
        return new ConfigurationScreen(Constants.MOD_ID, parent, (context, key, element) -> element);
    }
}
