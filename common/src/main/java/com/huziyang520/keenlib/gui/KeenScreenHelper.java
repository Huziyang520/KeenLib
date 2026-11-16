package com.huziyang520.keenlib.gui;

import net.minecraft.client.gui.screens.Screen;

public final class KeenScreenHelper {

    private KeenScreenHelper() {
    }

    public static boolean applyChanges(Screen screen) {

        return screen instanceof AutoSaveScreen autoSave && autoSave.keenlib$applyChanges();
    }
}
