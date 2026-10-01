package com.huziyang520.keenlib.config;

import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 一条配置项的描述，**与任何界面库无关**（不出现 Cloth Config 类型）。
 * 业务模组只描述“键、标签、默认值、读写方式”，由 KeenLib 在检测到 Cloth Config 时渲染成界面。
 *
 * @param <T> 值类型
 */
public final class KeenConfigEntry<T> {

    /** 支持的控件类型。 */
    public enum Kind {
        BOOLEAN,
        INT,
        STRING
    }

    private final Kind kind;
    private final String key;
    private final Component label;
    private final List<Component> tooltip;
    private final T defaultValue;
    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private final int min;
    private final int max;

    private KeenConfigEntry(Kind kind, String key, Component label, List<Component> tooltip, T defaultValue,
                            Supplier<T> getter, Consumer<T> setter, int min, int max) {
        this.kind = kind;
        this.key = key;
        this.label = label;
        this.tooltip = List.copyOf(tooltip);
        this.defaultValue = defaultValue;
        this.getter = getter;
        this.setter = setter;
        this.min = min;
        this.max = max;
    }

    static KeenConfigEntry<Boolean> bool(String key, Component label, boolean def, List<Component> tooltip,
                                         Supplier<Boolean> getter, Consumer<Boolean> setter) {
        return new KeenConfigEntry<>(Kind.BOOLEAN, key, label, tooltip, def, getter, setter, 0, 0);
    }

    static KeenConfigEntry<Integer> integer(String key, Component label, int def, int min, int max,
                                            List<Component> tooltip,
                                            Supplier<Integer> getter, Consumer<Integer> setter) {
        return new KeenConfigEntry<>(Kind.INT, key, label, tooltip, def, getter, setter, min, max);
    }

    static KeenConfigEntry<String> string(String key, Component label, String def, List<Component> tooltip,
                                          Supplier<String> getter, Consumer<String> setter) {
        return new KeenConfigEntry<>(Kind.STRING, key, label, tooltip, def, getter, setter, 0, 0);
    }

    public Kind kind() {
        return kind;
    }

    public String key() {
        return key;
    }

    public Component label() {
        return label;
    }

    public List<Component> tooltip() {
        return tooltip;
    }

    public T defaultValue() {
        return defaultValue;
    }

    public T get() {
        return getter.get();
    }

    /** 写入当前值（只改内存，落盘由保存动作统一完成）。 */
    public void set(T value) {
        setter.accept(value);
    }

    public int min() {
        return min;
    }

    public int max() {
        return max;
    }

    /** 键不存在时把默认值写进配置文件。 */
    void writeDefaultIfAbsent(KeenConfig file) {
        if (file.has(key)) {
            return;
        }
        switch (kind) {
            case BOOLEAN -> file.set(key, (Boolean) defaultValue);
            case INT -> file.set(key, (Integer) defaultValue);
            case STRING -> file.set(key, (String) defaultValue);
        }
    }
}
