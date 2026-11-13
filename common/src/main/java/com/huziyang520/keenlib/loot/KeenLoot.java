package com.huziyang520.keenlib.loot;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
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

    /** {@code chance} is a percentage; 100 means the entry is always picked when its table is rolled. */
    public record Entry(Holder<Item> item, int weight, int min, int max, float chance) {
    }

    private static final Map<ResourceLocation, List<Entry>> ENTRIES = new ConcurrentHashMap<>();

    private KeenLoot() {
    }

    public static void add(ResourceKey<LootTable> table, Holder<Item> item, int weight, int min, int max) {
        add(table.location(), item, weight, min, max);
    }

    /** Weighted entry: one weight-bearing entry per roll, so a lone entry on a table always drops. */
    public static void add(ResourceLocation table, Holder<Item> item, int weight, int min, int max) {
        register(table, item, Math.max(1, weight), min, max, 100.0F);
    }

    public static void addChance(ResourceKey<LootTable> table, Holder<Item> item, float chance, int min, int max) {
        addChance(table.location(), item, chance, min, max);
    }

    /** Adds an entry that shows up {@code chance} percent of the time; several chance entries roll independently. */
    public static void addChance(ResourceLocation table, Holder<Item> item, float chance, int min, int max) {
        register(table, item, 1, min, max, Math.max(0.0F, Math.min(100.0F, chance)));
    }

    public static List<Entry> entries(ResourceLocation table) {
        return table == null ? List.of() : ENTRIES.getOrDefault(table, List.of());
    }

    public static void roll(ResourceLocation table, RandomSource random, Consumer<ItemStack> sink) {
        List<Entry> pool = entries(table);
        if (pool.isEmpty()) {
            return;
        }
        int total = 0;
        for (Entry entry : pool) {
            if (entry.chance() >= 100.0F) {
                total += entry.weight();
            }
        }
        Entry picked = null;
        if (total > 0) {
            int pick = random.nextInt(total);
            for (Entry entry : pool) {
                if (entry.chance() < 100.0F) {
                    continue;
                }
                pick -= entry.weight();
                if (pick < 0) {
                    picked = entry;
                    break;
                }
            }
        }
        if (picked != null) {
            sink.accept(stack(picked, random));
        }
        for (Entry entry : pool) {
            if (entry.chance() < 100.0F && random.nextFloat() * 100.0F < entry.chance()) {
                sink.accept(stack(entry, random));
            }
        }
    }

    private static void register(ResourceLocation table, Holder<Item> item, int weight, int min, int max, float chance) {
        int bottom = Math.max(1, min);
        Entry entry = new Entry(item, weight, bottom, Math.max(bottom, max), chance);
        ENTRIES.computeIfAbsent(table, id -> new CopyOnWriteArrayList<>()).add(entry);
    }

    private static ItemStack stack(Entry entry, RandomSource random) {
        int count = entry.min() + random.nextInt(entry.max() - entry.min() + 1);
        return new ItemStack(entry.item(), count);
    }
}
