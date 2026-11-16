package com.huziyang520.keenlib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.platform.Services;
import com.huziyang520.keenlib.platform.services.SpecBinding;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class KeenConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final String modId;
    private final Path file;
    private final JsonObject root;

    private KeenConfig(String modId, Path file, JsonObject root) {
        this.modId = modId;
        this.file = file;
        this.root = root;
    }

    private static final Map<Path, KeenConfig> CACHE = new HashMap<>();

    public static synchronized KeenConfig create(String modId) {

        Path file = Services.PLATFORM.getConfigDir().resolve(modId + ".json");
        return CACHE.computeIfAbsent(file, path -> load(modId, path));
    }

    private static KeenConfig load(String modId, Path file) {

        JsonObject root = new JsonObject();
        if (Files.exists(file)) {
            try {
                JsonElement parsed = JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8));
                if (parsed.isJsonObject()) {
                    root = parsed.getAsJsonObject();
                }
            } catch (IOException | RuntimeException e) {
                Constants.LOG.error("Failed to read config {}, starting with defaults", file, e);
            }
        }
        return new KeenConfig(modId, file, root);
    }

    public boolean getBoolean(String key, boolean fallback) {

        SpecBinding binding = KeenConfigBindings.get(modId);
        if (binding != null && binding.has(key)) {
            return binding.getBoolean(key, fallback);
        }
        JsonElement element = root.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsBoolean() : fallback;
    }

    public int getInt(String key, int fallback) {

        SpecBinding binding = KeenConfigBindings.get(modId);
        if (binding != null && binding.has(key)) {
            return binding.getInt(key, fallback);
        }
        JsonElement element = root.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsInt() : fallback;
    }

    public double getDouble(String key, double fallback) {

        SpecBinding binding = KeenConfigBindings.get(modId);
        if (binding != null && binding.has(key)) {
            return binding.getDouble(key, fallback);
        }
        JsonElement element = root.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsDouble() : fallback;
    }

    public String getString(String key, String fallback) {

        SpecBinding binding = KeenConfigBindings.get(modId);
        if (binding != null && binding.has(key)) {
            return binding.getString(key, fallback);
        }
        JsonElement element = root.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsString() : fallback;
    }

    public void set(String key, boolean value) {
        SpecBinding binding = KeenConfigBindings.get(modId);
        if (binding != null && binding.has(key)) {
            binding.set(key, value);
            return;
        }
        root.addProperty(key, value);
    }

    public void set(String key, Number value) {
        SpecBinding binding = KeenConfigBindings.get(modId);
        if (binding != null && binding.has(key)) {
            binding.set(key, value);
            return;
        }
        root.addProperty(key, value);
    }

    public void set(String key, String value) {
        SpecBinding binding = KeenConfigBindings.get(modId);
        if (binding != null && binding.has(key)) {
            binding.set(key, value);
            return;
        }
        root.addProperty(key, value);
    }

    public boolean has(String key) {
        SpecBinding binding = KeenConfigBindings.get(modId);
        if (binding != null && binding.has(key)) {
            return true;
        }
        return root.has(key);
    }

    public void remove(String key) {
        root.remove(key);
    }

    public void save() {

        SpecBinding binding = KeenConfigBindings.get(modId);
        if (binding != null) {
            binding.save();
            return;
        }
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON.toJson(root), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Constants.LOG.error("Failed to save config {}", file, e);
        }
    }
}
