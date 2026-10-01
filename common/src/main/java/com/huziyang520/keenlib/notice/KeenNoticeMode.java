package com.huziyang520.keenlib.notice;

/**
 * 进入世界时的聊天框提示的发送时机。
 */
public enum KeenNoticeMode {

    /** 每次进入世界都发送。 */
    EVERY_JOIN,

    /** 每个世界（存档 / 服务器）只发送一次。 */
    ONCE_PER_WORLD
}
