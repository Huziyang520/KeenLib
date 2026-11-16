package com.huziyang520.keenlib.client;

import net.minecraft.client.gui.screens.Screen;

/**
 * Entry point for KeenLib's own configuration screen.
 *
 * <p>The screen is the loader's native one; each loader installs a factory during client setup.
 */
public final class KeenConfigScreenHook {

    /** Creates the native screen for KeenLib's own config file. */
    @FunctionalInterface
    public interface Factory {
        Screen create(Screen parent);
    }

    private static Factory factory;

    private KeenConfigScreenHook() {
    }

    public static void install(Factory screenFactory) {
        factory = screenFactory;
    }

    public static boolean isAvailable() {
        return factory != null;
    }

    public static Screen create(Screen parent) {
        return factory == null ? null : factory.create(parent);
    }
}
