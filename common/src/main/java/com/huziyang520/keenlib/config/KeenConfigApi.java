package com.huziyang520.keenlib.config;

import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
public final class KeenConfigApi {

    private static final Map<String, KeenBusinessConfig> BUSINESS = new LinkedHashMap<>();

    private KeenConfigApi() {
    }
    public static KeenBusinessConfig business(String modId, String displayName) {
        return business(modId, Component.literal(displayName));
    }
    public static KeenBusinessConfig business(String modId, Component displayName) {
        return BUSINESS.computeIfAbsent(modId, id -> new KeenBusinessConfig(id, displayName));
    }
    public static Collection<KeenBusinessConfig> businesses() {
        return BUSINESS.values();
    }
    public static KeenBusinessConfig find(String modId) {
        return BUSINESS.get(modId);
    }
    public static boolean hasBusinesses() {
        return !BUSINESS.isEmpty();
    }
}
