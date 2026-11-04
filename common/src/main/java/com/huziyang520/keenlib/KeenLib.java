package com.huziyang520.keenlib;

import com.huziyang520.keenlib.platform.Services;

/**
 * KeenLib 公共引导入口。各加载器入口类（fabric/neoforge 模块）在客户端初始化阶段调用 {@link #init()}。
 */
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
