package com.huziyang520.keenlib.forge.client;

import com.huziyang520.keenlib.platform.Services;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

/**
 * Attaches the ported NeoForge configuration screen to one mod container.
 *
 * <p>Client-only and port-gated: the port's types are named here and nowhere else on the server-visible path.
 */
public final class ForgeScreenBridge {

    /** mod id of the optional prerequisite that supplies the screen on this loader. */
    public static final String PORT_ID = "neoconfigurationscreenport";

    private ForgeScreenBridge() {
    }

    /** Registers KeenLib's own config file and points its mod list entry at the ported screen. */
    public static void registerOwnConfig(net.minecraftforge.fml.ModLoadingContext context) {
        net.minecraftforge.common.ForgeConfigSpec.Builder builder = new net.minecraftforge.common.ForgeConfigSpec.Builder();
        builder.translation("gui.keenlib.notices.enable");
        builder.define("notices_enabled", true);
        net.minecraftforge.common.ForgeConfigSpec spec = builder.build();
        ModContainer container = net.minecraftforge.fml.ModList.get()
                .getModContainerById(com.huziyang520.keenlib.Constants.MOD_ID).orElse(null);
        if (container == null) {
            return;
        }
        container.addConfig(new net.minecraftforge.fml.config.ModConfig(
                net.minecraftforge.fml.config.ModConfig.Type.CLIENT, spec, container, "keenlib-client.toml"));
        if (!Services.PLATFORM.isModLoaded(PORT_ID)) {
            return;
        }
        container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> create(container, parent)));
        com.huziyang520.keenlib.client.KeenConfigScreenHook.install(parent -> create(container, parent));
    }

    public static void install(ModContainer container) {
        if (!Services.PLATFORM.isModLoaded(PORT_ID)) {
            return;
        }
        container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> create(container, parent)));
    }

    public static Screen create(ModContainer container, Screen parent) {
        return new ConfigurationScreen(container, parent);
    }
}
