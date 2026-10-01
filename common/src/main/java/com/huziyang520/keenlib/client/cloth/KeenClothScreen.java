package com.huziyang520.keenlib.client.cloth;

import com.huziyang520.keenlib.config.KeenBusinessConfig;
import com.huziyang520.keenlib.config.KeenConfigApi;
import com.huziyang520.keenlib.config.KeenConfigEntry;
import com.huziyang520.keenlib.text.KeenText;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * KeenLib 的配置界面——**本类是 KeenLib 中唯一引用 Cloth Config 的地方**，只在客户端且
 * 已安装 Cloth Config 时被加载（由 {@link com.huziyang520.keenlib.client.KeenConfigScreenHook} 守卫）。
 *
 * <p>界面结构：一个分类「纯客户端业务模组」，其中每个业务模组是一个二级菜单（子分类）；
 * 若没有任何业务模组注册，则显示一行提示。
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class KeenClothScreen {

    private KeenClothScreen() {
    }

    /** 构建配置界面。 */
    public static Screen build(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(KeenText.trans("gui.keenlib.title", "KeenLib"));
        ConfigEntryBuilder entries = builder.entryBuilder();

        ConfigCategory category = builder.getOrCreateCategory(
                KeenText.trans("gui.keenlib.category.business", "纯客户端业务模组"));

        if (!KeenConfigApi.hasBusinesses()) {
            category.addEntry(entries.startTextDescription(
                    KeenText.trans("gui.keenlib.business.none", "没有依赖 KeenLib 的纯客户端模组")).build());
        } else {
            for (KeenBusinessConfig business : KeenConfigApi.businesses()) {
                SubCategoryBuilder sub = entries.startSubCategory(business.displayName());
                for (KeenConfigEntry<?> entry : business.entries()) {
                    sub.add(buildEntry(entries, entry));
                }
                sub.setExpanded(false);
                category.addEntry(sub.build());
            }
        }

        builder.setSavingRunnable(() -> {
            for (KeenBusinessConfig business : KeenConfigApi.businesses()) {
                business.file().save();
            }
        });
        return builder.build();
    }

    private static AbstractConfigListEntry buildEntry(ConfigEntryBuilder entries, KeenConfigEntry<?> entry) {
        switch (entry.kind()) {
            case BOOLEAN: {
                KeenConfigEntry<Boolean> typed = (KeenConfigEntry<Boolean>) entry;
                var toggle = entries.startBooleanToggle(typed.label(), typed.defaultValue())
                        .setDefaultValue(typed.defaultValue())
                        .setSaveConsumer(typed::set);
                if (!typed.tooltip().isEmpty()) {
                    toggle.setTooltip(typed.tooltip().toArray(new Component[0]));
                }
                return toggle.build();
            }
            case INT: {
                KeenConfigEntry<Integer> typed = (KeenConfigEntry<Integer>) entry;
                var slider = entries.startIntSlider(typed.label(), typed.defaultValue(), typed.min(), typed.max())
                        .setDefaultValue(typed.defaultValue())
                        .setSaveConsumer(typed::set);
                if (!typed.tooltip().isEmpty()) {
                    slider.setTooltip(typed.tooltip().toArray(new Component[0]));
                }
                return slider.build();
            }
            default: {
                KeenConfigEntry<String> typed = (KeenConfigEntry<String>) entry;
                var field = entries.startStrField(typed.label(), typed.defaultValue())
                        .setDefaultValue(typed.defaultValue())
                        .setSaveConsumer(typed::set);
                if (!typed.tooltip().isEmpty()) {
                    field.setTooltip(typed.tooltip().toArray(new Component[0]));
                }
                return field.build();
            }
        }
    }
}
