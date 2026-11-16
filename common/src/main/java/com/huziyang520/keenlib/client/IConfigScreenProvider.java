package com.huziyang520.keenlib.client;

import net.minecraft.client.gui.screens.Screen;

/**
 * Client-only entry: opens the loader's native configuration screen for KeenLib's own container.
 *
 * <p>Kept out of {@code platform.services} so nothing on the server-side path ever links a {@link Screen}.
 */
public interface IConfigScreenProvider {

    /** Whether this loader can produce the native screen right now (its optional prerequisite is installed). */
    boolean available();

    /** @return the aggregate native screen, or {@code null} when unavailable */
    Screen create(Screen parent);
}
