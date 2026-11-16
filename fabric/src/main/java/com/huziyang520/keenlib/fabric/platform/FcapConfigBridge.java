package com.huziyang520.keenlib.fabric.platform;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.config.KeenBusinessConfig;
import com.huziyang520.keenlib.config.KeenConfig;
import com.huziyang520.keenlib.config.KeenConfigEntry;
import com.huziyang520.keenlib.config.KeenConfigMigration;
import com.huziyang520.keenlib.config.KeenLabelKeys;
import com.huziyang520.keenlib.fabric.client.FcapScreenBridge;
import com.huziyang520.keenlib.platform.Services;
import com.huziyang520.keenlib.platform.services.SpecBinding;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builds the spec through Forge Config API Port and registers it under KeenLib's mod id.
 *
 * <p>Only reached when that mod is installed; the caller keeps its types out of the always-loaded path.
 */
final class FcapConfigBridge {

    private FcapConfigBridge() {
    }

    static SpecBinding register(KeenBusinessConfig config, String fileName) {
        boolean missing = !Files.exists(Services.PLATFORM.getConfigDir().resolve(fileName));
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        Map<String, ModConfigSpec.ConfigValue<?>> values = new LinkedHashMap<>();
        for (KeenConfigEntry<?> entry : config.entries()) {
            define(builder, entry, values);
        }
        ModConfigSpec spec = builder.build();
        ConfigRegistry.INSTANCE.register(config.modId(), ModConfig.Type.CLIENT, spec, fileName);
        if (Services.PLATFORM.isClientSide()) {
            FcapScreenBridge.install(config.modId());
        }
        Binding binding = new Binding(spec, values);
        if (missing && spec.isLoaded()) {
            KeenConfigMigration.importJson(KeenConfig.create(config.modId()), config.entries(), binding);
        }
        return binding;
    }

    private static void define(ModConfigSpec.Builder builder, KeenConfigEntry<?> entry,
                               Map<String, ModConfigSpec.ConfigValue<?>> values) {
        String comment = KeenLabelKeys.commentOf(entry.tooltip());
        if (comment != null) {
            builder.comment(comment);
        }
        String labelKey = KeenLabelKeys.keyOf(entry.label());
        if (labelKey != null) {
            builder.translation(labelKey);
        }
        String key = entry.key();
        // The primitive overloads return BooleanValue / IntValue / DoubleValue; the generic one returns a plain
        // ConfigValue, which the native screen renders as an unsupported element.
        switch (entry.kind()) {
            case BOOLEAN -> values.put(key, builder.define(key, ((Boolean) entry.defaultValue()).booleanValue()));
            case INT -> values.put(key, builder.defineInRange(key, ((Integer) entry.defaultValue()).intValue(),
                    entry.min(), entry.max()));
            case DOUBLE -> values.put(key, builder.defineInRange(key, ((Double) entry.defaultValue()).doubleValue(),
                    entry.minDecimal(), entry.maxDecimal()));
            default -> values.put(key, builder.define(key, (String) entry.defaultValue()));
        }
    }

    private static final class Binding implements SpecBinding {

        private final ModConfigSpec spec;
        private final Map<String, ModConfigSpec.ConfigValue<?>> values;

        private Binding(ModConfigSpec spec, Map<String, ModConfigSpec.ConfigValue<?>> values) {
            this.spec = spec;
            this.values = values;
        }

        private boolean loaded() {
            return spec.isLoaded();
        }

        @Override
        public boolean has(String key) {
            return values.containsKey(key);
        }

        @Override
        public boolean getBoolean(String key, boolean fallback) {
            Object value = loaded() ? get(key) : null;
            return value instanceof Boolean flag ? flag : fallback;
        }

        @Override
        public int getInt(String key, int fallback) {
            Object value = loaded() ? get(key) : null;
            return value instanceof Number number ? number.intValue() : fallback;
        }

        @Override
        public double getDouble(String key, double fallback) {
            Object value = loaded() ? get(key) : null;
            return value instanceof Number number ? number.doubleValue() : fallback;
        }

        @Override
        public String getString(String key, String fallback) {
            Object value = loaded() ? get(key) : null;
            return value instanceof String text ? text : fallback;
        }

        private Object get(String key) {
            ModConfigSpec.ConfigValue<?> value = values.get(key);
            return value == null ? null : value.get();
        }

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public void set(String key, Object value) {
            if (!loaded()) {
                return;
            }
            ModConfigSpec.ConfigValue target = values.get(key);
            if (target != null) {
                target.set(value);
            }
        }

        @Override
        public boolean save() {
            if (!loaded()) {
                return false;
            }
            try {
                spec.save();
                return true;
            } catch (Throwable t) {
                Constants.LOG.error("Failed to save config spec", t);
                return false;
            }
        }
    }
}
