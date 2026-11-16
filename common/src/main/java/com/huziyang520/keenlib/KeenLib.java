package com.huziyang520.keenlib;

import com.huziyang520.keenlib.platform.Services;

public final class KeenLib {

    private static boolean initialized = false;

    private KeenLib() {
    }

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        Constants.LOG.info("KeenLib initialized on {} ({})", Services.PLATFORM.getPlatformName(),
                Services.PLATFORM.getEnvironmentName());
    }
}
