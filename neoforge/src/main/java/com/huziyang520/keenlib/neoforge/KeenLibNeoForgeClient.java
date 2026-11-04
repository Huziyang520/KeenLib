package com.huziyang520.keenlib.neoforge;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.client.KeenConfigScreenHook;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/**
 * NeoForge 客户端侧的配置入口注册。
 * 本类只在客户端加载（由 {@link KeenLibNeoForge} 在 {@code Dist.CLIENT} 分支调用）。
 */
public final class KeenLibNeoForgeClient {

    private KeenLibNeoForgeClient() {
    }

    /** 安装了 Cloth Config 时，为 KeenLib 注册原生模组列表里的「配置」按钮。 */
    public static void registerConfigScreen(ModContainer container) {
        if (!KeenConfigScreenHook.isAvailable()) {
            Constants.LOG.debug("Cloth Config not installed; KeenLib config screen is unavailable");
            return;
        }
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (minecraft, parent) -> KeenConfigScreenHook.create(parent));
    }
}
