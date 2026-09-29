package com.huziyang520.keenlib.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {

    /**
     * 当前加载器名称（"Fabric" / "NeoForge"）。
     */
    String getPlatformName();

    /**
     * 指定 modId 的模组是否已加载。
     */
    boolean isModLoaded(String modId);

    /**
     * 是否处于开发环境。
     */
    boolean isDevelopmentEnvironment();

    /**
     * 配置文件目录（对应实例的 config 目录）。
     */
    Path getConfigDir();

    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
