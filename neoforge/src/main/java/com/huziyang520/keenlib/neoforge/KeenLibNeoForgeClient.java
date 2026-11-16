package com.huziyang520.keenlib.neoforge;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.client.KeenConfigScreenHook;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;

/** Registers KeenLib's own config file and points its mod list entry at the native screen. */
public final class KeenLibNeoForgeClient {

    private KeenLibNeoForgeClient() {
    }

    public static void registerConfigScreen(ModContainer container) {
        try {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
            builder.translation("gui.keenlib.notices.enable");
            builder.define("notices_enabled", true);
            container.registerConfig(ModConfig.Type.CLIENT, builder.build(), "keenlib-client.toml");
        } catch (Throwable t) {
            Constants.LOG.error("Failed to register the KeenLib config file", t);
        }
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (minecraft, parent) -> new ConfigurationScreen(container, parent, (context, key, element) -> element));
        KeenConfigScreenHook.install(parent ->
                new ConfigurationScreen(container, parent, (context, key, element) -> element));
    }
}
