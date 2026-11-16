package com.huziyang520.keenlib.forge.platform;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.config.KeenBusinessConfig;
import com.huziyang520.keenlib.config.KeenConfig;
import com.huziyang520.keenlib.config.KeenConfigEntry;
import com.huziyang520.keenlib.config.KeenConfigMigration;
import com.huziyang520.keenlib.config.KeenLabelKeys;
import com.huziyang520.keenlib.platform.Services;
import com.huziyang520.keenlib.platform.services.IConfigSpecHelper;
import com.huziyang520.keenlib.platform.services.SpecBinding;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Builds one {@link ForgeConfigSpec} per business config and attaches it to KeenLib's container under that
 * mod's own file name, so the ported configuration screen lists every business mod as one file.
 */
public final class ForgeConfigSpecHelper implements IConfigSpecHelper {

    private static final Map<String, Pending> PENDING = new ConcurrentHashMap<>();
    private static boolean installed;

    /** Called from the mod constructor with the mod event bus, where config loading events are delivered. */
    public static void install(IEventBus modBus) {
        if (installed) {
            return;
        }
        installed = true;
        modBus.addListener(ForgeConfigSpecHelper::onConfigLoading);
    }

    @Override
    public SpecBinding register(KeenBusinessConfig config, String fileName) {
        try {
            ModContainer container = ModList.get().getModContainerById(Constants.MOD_ID).orElse(null);
            if (container == null) {
                Constants.LOG.error("No mod container for {}; config {} stays JSON", Constants.MOD_ID, fileName);
                return null;
            }
            boolean missing = !Files.exists(Services.PLATFORM.getConfigDir().resolve(fileName));
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            Map<String, ForgeConfigSpec.ConfigValue<?>> values = new LinkedHashMap<>();
            for (KeenConfigEntry<?> entry : config.entries()) {
                define(builder, entry, values);
            }
            ForgeConfigSpec spec = builder.build();
            container.addConfig(new ModConfig(ModConfig.Type.CLIENT, spec, container, fileName));
            Binding binding = new Binding(spec, values);
            PENDING.put(fileName, new Pending(binding, config, missing));
            return binding;
        } catch (Throwable t) {
            Constants.LOG.error("Failed to register config spec {}", fileName, t);
            return null;
        }
    }

    private static void onConfigLoading(ModConfigEvent.Loading event) {
        Pending pending = PENDING.get(event.getConfig().getFileName());
        if (pending != null) {
            pending.importLegacy();
        }
    }

    private static void define(ForgeConfigSpec.Builder builder, KeenConfigEntry<?> entry,
                               Map<String, ForgeConfigSpec.ConfigValue<?>> values) {
        String comment = KeenLabelKeys.commentOf(entry.tooltip());
        if (comment != null) {
            builder.comment(comment);
        }
        String labelKey = KeenLabelKeys.keyOf(entry.label());
        if (labelKey != null) {
            builder.translation(labelKey);
        }
        String key = entry.key();
        switch (entry.kind()) {
            case BOOLEAN -> values.put(key, builder.define(key, (Boolean) entry.defaultValue()));
            case INT -> values.put(key, builder.defineInRange(key, (Integer) entry.defaultValue(),
                    entry.min(), entry.max()));
            case DOUBLE -> values.put(key, builder.defineInRange(key, (Double) entry.defaultValue(),
                    entry.minDecimal(), entry.maxDecimal()));
            default -> values.put(key, builder.define(key, (String) entry.defaultValue()));
        }
    }

    private record Pending(Binding binding, KeenBusinessConfig config, boolean specFileMissing) {

        void importLegacy() {
            if (!specFileMissing || !binding.loaded()) {
                return;
            }
            KeenConfigMigration.importJson(KeenConfig.create(config.modId()), config.entries(), binding);
        }
    }

    private static final class Binding implements SpecBinding {

        private final ForgeConfigSpec spec;
        private final Map<String, ForgeConfigSpec.ConfigValue<?>> values;

        private Binding(ForgeConfigSpec spec, Map<String, ForgeConfigSpec.ConfigValue<?>> values) {
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
            ForgeConfigSpec.ConfigValue<?> value = values.get(key);
            return value == null ? null : value.get();
        }

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public void set(String key, Object value) {
            if (!loaded()) {
                return;
            }
            ForgeConfigSpec.ConfigValue target = values.get(key);
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
