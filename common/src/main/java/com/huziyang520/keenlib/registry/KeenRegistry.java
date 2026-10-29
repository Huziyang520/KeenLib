package com.huziyang520.keenlib.registry;

import com.huziyang520.keenlib.platform.Services;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Loader-neutral registration entry point; obtain one instance per mod id.
 */
public final class KeenRegistry {

    private final String modId;

    private KeenRegistry(String modId) {
        this.modId = modId;
    }

    public static KeenRegistry of(String modId) {
        return new KeenRegistry(modId);
    }

    /**
     * Wires this mod id to the current loader context; Fabric ignores the bus.
     */
    public void bind(Object eventBus) {
        Services.REGISTRY.bind(modId, eventBus);
    }

    public String modId() {
        return modId;
    }

    public Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(modId, path);
    }

    public <T extends Item> Holder<Item> item(String path, Function<Item.Properties, T> factory) {
        return Services.REGISTRY.item(modId, path, factory);
    }

    public Holder<Item> item(String path) {
        return item(path, Item::new);
    }

    public Holder<SoundEvent> sound(String path) {
        return Services.REGISTRY.sound(modId, path);
    }

    public <T> Holder<DataComponentType<T>> component(String path, Codec<T> codec,
                                                      StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return Services.REGISTRY.component(modId, path, codec, streamCodec);
    }

    public Holder<CreativeModeTab> tab(String path, Component title, Supplier<ItemStack> icon,
                                       KeenTabEntries entries) {
        return Services.REGISTRY.tab(modId, path, title, icon, entries);
    }
}
