package com.huziyang520.keenlib.notice;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public final class KeenNoticeApi {

    private static final List<KeenNotice> NOTICES = new ArrayList<>();

    private KeenNoticeApi() {
    }
    public static void register(Identifier id, KeenNoticeMode mode, Component line) {
        register(id, mode, List.of(line), true);
    }
    public static void register(Identifier id, KeenNoticeMode mode, Component line, boolean enabledByDefault) {
        register(id, mode, List.of(line), enabledByDefault);
    }
    public static void register(Identifier id, KeenNoticeMode mode, List<Component> lines) {
        register(id, mode, lines, true);
    }
    public static void register(Identifier id, KeenNoticeMode mode, List<Component> lines, boolean enabledByDefault) {
        NOTICES.add(new KeenNotice(id, mode, lines, enabledByDefault));
    }
    public static List<KeenNotice> notices() {
        return Collections.unmodifiableList(NOTICES);
    }
    public static List<String> owners() {
        List<String> owners = new ArrayList<>();
        for (KeenNotice notice : NOTICES) {
            if (!owners.contains(notice.owner())) {
                owners.add(notice.owner());
            }
        }
        return Collections.unmodifiableList(owners);
    }
}
