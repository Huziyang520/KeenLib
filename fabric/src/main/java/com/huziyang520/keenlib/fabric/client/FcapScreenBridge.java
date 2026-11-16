package com.huziyang520.keenlib.fabric.client;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.client.KeenAggregateScreen;
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
        String fileName = "keenlib-client.toml";
        ModConfigSpec spec = new ModConfigSpec.Builder().build();
        ConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.CLIENT, spec, fileName);
        ModConfig config = ownConfig(spec, fileName);
        if (config != null) {
            ConfigScreenFactoryRegistry.INSTANCE.register(Constants.MOD_ID, (id, parent) -> aggregate(config, parent));
            KeenConfigScreenHook.install(parent -> aggregate(config, parent));
        } else {
            ConfigScreenFactoryRegistry.INSTANCE.register(Constants.MOD_ID, FcapScreenBridge::create);
            KeenConfigScreenHook.install(parent -> create(Constants.MOD_ID, parent));
        }
    }

    private static Screen aggregate(ModConfig config, Screen parent) {
        try {
            return new KeenAggregateScreen(parent, config);
        } catch (Throwable t) {
            Constants.LOG.error("Aggregate screen failed; showing the plain native screen", t);
            return create(Constants.MOD_ID, parent);
        }
    }

    private static ModConfig ownConfig(ModConfigSpec spec, String fileName) {
        // Forge Config API Port keeps this constructor package-private, so the handle is fetched reflectively.
        try {
            java.lang.reflect.Constructor<?> ctor = ModConfig.class.getDeclaredConstructor(
                    ModConfig.Type.class, net.neoforged.fml.config.IConfigSpec.class, String.class, String.class,
                    java.util.concurrent.locks.ReentrantLock.class);
            ctor.setAccessible(true);
            return (ModConfig) ctor.newInstance(ModConfig.Type.CLIENT, spec, Constants.MOD_ID, fileName,
                    new java.util.concurrent.locks.ReentrantLock());
        } catch (Throwable t) {
            Constants.LOG.error("Failed to build the KeenLib config handle; falling back to the plain screen", t);
            return null;
        }
    }

    private static Screen create(String modId, Screen parent) {
        return new ConfigurationScreen(modId, parent, (context, key, element) -> element);
    }
}
