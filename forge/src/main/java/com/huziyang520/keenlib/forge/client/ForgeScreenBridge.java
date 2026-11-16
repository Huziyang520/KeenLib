package com.huziyang520.keenlib.forge.client;

import com.huziyang520.keenlib.platform.Services;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

/**
 * Attaches the ported NeoForge configuration screen to one mod container.
 *
 * <p>Client-only and port-gated: the port's types are named here and nowhere else on the server-visible path.
 */
public final class ForgeScreenBridge {

    /** mod id of the optional prerequisite that supplies the screen on this loader. */
    public static final String PORT_ID = "neoconfigurationscreenport";

    private ForgeScreenBridge() {
    }

    public static void install(ModContainer container) {
        if (!Services.PLATFORM.isModLoaded(PORT_ID)) {
            return;
        }
        container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> create(container, parent)));
    }

    private static Screen create(ModContainer container, Screen parent) {
        return new ConfigurationScreen(container, parent);
    }
}
