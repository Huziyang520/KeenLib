package com.huziyang520.keenlib.config;

import com.huziyang520.keenlib.notice.KeenNoticePreferences;
import com.huziyang520.keenlib.platform.Services;
import com.huziyang520.keenlib.text.KeenText;
import com.huziyang520.keenlib.platform.services.SpecBinding;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

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

    public KeenConfig file() {
        return file;
    }

    public KeenBusinessConfig booleanToggle(String key, boolean defaultValue, Component label, Component... tooltip) {
        entries.add(KeenConfigEntry.bool(key, label, defaultValue, List.of(tooltip),
                () -> file.getBoolean(key, defaultValue),
                value -> write(key, value)));
        return this;
    }

    public KeenBusinessConfig intSlider(String key, int defaultValue, int min, int max, Component label, Component... tooltip) {
        entries.add(KeenConfigEntry.integer(key, label, defaultValue, min, max, List.of(tooltip),
                () -> file.getInt(key, defaultValue),
                value -> write(key, value)));
        return this;
    }

    public KeenBusinessConfig doubleSlider(String key, double defaultValue, double min, double max, Component label,
                                           Component... tooltip) {
        entries.add(KeenConfigEntry.decimal(key, label, defaultValue, min, max, List.of(tooltip),
                () -> file.getDouble(key, defaultValue),
                value -> write(key, value)));
        return this;
    }

    public KeenBusinessConfig dependsOn(String otherKey) {
        if (!entries.isEmpty()) {
            entries.get(entries.size() - 1).setEnabled(() -> file.getBoolean(otherKey, true));
        }
        return this;
    }

    public KeenBusinessConfig stringField(String key, String defaultValue, Component label, Component... tooltip) {
        entries.add(KeenConfigEntry.string(key, label, defaultValue, List.of(tooltip),
                () -> file.getString(key, defaultValue),
                value -> write(key, value)));
        return this;
    }

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

    /** Adds the join-notice switch so it shows up on this mod's own config screen. */
    public KeenBusinessConfig noticeToggle() {
        entries.add(KeenConfigEntry.bool(KeenNoticePreferences.KEY,
                KeenText.trans("gui.keenlib.notices.enable", "Enable join notices"), true, List.of(),
                () -> KeenNoticePreferences.isEnabled(modId, true),
                value -> KeenNoticePreferences.setEnabled(modId, value)));
        return this;
    }

    public void submit() {
        noticeToggle();
        SpecBinding binding = Services.CONFIG_SPEC.register(this, modId + "-client.toml");
        if (binding != null) {
            KeenConfigBindings.bind(modId, binding);
            return;
        }
        for (KeenConfigEntry<?> entry : entries) {
            entry.writeDefaultIfAbsent(file);
        }
        file.save();
    }
}
