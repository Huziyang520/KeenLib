package com.huziyang520.keenlib.neoforge.client;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.client.IConfigScreenProvider;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

/** Opens NeoForge's own configuration screen for KeenLib's container, which lists one file per business mod. */
public final class NeoForgeScreenProvider implements IConfigScreenProvider {

    @Override
    public boolean available() {
        return true;
    }

    @Override
    public Screen create(Screen parent) {
        ModContainer container = ModList.get().getModContainerById(Constants.MOD_ID).orElse(null);
        if (container == null) {
            return null;
        }
        return new ConfigurationScreen(container, parent, (context, key, element) -> element);
    }
}
