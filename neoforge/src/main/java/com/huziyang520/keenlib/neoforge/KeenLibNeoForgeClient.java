package com.huziyang520.keenlib.neoforge;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.client.KeenAggregateScreen;
import com.huziyang520.keenlib.client.KeenConfigScreenHook;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ConfigTracker;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;

/** Registers KeenLib's own config file and points its mod list entry at the aggregate screen. */
public final class KeenLibNeoForgeClient {

    private KeenLibNeoForgeClient() {
    }

    public static void registerConfigScreen(ModContainer container) {
        ModConfig config;
        try {
            ModConfigSpec spec = new ModConfigSpec.Builder().build();
            config = ConfigTracker.INSTANCE.registerConfig(ModConfig.Type.CLIENT, spec, container,
                    "keenlib-client.toml");
        } catch (Throwable t) {
            Constants.LOG.error("Failed to register the KeenLib config file", t);
            return;
        }
        container.registerExtensionPoint(IConfigScreenFactory.class, (minecraft, parent) -> screen(container, config, parent));
        KeenConfigScreenHook.install(parent -> screen(container, config, parent));
    }

    private static net.minecraft.client.gui.screens.Screen screen(ModContainer container, ModConfig config,
                                                                 net.minecraft.client.gui.screens.Screen parent) {
        try {
            return new KeenAggregateScreen(parent, config);
        } catch (Throwable t) {
            Constants.LOG.error("Aggregate screen failed; showing the plain native screen", t);
            return new net.neoforged.neoforge.client.gui.ConfigurationScreen(container, parent,
                    (context, key, element) -> element);
        }
    }
}
