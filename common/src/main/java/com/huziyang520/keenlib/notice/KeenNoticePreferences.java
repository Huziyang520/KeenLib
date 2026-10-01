package com.huziyang520.keenlib.notice;

import com.huziyang520.keenlib.config.KeenConfig;

/**
 * 每个业务模组的「进入世界提示」开关，存放在 KeenLib 自己的 {@code config/keenlib.json} 中（键 {@code notices.<modId>}）。
 *
 * <p>未设置时使用该提示注册时声明的默认值，因此配置文件里只会出现被手动改过的模组。
 */
public final class KeenNoticePreferences {

    private static final String PREFIX = "notices.";

    private static final KeenConfig FILE = KeenConfig.create("keenlib");

    private KeenNoticePreferences() {
    }

    /** 某模组的提示是否启用。 */
    public static boolean isEnabled(String owner, boolean fallback) {
        return FILE.getBoolean(PREFIX + owner, fallback);
    }

    /** 设置某模组的提示开关并立即落盘。 */
    public static void setEnabled(String owner, boolean enabled) {
        FILE.set(PREFIX + owner, enabled);
        FILE.save();
    }
}
