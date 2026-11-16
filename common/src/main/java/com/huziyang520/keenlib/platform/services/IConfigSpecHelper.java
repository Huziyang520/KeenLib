package com.huziyang520.keenlib.platform.services;

import com.huziyang520.keenlib.config.KeenBusinessConfig;

/**
 * Builds a loader config spec for one business config and registers it under KeenLib's own container,
 * so the loader's native config screen lists every business mod as one file.
 */
public interface IConfigSpecHelper {

    /**
     * @param config   the business config whose entries become spec values
     * @param fileName file name to register, {@code <modId>-client.toml}
     * @return the binding for those values, or {@code null} when this loader has no spec backend available
     */
    SpecBinding register(KeenBusinessConfig config, String fileName);
}
