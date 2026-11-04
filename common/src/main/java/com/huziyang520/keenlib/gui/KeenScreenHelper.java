package com.huziyang520.keenlib.gui;

import net.minecraft.client.gui.screens.Screen;

/**
 * Screen 关闭路径的统一入口。功能模组的 Mixin 在 ESC 按键/onClose 处理中调用
 * {@link #applyChanges(Screen)}：目标实现了 {@link AutoSaveScreen} 就先应用修改。
 */
public final class KeenScreenHelper {

    private KeenScreenHelper() {
    }

    /**
     * 尝试应用未保存的修改。返回 true 表示已应用、可以继续关闭流程；
     * 返回 false 表示目标不实现 {@link AutoSaveScreen} 或应用失败。
     */
    public static boolean applyChanges(Screen screen) {

        return screen instanceof AutoSaveScreen autoSave && autoSave.keenlib$applyChanges();
    }
}
