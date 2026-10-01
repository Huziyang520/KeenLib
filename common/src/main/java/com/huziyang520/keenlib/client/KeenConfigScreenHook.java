package com.huziyang520.keenlib.client;

import com.huziyang520.keenlib.platform.Services;
import net.minecraft.client.gui.screens.Screen;

/**
 * 配置界面的入口守卫。
 *
 * <p>Cloth Config 是**可选联动**（不是依赖）：未安装时本类照常可用，但 {@link #create(Screen)} 返回
 * {@code null}，即“没有配置界面”。引用 Cloth Config 的代码只在与
 * {@link com.huziyang520.keenlib.client.cloth.KeenClothScreen} 相关的分支里，未安装时不会被加载。
 */
public final class KeenConfigScreenHook {

    /**
     * Cloth Config 的 mod id。
     *
     * <p>**两端不一样**：Fabric 上是 `cloth-config`，NeoForge 上因 NeoForge 的 modId 不允许连字符而是
     * `cloth_config`。只按其中一个判断会导致另一端「装了也认不出来」。
     */
    public static final String CLOTH_CONFIG_FABRIC_ID = "cloth-config";

    /** NeoForge 侧的 Cloth Config mod id。 */
    public static final String CLOTH_CONFIG_NEOFORGE_ID = "cloth_config";

    private KeenConfigScreenHook() {
    }

    /** 是否安装了 Cloth Config（两端 id 都检查）。 */
    public static boolean isAvailable() {
        return Services.PLATFORM.isModLoaded(CLOTH_CONFIG_FABRIC_ID)
                || Services.PLATFORM.isModLoaded(CLOTH_CONFIG_NEOFORGE_ID);
    }

    /**
     * 创建配置界面。
     *
     * @param parent 父界面
     * @return 配置界面；未安装 Cloth Config 时返回 {@code null}
     */
    public static Screen create(Screen parent) {
        if (!isAvailable()) {
            return null;
        }
        return com.huziyang520.keenlib.client.cloth.KeenClothScreen.build(parent);
    }
}
