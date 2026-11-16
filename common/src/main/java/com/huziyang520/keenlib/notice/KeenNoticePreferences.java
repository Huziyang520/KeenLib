package com.huziyang520.keenlib.notice;

import com.huziyang520.keenlib.config.KeenConfig;

/**
 * Per-mod switch for join notices.
 *
 * <p>The value lives in that mod's own config, so it is edited on its own screen and never leaves the client.
 */
public final class KeenNoticePreferences {

    /** Key of the switch inside the owning mod's config. */
    public static final String KEY = "notices_enabled";

    private KeenNoticePreferences() {
    }

    public static boolean isEnabled(String owner, boolean fallback) {
        return KeenConfig.create(owner).getBoolean(KEY, fallback);
    }

    public static void setEnabled(String owner, boolean enabled) {
        KeenConfig file = KeenConfig.create(owner);
        file.set(KEY, enabled);
        file.save();
    }
}
