package com.huziyang520.keenlib.config;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个业务模组在 KeenLib 配置界面中的配置组。
 *
 * <p>业务模组这样使用（**不需要依赖 Cloth Config**）：
 * <pre>{@code
 * KeenConfigApi.business("mymod", "My Mod")
 *         .booleanToggle("enabled", true, Component.literal("Enable feature"))
 *         .submit();
 * }</pre>
 *
 * <p>值直接读写该模组自己的 {@code config/<modId>.json}；`submit()` 会把尚不存在的键写成默认值并落盘。
 */
public final class KeenBusinessConfig {

    private final String modId;
    private final Component displayName;
    private final KeenConfig file;
    private final List<KeenConfigEntry<?>> entries = new ArrayList<>();

    KeenBusinessConfig(String modId, Component displayName) {
        this.modId = modId;
        this.displayName = displayName;
        this.file = KeenConfig.create(modId);
    }

    public String modId() {
        return modId;
    }

    public Component displayName() {
        return displayName;
    }

    public List<KeenConfigEntry<?>> entries() {
        return entries;
    }

    /** 该模组自己的配置文件（{@code config/<modId>.json}）。 */
    public KeenConfig file() {
        return file;
    }

    /** 新增一个开关。 */
    public KeenBusinessConfig booleanToggle(String key, boolean defaultValue, Component label, Component... tooltip) {
        entries.add(KeenConfigEntry.bool(key, label, defaultValue, List.of(tooltip),
                () -> file.getBoolean(key, defaultValue),
                value -> write(key, value)));
        return this;
    }

    /** 新增一个整数滑条。 */
    public KeenBusinessConfig intSlider(String key, int defaultValue, int min, int max, Component label, Component... tooltip) {
        entries.add(KeenConfigEntry.integer(key, label, defaultValue, min, max, List.of(tooltip),
                () -> file.getInt(key, defaultValue),
                value -> write(key, value)));
        return this;
    }

    /** 新增一个文本输入框。 */
    public KeenBusinessConfig stringField(String key, String defaultValue, Component label, Component... tooltip) {
        entries.add(KeenConfigEntry.string(key, label, defaultValue, List.of(tooltip),
                () -> file.getString(key, defaultValue),
                value -> write(key, value)));
        return this;
    }

    /**
     * 写值并**立即落盘**。
     *
     * <p>不依赖「保存按钮 → savingRunnable」的顺序：不同版本的 Cloth Config 里，savingRunnable 与各配置项
     * save 回调的先后并不一致，先跑 runnable 就会把旧值写进文件，表现为「改了又变回去」。
     */
    private void write(String key, Object value) {
        if (value instanceof Boolean bool) {
            file.set(key, bool);
        } else if (value instanceof Number number) {
            file.set(key, number);
        } else if (value instanceof String text) {
            file.set(key, text);
        }
        file.save();
    }

    /** 补齐缺失的默认值并写回磁盘。 */
    public void submit() {
        for (KeenConfigEntry<?> entry : entries) {
            entry.writeDefaultIfAbsent(file);
        }
        file.save();
    }
}
