package com.huziyang520.keenlib.platform.services;

import com.huziyang520.keenlib.registry.KeenTabEntries;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Registry abstraction so common code can register content without touching loader APIs.
 */
public interface IRegistryHelper {

    /**
     * Binds one mod id to its loader registration context; call once per mod before registering.
     */
    void bind(String modId, Object eventBus);

    <T extends Item> Holder<Item> item(String modId, String path, Function<Item.Properties, T> factory);

    Holder<SoundEvent> sound(String modId, String path);

    <T> Holder<DataComponentType<T>> component(String modId, String path, Codec<T> codec,
                                               StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec);

    Holder<CreativeModeTab> tab(String modId, String path, Component title, Supplier<ItemStack> icon,
                                KeenTabEntries entries);
}
