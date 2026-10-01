package com.huziyang520.keenlib.neoforge;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.KeenLib;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * KeenLib 是客户端与服务端均可用、且允许仅客户端安装的共享库，故不限定 dist（默认双端加载）。
 * 配置界面属于客户端功能，只在客户端分支注册。
 */
@Mod(Constants.MOD_ID)
public class KeenLibNeoForge {

    public KeenLibNeoForge(IEventBus eventBus, ModContainer container) {

        Constants.LOG.debug("KeenNeoForge constructor");
        KeenLib.init();
        if (isClient()) {
            KeenLibNeoForgeClient.registerConfigScreen(container);
        }
    }

    /**
     * 判定是否运行在客户端。
     *
     * <p>用「客户端专属类是否存在」来判断，而不是引用某个加载器的运行端 API——专用服务端不会加载客户端类，
     * 因此该判定与 NeoForge 版本无关，且不会在服务端触发客户端类加载。
     */
    private static boolean isClient() {
        try {
            Class.forName("net.neoforged.neoforge.client.gui.IConfigScreenFactory");
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
