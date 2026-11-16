package com.huziyang520.keenlib.client;

import com.huziyang520.keenlib.config.KeenBusinessConfig;
import com.huziyang520.keenlib.config.KeenConfig;
import com.huziyang520.keenlib.config.KeenConfigApi;
import com.huziyang520.keenlib.config.KeenConfigEntry;
import com.huziyang520.keenlib.notice.KeenNoticeApi;
import com.huziyang520.keenlib.notice.KeenNoticePreferences;
import com.huziyang520.keenlib.text.KeenText;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

import java.util.ArrayList;
import java.util.List;

/** KeenLib's own configuration screen: the loader's native one plus rows that no spec owns. */
public final class KeenAggregateScreen extends ConfigurationScreen.ConfigurationSectionScreen {

    public KeenAggregateScreen(Screen parent, ModConfig config) {
        super(parent, ModConfig.Type.CLIENT, config, KeenText.trans("gui.keenlib.title", "KeenLib"));
    }

    @Override
    protected List<Element> createSyntheticValues() {
        List<Element> rows = new ArrayList<>();
        for (String owner : KeenNoticeApi.owners()) {
            rows.add(new Element(
                    prefix(modName(owner), KeenText.trans("gui.keenlib.entry.notice", "Join notices")),
                    KeenText.trans("gui.keenlib.entry.notice.tip", "Show this mod's message when joining a world"),
                    noticeToggle(owner), null, false));
        }
        for (KeenBusinessConfig business : KeenConfigApi.businesses()) {
            for (KeenConfigEntry<?> entry : business.entries()) {
                rows.add(new Element(prefix(business.displayName(), labelOf(entry)), tooltipOf(entry),
                        widget(business, entry), null, false));
            }
        }
        return rows;
    }

    private static Component modName(String modId) {
        KeenBusinessConfig config = KeenConfigApi.find(modId);
        return config == null ? KeenText.literal(modId) : config.displayName();
    }

    private static Component labelOf(KeenConfigEntry<?> entry) {
        return entry.label() == null ? KeenText.literal(entry.key()) : entry.label();
    }

    private static Component prefix(Component owner, Component label) {
        MutableComponent line = owner.copy();
        line.append(KeenText.literal(" · "));
        line.append(label);
        return line;
    }

    private static Component tooltipOf(KeenConfigEntry<?> entry) {
        List<Component> lines = entry.tooltip();
        if (lines == null || lines.isEmpty()) {
            return Component.empty();
        }
        MutableComponent joined = Component.empty();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) {
                joined.append(Component.literal(String.valueOf((char) 10)));
            }
            joined.append(lines.get(i));
        }
        return joined;
    }

    private AbstractWidget noticeToggle(String owner) {
        return onOff(KeenNoticePreferences.isEnabled(owner, true),
                value -> KeenNoticePreferences.setEnabled(owner, value));
    }

    private static AbstractWidget onOff(boolean initial, java.util.function.Consumer<Boolean> sink) {
        boolean[] state = {initial};
        return Button.builder(labelFor(state[0]), button -> {
            state[0] = !state[0];
            button.setMessage(labelFor(state[0]));
            sink.accept(state[0]);
        }).bounds(0, 0, 150, 20).build();
    }

    private static Component labelFor(boolean value) {
        return KeenText.trans(value ? "gui.keenlib.on" : "gui.keenlib.off", value ? "On" : "Off");
    }

    @SuppressWarnings("unchecked")
    private AbstractWidget widget(KeenBusinessConfig business, KeenConfigEntry<?> entry) {
        AbstractWidget widget;
        if (entry.kind() == KeenConfigEntry.Kind.BOOLEAN) {
            KeenConfigEntry<Boolean> typed = (KeenConfigEntry<Boolean>) entry;
            widget = onOff(typed.get(), value -> {
                typed.set(value);
                business.file().save();
            });
        } else {
            KeenConfigEntry<Object> typed = (KeenConfigEntry<Object>) entry;
            EditBox box = new EditBox(this.font, 0, 0, 150, 20, Component.empty());
            box.setValue(String.valueOf(typed.get()));
            box.setResponder(text -> {
                try {
                    switch (entry.kind()) {
                        case INT -> typed.set(Integer.parseInt(text.trim()));
                        case DOUBLE -> typed.set(Double.parseDouble(text.trim()));
                        default -> typed.set(text);
                    }
                    business.file().save();
                } catch (RuntimeException ignored) {
                }
            });
            widget = box;
        }
        widget.active = entry.enabled();
        return widget;
    }
}
