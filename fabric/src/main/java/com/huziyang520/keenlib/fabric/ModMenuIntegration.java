package com.huziyang520.keenlib.fabric;

import com.huziyang520.keenlib.client.KeenConfigScreenHook;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/** Optional Mod Menu entry for KeenLib's own screen; absent without Forge Config API Port. */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!KeenConfigScreenHook.isAvailable()) {
            return null;
        }
        return KeenConfigScreenHook::create;
    }
}
