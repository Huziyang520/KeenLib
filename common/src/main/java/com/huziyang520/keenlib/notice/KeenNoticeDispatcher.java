package com.huziyang520.keenlib.notice;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.config.KeenConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
public final class KeenNoticeDispatcher {

    private static final KeenConfig MEMORY = KeenConfig.create("keenlib/notices");

    private static String lastWorldKey;

    private KeenNoticeDispatcher() {
    }
    public static void clientTick(Minecraft minecraft) {
        ClientLevel level = minecraft.level;
        if (level == null || minecraft.player == null) {
            lastWorldKey = null;
            return;
        }
        String key = worldKey(minecraft);
        if (key.equals(lastWorldKey)) {
            return;
        }
        lastWorldKey = key;
        dispatch(minecraft, key);
    }
    private static String worldKey(Minecraft minecraft) {
        if (minecraft.getCurrentServer() != null) {
            return "mp:" + minecraft.getCurrentServer().ip;
        }
        if (minecraft.getSingleplayerServer() != null) {
            return "sp:" + minecraft.getSingleplayerServer().getWorldData().getLevelName();
        }
        return "local";
    }

    private static void dispatch(Minecraft minecraft, String worldKey) {
        if (KeenNoticeApi.notices().isEmpty()) {
            return;
        }
        String memoryKey = "seen." + sanitize(worldKey);
        String seen = MEMORY.getString(memoryKey, "");
        StringBuilder updated = new StringBuilder(seen);
        int sent = 0;
        for (KeenNotice notice : KeenNoticeApi.notices()) {
            if (!KeenNoticePreferences.isEnabled(notice.owner(), notice.enabledByDefault())) {
                continue;
            }
            boolean once = notice.mode() == KeenNoticeMode.ONCE_PER_WORLD;
            if (once && seen.contains(notice.id() + ";")) {
                continue;
            }
            for (Component line : notice.lines()) {
                minecraft.player.sendSystemMessage(line);
            }
            if (once) {
                updated.append(notice.id()).append(';');
            }
            sent++;
        }
        if (sent > 0) {
            Constants.LOG.debug("KeenLib dispatched {} join notice(s) for world {}", sent, worldKey);
        }
        if (!updated.toString().equals(seen)) {
            MEMORY.set(memoryKey, updated.toString());
            MEMORY.save();
        }
    }

    private static String sanitize(String value) {
        return value.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}
