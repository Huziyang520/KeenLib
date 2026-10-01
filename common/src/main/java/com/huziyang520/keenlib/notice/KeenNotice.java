package com.huziyang520.keenlib.notice;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

/**
 * 一条进入世界的聊天框提示。
 *
 * @param id               提示 id；其 namespace 即所属模组
 * @param mode             发送时机
 * @param lines            要发送的内容行
 * @param enabledByDefault 默认是否发送
 */
public record KeenNotice(Identifier id, KeenNoticeMode mode, List<Component> lines, boolean enabledByDefault) {

    public KeenNotice {
        lines = List.copyOf(lines);
    }

    /** @return 所属模组 id（取自 id 的 namespace） */
    public String owner() {
        return id.getNamespace();
    }
}
