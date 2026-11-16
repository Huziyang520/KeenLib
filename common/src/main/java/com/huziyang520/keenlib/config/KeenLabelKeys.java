package com.huziyang520.keenlib.config;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.ArrayList;
import java.util.List;

/** Extracts the keys the native config screen needs from components the business mod already passes in. */
public final class KeenLabelKeys {

    private KeenLabelKeys() {
    }

    /** @return the translation key of a translatable component, or {@code null} for literal text */
    public static String keyOf(Component component) {
        if (component != null && component.getContents() instanceof TranslatableContents contents) {
            return contents.getKey();
        }
        return null;
    }

    /** Tooltips are plain text in a spec, so the current translation is baked in at registration time. */
    public static String commentOf(List<Component> tooltip) {
        List<String> lines = new ArrayList<>();
        for (Component line : tooltip) {
            String text = line.getString();
            if (!text.isBlank()) {
                lines.add(text);
            }
        }
        return lines.isEmpty() ? null : String.join("\n", lines);
    }
}
