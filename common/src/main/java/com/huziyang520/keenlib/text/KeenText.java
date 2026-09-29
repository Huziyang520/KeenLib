package com.huziyang520.keenlib.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * 本地化助手：统一可翻译组件的创建入口，带兜底文案，避免 literal 事故（切语言后仍是写死文案）。
 * 语言键规范：gui.&lt;modid&gt;.* / message.&lt;modid&gt;.*。
 */
public final class KeenText {

    private KeenText() {
    }

    /** 可翻译文本；语言文件缺失该键时显示 fallback，而不是 raw key。 */
    public static MutableComponent trans(String key, String fallback) {
        return Component.translatableWithFallback(key, fallback);
    }

    /** 带参数的可翻译文本。 */
    public static MutableComponent trans(String key, String fallback, Object... args) {
        return Component.translatableWithFallback(key, fallback, args);
    }

    /** 无兜底的可翻译文本（确信语言键已齐平时使用）。 */
    public static MutableComponent trans(String key) {
        return Component.translatable(key);
    }

    /** 纯文本（仅用于不进入语言文件的动态内容）。 */
    public static MutableComponent literal(String text) {
        return Component.literal(text);
    }
}
