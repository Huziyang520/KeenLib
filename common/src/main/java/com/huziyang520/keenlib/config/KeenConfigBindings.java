package com.huziyang520.keenlib.config;

import com.huziyang520.keenlib.platform.services.SpecBinding;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Routes reads and writes of a registered business config to its spec instead of the JSON file. */
final class KeenConfigBindings {

    private static final Map<String, SpecBinding> BINDINGS = new ConcurrentHashMap<>();

    private KeenConfigBindings() {
    }

    static void bind(String modId, SpecBinding binding) {
        BINDINGS.put(modId, binding);
    }

    static SpecBinding get(String modId) {
        return modId == null ? null : BINDINGS.get(modId);
    }
}
