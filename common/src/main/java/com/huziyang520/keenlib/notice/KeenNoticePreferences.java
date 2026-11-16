package com.huziyang520.keenlib.notice;

import com.huziyang520.keenlib.config.KeenConfig;

/** Per-mod switch for join notices, stored in {@code config/keenlib.json} under {@code notices.<modId>}. */
public final class KeenNoticePreferences {

    /** Prefix of the switch inside KeenLib's own config file. */
    public static final String PREFIX = "notices.";

    private static final KeenConfig FILE = KeenConfig.create("keenlib");

    private KeenNoticePreferences() {
    }

    public static boolean isEnabled(String owner, boolean fallback) {
        return FILE.getBoolean(PREFIX + owner, fallback);
    }

    public static void setEnabled(String owner, boolean enabled) {
        FILE.set(PREFIX + owner, enabled);
        FILE.save();
    }
}
