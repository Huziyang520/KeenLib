package com.huziyang520.keenlib.fabric;

import com.huziyang520.keenlib.KeenLib;
import com.huziyang520.keenlib.platform.Services;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class KeenLibFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        KeenLib.init();
    }

    @Override
    public void onInitializeClient() {
        KeenLib.init();
        if (Services.PLATFORM.isModLoaded("forgeconfigapiport")) {
            com.huziyang520.keenlib.fabric.client.FcapScreenBridge.installKeenLib();
        }
    }
}
