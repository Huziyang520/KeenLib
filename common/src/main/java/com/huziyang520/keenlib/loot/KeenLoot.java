package com.huziyang520.keenlib.loot;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class KeenLoot {

    public record Entry(Holder<Item> item, int weight, int min, int max) {
    }

    private static final Map<Identifier, List<Entry>> ENTRIES = new ConcurrentHashMap<>();

    private KeenLoot() {
    }

    public static void add(ResourceKey<LootTable> table, Holder<Item> item, int weight, int min, int max) {
        add(table.identifier(), item, weight, min, max);
    }

    public static void add(Identifier table, Holder<Item> item, int weight, int min, int max) {
        int bottom = Math.max(1, min);
        Entry entry = new Entry(item, Math.max(1, weight), bottom, Math.max(bottom, max));
        ENTRIES.computeIfAbsent(table, id -> new CopyOnWriteArrayList<>()).add(entry);
    }

    public static List<Entry> entries(Identifier table) {
        return table == null ? List.of() : ENTRIES.getOrDefault(table, List.of());
    }

    public static void roll(Identifier table, RandomSource random, Consumer<ItemStack> sink) {
        List<Entry> pool = entries(table);
        if (pool.isEmpty()) {
            return;
        }
        int total = 0;
        for (Entry entry : pool) {
            total += entry.weight();
        }
        int pick = random.nextInt(total);
        for (Entry entry : pool) {
            pick -= entry.weight();
            if (pick < 0) {
                int count = entry.min() + random.nextInt(entry.max() - entry.min() + 1);
                sink.accept(new ItemStack(entry.item(), count));
                return;
            }
        }
    }
}
