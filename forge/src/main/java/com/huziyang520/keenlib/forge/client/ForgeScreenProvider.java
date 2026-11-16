package com.huziyang520.keenlib.forge.client;

import com.huziyang520.keenlib.client.IConfigScreenProvider;
import com.huziyang520.keenlib.platform.Services;
import net.minecraft.client.gui.screens.Screen;

/** Client entry for the ported NeoForge configuration screen; Forge itself has no config screen. */
public final class ForgeScreenProvider implements IConfigScreenProvider {

    /** mod id of the optional prerequisite that supplies the screen on this loader. */
    public static final String PORT_ID = "neoconfigurationscreenport";

    @Override
    public boolean available() {
        return Services.PLATFORM.isModLoaded(PORT_ID);
    }

    @Override
    public Screen create(Screen parent) {
        return available() ? PortScreenBridge.create(parent) : null;
    }
}
