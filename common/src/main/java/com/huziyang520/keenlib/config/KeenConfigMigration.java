package com.huziyang520.keenlib.config;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.platform.services.SpecBinding;

import java.util.List;

/**
 * One-shot import of a legacy {@code config/<modId>.json} into the registered spec.
 *
 * <p>Runs only when the target file did not exist before registration, and never deletes the JSON.
 */
public final class KeenConfigMigration {

    private KeenConfigMigration() {
    }

    public static void importJson(KeenConfig json, List<KeenConfigEntry<?>> entries, SpecBinding binding) {
        int moved = 0;
        for (KeenConfigEntry<?> entry : entries) {
            String key = entry.key();
            if (!json.has(key)) {
                continue;
            }
            switch (entry.kind()) {
                case BOOLEAN -> binding.set(key, json.getBoolean(key, (Boolean) entry.defaultValue()));
                case INT -> binding.set(key, json.getInt(key, (Integer) entry.defaultValue()));
                case DOUBLE -> binding.set(key, json.getDouble(key, (Double) entry.defaultValue()));
                case STRING -> binding.set(key, json.getString(key, (String) entry.defaultValue()));
            }
            moved++;
        }
        if (moved > 0 && binding.save()) {
            Constants.LOG.info("Migrated {} value(s) from the legacy JSON config into the config spec", moved);
        }
    }
}
