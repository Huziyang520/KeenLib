package com.huziyang520.keenlib.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class KeenText {

    private KeenText() {
    }

    public static MutableComponent trans(String key, String fallback) {
        return Component.translatableWithFallback(key, fallback);
    }

    public static MutableComponent trans(String key, String fallback, Object... args) {
        return Component.translatableWithFallback(key, fallback, args);
    }

    public static MutableComponent trans(String key) {
        return Component.translatable(key);
    }

    public static MutableComponent literal(String text) {
        return Component.literal(text);
    }
}
