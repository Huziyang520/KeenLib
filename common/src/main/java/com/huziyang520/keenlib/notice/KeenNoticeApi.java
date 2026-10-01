package com.huziyang520.keenlib.notice;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 进入世界的聊天框提示注册入口，供业务模组调用。
 *
 * <p>业务模组这样使用（纯客户端模组同样可用，提示在客户端本地发送）：
 * <pre>{@code
 * KeenNoticeApi.register(
 *         Identifier.fromNamespaceAndPath("mymod", "welcome"),
 *         KeenNoticeMode.ONCE_PER_WORLD,
 *         Component.literal("My Mod is active"));
 * }</pre>
 *
 * <p>注册时机：所属模组的客户端初始化阶段（早于进入世界即可）。
 */
public final class KeenNoticeApi {

    private static final List<KeenNotice> NOTICES = new ArrayList<>();

    private KeenNoticeApi() {
    }

    /** 注册一条单行提示，默认发送。 */
    public static void register(Identifier id, KeenNoticeMode mode, Component line) {
        register(id, mode, List.of(line), true);
    }

    /** 注册一条单行提示。 */
    public static void register(Identifier id, KeenNoticeMode mode, Component line, boolean enabledByDefault) {
        register(id, mode, List.of(line), enabledByDefault);
    }

    /** 注册一条多行提示，默认发送。 */
    public static void register(Identifier id, KeenNoticeMode mode, List<Component> lines) {
        register(id, mode, lines, true);
    }

    /** 注册一条多行提示。 */
    public static void register(Identifier id, KeenNoticeMode mode, List<Component> lines, boolean enabledByDefault) {
        NOTICES.add(new KeenNotice(id, mode, lines, enabledByDefault));
    }

    /** 已注册的提示，按注册顺序。 */
    public static List<KeenNotice> notices() {
        return Collections.unmodifiableList(NOTICES);
    }
}
