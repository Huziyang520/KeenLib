package com.huziyang520.keenlib.fabric.client;

import com.huziyang520.keenlib.client.IConfigScreenProvider;
import com.huziyang520.keenlib.platform.Services;
import net.minecraft.client.gui.screens.Screen;

/** Client entry for the native configuration screen on Fabric, which comes from Forge Config API Port. */
public final class FabricScreenProvider implements IConfigScreenProvider {

    private static final String FCAP_ID = "forgeconfigapiport";

    @Override
    public boolean available() {
        return Services.PLATFORM.isModLoaded(FCAP_ID);
    }

    @Override
    public Screen create(Screen parent) {
        return available() ? FcapScreenBridge.create(parent) : null;
    }
}
