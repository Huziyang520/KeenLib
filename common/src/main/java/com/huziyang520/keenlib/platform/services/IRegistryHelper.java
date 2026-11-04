package com.huziyang520.keenlib.platform.services;

import com.huziyang520.keenlib.registry.KeenTabEntries;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
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


    Holder<CreativeModeTab> tab(String modId, String path, Component title, Supplier<ItemStack> icon,
                                KeenTabEntries entries);
}
