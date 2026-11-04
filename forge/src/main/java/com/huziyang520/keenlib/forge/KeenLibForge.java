package com.huziyang520.keenlib.forge;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.KeenLib;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

/**
 * KeenLib 在 Forge 1.20.1 上是双端库：入口不限定 dist，客户端能力单独分支注册。
 */
@Mod(Constants.MOD_ID)
public class KeenLibForge {

    public KeenLibForge() {
        KeenLib.init();
        MinecraftForge.EVENT_BUS.register(KeenLibLootEvents.class);
        if (isClient()) {
            KeenLibForgeClient.init(ModLoadingContext.get());
        }
    }

    private static boolean isClient() {
        try {
            Class.forName("net.minecraftforge.client.ConfigScreenHandler");
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
