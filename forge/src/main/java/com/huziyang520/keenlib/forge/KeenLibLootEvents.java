package com.huziyang520.keenlib.forge;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.loot.KeenLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class KeenLibLootEvents {

    private KeenLibLootEvents() {
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();
        for (KeenLoot.Entry entry : KeenLoot.entries(name)) {
            LootItem.Builder<?> item = LootItem.lootTableItem(entry.item().value());
            item.apply(SetItemCountFunction.setCount(
                    UniformGenerator.between(entry.min(), entry.max())));
            item.setWeight(entry.weight());
            if (entry.chance() < 100.0F) {
                item.when(LootItemRandomChanceCondition.randomChance(entry.chance() / 100.0F));
            }
            event.getTable().addPool(LootPool.lootPool().add(item).build());
            Constants.LOG.debug("KeenLib added a loot pool to {}", name);
        }
    }
}
