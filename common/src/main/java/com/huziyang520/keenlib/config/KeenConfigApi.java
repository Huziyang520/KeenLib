package com.huziyang520.keenlib.config;

import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置界面注册入口，供业务模组调用。
 *
 * <p><b>本类及其返回值不引用 Cloth Config 的任何类型</b>：业务模组即使不安装 Cloth Config 也能安全调用，
 * 界面渲染由 KeenLib 客户端侧在检测到 Cloth Config 后才进行（未安装则没有配置界面，功能不受影响）。
 */
public final class KeenConfigApi {

    private static final Map<String, KeenBusinessConfig> BUSINESS = new LinkedHashMap<>();

    private KeenConfigApi() {
    }

    /** 取得（必要时创建）某业务模组的配置组，显示名用纯文本。 */
    public static KeenBusinessConfig business(String modId, String displayName) {
        return business(modId, Component.literal(displayName));
    }

    /** 取得（必要时创建）某业务模组的配置组。 */
    public static KeenBusinessConfig business(String modId, Component displayName) {
        return BUSINESS.computeIfAbsent(modId, id -> new KeenBusinessConfig(id, displayName));
    }

    /** 已注册的业务模组配置组，按注册顺序。 */
    public static Collection<KeenBusinessConfig> businesses() {
        return BUSINESS.values();
    }

    /** 是否已有业务模组注册（没有时配置界面显示提示）。 */
    public static boolean hasBusinesses() {
        return !BUSINESS.isEmpty();
    }
}
