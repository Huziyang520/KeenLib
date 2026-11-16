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
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * KeenLib's own configuration screen: the loader's native one, extended with rows that do not come from a spec.
 *
 * <p>Two groups: the join-notice switch of every registered mod, and the client options of every business mod.
 */
public final class KeenAggregateScreen extends ConfigurationScreen.ConfigurationSectionScreen {

    public KeenAggregateScreen(Screen parent, ModConfig config) {
        super(parent, ModConfig.Type.CLIENT, config, KeenText.trans("gui.keenlib.title", "KeenLib"));
    }

    @Override
    protected List<Element> createSyntheticValues() {
        List<Element> rows = new ArrayList<>();
        for (String owner : KeenNoticeApi.owners()) {
            rows.add(new Element(
                    KeenText.trans("gui.keenlib.notices.enable", "Enable join notices"),
                    KeenText.trans("gui.keenlib.notices.owner", "Applies to: %s", owner),
                    noticeToggle(owner), null, false));
        }
        for (KeenBusinessConfig business : KeenConfigApi.businesses()) {
            for (KeenConfigEntry<?> entry : business.entries()) {
                rows.add(new Element(entry.label(), null, widget(business, entry), null, false));
            }
        }
        return rows;
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
        if (entry.kind() == KeenConfigEntry.Kind.BOOLEAN) {
            KeenConfigEntry<Boolean> typed = (KeenConfigEntry<Boolean>) entry;
            return onOff(typed.get(), value -> {
                typed.set(value);
                business.file().save();
            });
        }
        KeenConfigEntry<Object> typed = (KeenConfigEntry<Object>) entry;
        EditBox box = new EditBox(this.font, 0, 0, 150, 20, Component.empty());
        box.setValue(String.valueOf(entry.get()));
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
        return box;
    }
}
