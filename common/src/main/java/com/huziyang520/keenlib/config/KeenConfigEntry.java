package com.huziyang520.keenlib.config;

import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class KeenConfigEntry<T> {

    public enum Kind {
        BOOLEAN,
        INT,
        DOUBLE,
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
    private final double minDecimal;
    private final double maxDecimal;
    private Supplier<Boolean> enabled = () -> true;

    private KeenConfigEntry(Kind kind, String key, Component label, List<Component> tooltip, T defaultValue,
                            Supplier<T> getter, Consumer<T> setter, int min, int max,
                            double minDecimal, double maxDecimal) {
        this.kind = kind;
        this.key = key;
        this.label = label;
        this.tooltip = List.copyOf(tooltip);
        this.defaultValue = defaultValue;
        this.getter = getter;
        this.setter = setter;
        this.min = min;
        this.max = max;
        this.minDecimal = minDecimal;
        this.maxDecimal = maxDecimal;
    }

    static KeenConfigEntry<Boolean> bool(String key, Component label, boolean def, List<Component> tooltip,
                                         Supplier<Boolean> getter, Consumer<Boolean> setter) {
        return new KeenConfigEntry<>(Kind.BOOLEAN, key, label, tooltip, def, getter, setter, 0, 0, 0, 0);
    }

    static KeenConfigEntry<Integer> integer(String key, Component label, int def, int min, int max,
                                            List<Component> tooltip,
                                            Supplier<Integer> getter, Consumer<Integer> setter) {
        return new KeenConfigEntry<>(Kind.INT, key, label, tooltip, def, getter, setter, min, max, 0, 0);
    }

    static KeenConfigEntry<Double> decimal(String key, Component label, double def, double min, double max,
                                           List<Component> tooltip,
                                           Supplier<Double> getter, Consumer<Double> setter) {
        return new KeenConfigEntry<>(Kind.DOUBLE, key, label, tooltip, def, getter, setter, 0, 0, min, max);
    }

    static KeenConfigEntry<String> string(String key, Component label, String def, List<Component> tooltip,
                                          Supplier<String> getter, Consumer<String> setter) {
        return new KeenConfigEntry<>(Kind.STRING, key, label, tooltip, def, getter, setter, 0, 0, 0, 0);
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

    public void set(T value) {
        setter.accept(value);
    }

    public int min() {
        return min;
    }

    public int max() {
        return max;
    }

    public double minDecimal() {
        return minDecimal;
    }

    public double maxDecimal() {
        return maxDecimal;
    }

    public boolean enabled() {
        return enabled.get();
    }

    public void setEnabled(Supplier<Boolean> rule) {
        this.enabled = rule;
    }

    void writeDefaultIfAbsent(KeenConfig file) {
        if (file.has(key)) {
            return;
        }
        switch (kind) {
            case BOOLEAN -> file.set(key, (Boolean) defaultValue);
            case INT -> file.set(key, (Integer) defaultValue);
            case DOUBLE -> file.set(key, (Double) defaultValue);
            case STRING -> file.set(key, (String) defaultValue);
        }
    }
}
