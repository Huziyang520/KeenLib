package com.huziyang520.keenlib.forge.client;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.platform.Services;
import com.huziyang520.keenlib.platform.services.IPlatformHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.ModList;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import com.huziyang.neoconfigurationscreenport.forge.ConfigScreenRegistration;

/**
 * Holds the only references to the optional screen port, so this class loads only when that mod is present.
 */
public final class PortScreenBridge {

    private PortScreenBridge() {
    }

    /** Attaches the ported screen to KeenLib's mod list entry; must run from KeenLib's own constructor. */
    public static void register() {
        if (!available()) {
            return;
        }
        ConfigScreenRegistration.register(Constants.MOD_ID);
    }

    static Screen create(Screen parent) {
        if (!available()) {
            return null;
        }
        return ModList.get().getModContainerById(Constants.MOD_ID)
                .map(container -> (Screen) new ConfigurationScreen(container, parent))
                .orElse(null);
    }

    private static boolean available() {
        IPlatformHelper platform = Services.PLATFORM;
        return platform.isModLoaded(ForgeScreenProvider.PORT_ID);
    }
}
