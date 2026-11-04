package com.huziyang520.keenlib.registry;

import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

/**
 * Fills a creative tab; loader-specific output types are hidden behind this interface.
 */
@FunctionalInterface
public interface KeenTabEntries {

    void addTo(Consumer<ItemStack> output);
}
