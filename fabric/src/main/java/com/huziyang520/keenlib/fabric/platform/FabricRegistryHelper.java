package com.huziyang520.keenlib.fabric.platform;

import com.huziyang520.keenlib.platform.services.IRegistryHelper;
import com.huziyang520.keenlib.registry.KeenTabEntries;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Fabric registrations go straight into the built-in registries.
 */
public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public void bind(String modId, Object eventBus) {
        // Fabric needs no per-mod registration context.
    }

    @Override
    public <T extends Item> Holder<Item> item(String modId, String path, Function<Item.Properties, T> factory) {
        Identifier id = Identifier.fromNamespaceAndPath(modId, path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        Item item = Registry.register(BuiltInRegistries.ITEM, key, factory.apply(new Item.Properties().setId(key)));
        return BuiltInRegistries.ITEM.wrapAsHolder(item);
    }

    @Override
    public Holder<SoundEvent> sound(String modId, String path) {
        Identifier id = Identifier.fromNamespaceAndPath(modId, path);
        SoundEvent event = Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
        return BuiltInRegistries.SOUND_EVENT.wrapAsHolder(event);
    }

    @Override
    public <T> Holder<DataComponentType<T>> component(String modId, String path, Codec<T> codec,
                                                      StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        Identifier id = Identifier.fromNamespaceAndPath(modId, path);
        DataComponentType<T> type = DataComponentType.<T>builder()
                .persistent(codec)
                .networkSynchronized(streamCodec)
                .build();
        DataComponentType<?> registered = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, type);
        return wrapComponent(registered);
    }

    @Override
    public Holder<CreativeModeTab> tab(String modId, String path, Component title, Supplier<ItemStack> icon,
                                       KeenTabEntries entries) {
        Identifier id = Identifier.fromNamespaceAndPath(modId, path);
        CreativeModeTab tab = tabBuilder()
                .title(title)
                .icon(icon)
                .displayItems((parameters, output) -> entries.addTo(output::accept))
                .build();
        CreativeModeTab registered = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id, tab);
        return BuiltInRegistries.CREATIVE_MODE_TAB.wrapAsHolder(registered);
    }

    /**
     * Fabric API places custom tabs after the vanilla ones; without it fall back to the vanilla builder.
     */
    private static CreativeModeTab.Builder tabBuilder() {
        try {
            Class<?> factory = Class.forName("net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab");
            return (CreativeModeTab.Builder) factory.getMethod("builder").invoke(null);
        } catch (Throwable ignored) {
            return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Holder<DataComponentType<T>> wrapComponent(DataComponentType<?> type) {
        return (Holder<DataComponentType<T>>) (Holder<?>) BuiltInRegistries.DATA_COMPONENT_TYPE.wrapAsHolder(type);
    }
}
