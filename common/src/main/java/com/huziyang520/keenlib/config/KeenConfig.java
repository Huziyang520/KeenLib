package com.huziyang520.keenlib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.platform.Services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 轻量 JSON 配置封装：每个模组一个实例，读写实例 config 目录下的单个 JSON 文件。
 * 用法：KeenConfig.create("mymod")，读取用 get 系列方法，写入用 set 系列方法，最后 save()。
 */
public final class KeenConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Path file;
    private final JsonObject root;

    private KeenConfig(Path file, JsonObject root) {
        this.file = file;
        this.root = root;
    }

    private static final Map<Path, KeenConfig> CACHE = new HashMap<>();

    /**
     * 加载（或创建）{@code <config目录>/<modId>.json}。文件缺失或损坏时返回空配置，损坏会记录日志但不会抛出。
     *
     * <p><b>同一路径只返回同一个实例</b>：否则不同调用方各自持有一份内存副本，界面改了 A 副本、业务代码读 B 副本，
     * 就会出现「改了值却不生效 / 重新打开又变回去」的问题。
     */
    public static synchronized KeenConfig create(String modId) {

        Path file = Services.PLATFORM.getConfigDir().resolve(modId + ".json");
        return CACHE.computeIfAbsent(file, KeenConfig::load);
    }

    private static KeenConfig load(Path file) {

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
        return new KeenConfig(file, root);
    }

    public boolean getBoolean(String key, boolean fallback) {

        JsonElement element = root.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsBoolean() : fallback;
    }

    public int getInt(String key, int fallback) {

        JsonElement element = root.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsInt() : fallback;
    }

    public String getString(String key, String fallback) {

        JsonElement element = root.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsString() : fallback;
    }

    public void set(String key, boolean value) {
        root.addProperty(key, value);
    }

    public void set(String key, Number value) {
        root.addProperty(key, value);
    }

    public void set(String key, String value) {
        root.addProperty(key, value);
    }

    public boolean has(String key) {
        return root.has(key);
    }

    public void remove(String key) {
        root.remove(key);
    }

    /** 将当前配置写回磁盘。写入失败只记录日志。 */
    public void save() {

        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON.toJson(root), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Constants.LOG.error("Failed to save config {}", file, e);
        }
    }
}
