package com.huziyang520.keenlib.notice;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;
public record KeenNotice(Identifier id, KeenNoticeMode mode, List<Component> lines, boolean enabledByDefault) {

    public KeenNotice {
        lines = List.copyOf(lines);
    }
    public String owner() {
        return id.getNamespace();
    }
}
