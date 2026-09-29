package com.huziyang520.keenlib.gui;

import net.minecraft.client.gui.screens.Screen;

/**
 * 「ESC 即保存并退出」的通用抽象：需要该行为的 Screen（通常由功能模组的 Mixin 实现）
 * 实现 {@link #keenlib$applyChanges()}，把“应用更改”按钮的处理逻辑委托进来。
 */
public interface AutoSaveScreen {

    /**
     * 应用界面上的未保存修改。返回 false 表示应用失败（此时不应关闭界面）。
     */
    boolean keenlib$applyChanges();
}
