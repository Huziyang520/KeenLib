package com.huziyang520.keenlib.neoforge.client;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/**
 * Attaches NeoForge's own configuration screen to one mod container.
 *
 * <p>Client-only: reached from the spec helper only after the side check, so a dedicated server never loads it.
 */
public final class NeoForgeScreenBridge {

    private NeoForgeScreenBridge() {
    }

    public static void install(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (minecraft, parent) -> create(container, parent));
    }

    private static Screen create(ModContainer container, Screen parent) {
        return new ConfigurationScreen(container, parent, (context, key, element) -> element);
    }
}
