package com.huziyang520.keenlib.client.cloth;

import com.huziyang520.keenlib.config.KeenBusinessConfig;
import com.huziyang520.keenlib.config.KeenConfigApi;
import com.huziyang520.keenlib.config.KeenConfigEntry;
import com.huziyang520.keenlib.notice.KeenNoticeApi;
import com.huziyang520.keenlib.notice.KeenNoticePreferences;
import com.huziyang520.keenlib.platform.Services;
import com.huziyang520.keenlib.text.KeenText;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * KeenLib 的配置界面——**本类是 KeenLib 中唯一引用 Cloth Config 的地方**，只在客户端且
 * 已安装 Cloth Config 时被加载（由 {@link com.huziyang520.keenlib.client.KeenConfigScreenHook} 守卫）。
 *
 * <p>布局：左侧是分类（Cloth Config 在多分类时自动显示侧栏），右侧是该分类下的条目。
 * <ul>
 *   <li><b>业务模组通知设置</b>：每个注册了进入世界提示的业务模组一个二级菜单，内含开关；</li>
 *   <li><b>纯客户端业务模组启用设置</b>：每个业务模组一个二级菜单，内含它自己登记的配置项。</li>
 * </ul>
 * 两侧都没有内容时各显示一行提示。所有文案走语言键（见 {@code assets/keenlib/lang/}）。
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
        // 用「左侧分类树」布局，而不是把分类画成顶部的标签按钮
        builder.setGlobalized(true);
        builder.setGlobalizedExpanded(true);
        ConfigEntryBuilder entries = builder.entryBuilder();

        buildNoticeCategory(builder, entries);
        buildBusinessCategory(builder, entries);

        // 各项已经在写值时立即落盘；这里再存一次，兜住「重置」等不触发写回调的路径。
        builder.setSavingRunnable(() -> {
            for (KeenBusinessConfig business : KeenConfigApi.businesses()) {
                business.submit();
            }
        });
        return builder.build();
    }

    /** 左侧分类：业务模组通知设置。 */
    private static void buildNoticeCategory(ConfigBuilder builder, ConfigEntryBuilder entries) {
        ConfigCategory category = builder.getOrCreateCategory(
                KeenText.trans("gui.keenlib.category.notices", "Business mod notice settings"));

        List<String> owners = KeenNoticeApi.owners();
        if (owners.isEmpty()) {
            category.addEntry(entries.startTextDescription(
                    KeenText.trans("gui.keenlib.notices.none",
                            "No business mod has registered a join notice.")).build());
            return;
        }

        for (String owner : owners) {
            SubCategoryBuilder sub = entries.startSubCategory(modName(owner));
            var toggle = entries.startBooleanToggle(
                            KeenText.trans("gui.keenlib.notices.enable", "Enable join notices"),
                            KeenNoticePreferences.isEnabled(owner, true))
                    .setDefaultValue(true)
                    .setTooltip(KeenText.trans("gui.keenlib.notices.enable.tooltip",
                            "When off, this mod's messages are not shown when you enter a world."))
                    .setSaveConsumer(value -> KeenNoticePreferences.setEnabled(owner, value));
            sub.add(toggle.build());
            sub.setExpanded(true);
            category.addEntry(sub.build());
        }
    }

    /** 左侧分类：纯客户端业务模组启用设置。 */
    private static void buildBusinessCategory(ConfigBuilder builder, ConfigEntryBuilder entries) {
        ConfigCategory category = builder.getOrCreateCategory(
                KeenText.trans("gui.keenlib.category.business", "Client-side business mod settings"));

        if (!KeenConfigApi.hasBusinesses()) {
            category.addEntry(entries.startTextDescription(
                    KeenText.trans("gui.keenlib.business.none",
                            "No client-side mod depends on KeenLib.")).build());
            return;
        }

        for (KeenBusinessConfig business : KeenConfigApi.businesses()) {
            SubCategoryBuilder sub = entries.startSubCategory(business.displayName());
            for (KeenConfigEntry<?> entry : business.entries()) {
                sub.add(buildEntry(entries, entry));
            }
            sub.setExpanded(true);
            category.addEntry(sub.build());
        }
    }

    /** 业务模组显示名：优先用它在 KeenLib 里登记的（可本地化）名字，否则退回模组元数据名。 */
    private static Component modName(String modId) {
        KeenBusinessConfig registered = KeenConfigApi.find(modId);
        if (registered != null) {
            return registered.displayName();
        }
        return Component.literal(Services.PLATFORM.getModName(modId));
    }

    private static AbstractConfigListEntry buildEntry(ConfigEntryBuilder entries, KeenConfigEntry<?> entry) {
        switch (entry.kind()) {
            case BOOLEAN: {
                KeenConfigEntry<Boolean> typed = (KeenConfigEntry<Boolean>) entry;
                // 初始值必须是「当前配置文件里的值」，用 defaultValue() 会让界面永远显示默认值
                var toggle = entries.startBooleanToggle(typed.label(), typed.get())
                        .setDefaultValue(typed.defaultValue())
                        .setSaveConsumer(typed::set);
                if (!typed.tooltip().isEmpty()) {
                    toggle.setTooltip(typed.tooltip().toArray(new Component[0]));
                }
                return toggle.build();
            }
            case INT: {
                KeenConfigEntry<Integer> typed = (KeenConfigEntry<Integer>) entry;
                var slider = entries.startIntSlider(typed.label(), typed.get(), typed.min(), typed.max())
                        .setDefaultValue(typed.defaultValue())
                        .setSaveConsumer(typed::set);
                if (!typed.tooltip().isEmpty()) {
                    slider.setTooltip(typed.tooltip().toArray(new Component[0]));
                }
                return slider.build();
            }
            default: {
                KeenConfigEntry<String> typed = (KeenConfigEntry<String>) entry;
                var field = entries.startStrField(typed.label(), typed.get())
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
