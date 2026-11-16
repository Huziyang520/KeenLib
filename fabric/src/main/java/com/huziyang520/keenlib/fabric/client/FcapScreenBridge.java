package com.huziyang520.keenlib.fabric.client;

import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

/** Holds the only reference to Forge Config API Port's screen class, so it loads only when that mod is present. */
public final class FcapScreenBridge {

    private FcapScreenBridge() {
    }

    /** Offers the native screen on one mod's entry in the mod list. */
    public static void install(String modId) {
        ConfigScreenFactoryRegistry.INSTANCE.register(modId,
                (id, parent) -> new ConfigurationScreen(id, parent, (context, key, element) -> element));
    }
}
