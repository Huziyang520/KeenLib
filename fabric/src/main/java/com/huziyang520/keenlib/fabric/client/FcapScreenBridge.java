package com.huziyang520.keenlib.fabric.client;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.client.KeenConfigScreenHook;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.common.ModConfigSpec;

/** Holds the only references to Forge Config API Port's screen and registry types. */
public final class FcapScreenBridge {

    private FcapScreenBridge() {
    }

    /** Registers KeenLib's own config file and its screen factory. */
    public static void installKeenLib() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.translation("gui.keenlib.notices.enable");
        builder.define("notices_enabled", true);
        ConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.CLIENT, builder.build(),
                "keenlib-client.toml");
        ConfigScreenFactoryRegistry.INSTANCE.register(Constants.MOD_ID, FcapScreenBridge::create);
        KeenConfigScreenHook.install(parent -> create(Constants.MOD_ID, parent));
    }

    /** Offers the native screen on one mod's entry in the mod list. */
    public static void install(String modId) {
        ConfigScreenFactoryRegistry.INSTANCE.register(modId, FcapScreenBridge::create);
    }

    private static Screen create(String modId, Screen parent) {
        return new ConfigurationScreen(modId, parent, (context, key, element) -> element);
    }
}
