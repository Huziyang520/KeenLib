package com.huziyang520.keenlib.fabric;

import com.huziyang520.keenlib.client.KeenConfigScreenHook;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Mod Menu 联动（可选）：装上 Mod Menu 时，在模组列表里给 KeenLib 提供「配置」入口。
 *
 * <p>未安装 Mod Menu 时本类不会被加载；未安装 Cloth Config 时返回 {@code null}（Mod Menu 约定：不显示配置按钮）。
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!KeenConfigScreenHook.isAvailable()) {
            return null;
        }
        return KeenConfigScreenHook::create;
    }
}
