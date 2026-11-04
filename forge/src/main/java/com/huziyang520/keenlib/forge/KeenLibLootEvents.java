package com.huziyang520.keenlib.forge;

import com.huziyang520.keenlib.Constants;
import com.huziyang520.keenlib.loot.KeenLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Forge 1.20.1 用 LootTableLoadEvent 挂额外掉落，避免依赖 Mixin 与 refmap。
 */
public final class KeenLibLootEvents {

    private KeenLibLootEvents() {
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();
        for (KeenLoot.Entry entry : KeenLoot.entries(name)) {
            LootPool pool = LootPool.lootPool()
                    .add(LootItem.lootTableItem(entry.item().value())
                            .apply(SetItemCountFunction.setCount(
                                    UniformGenerator.between(entry.min(), entry.max())))
                            .setWeight(entry.weight()))
                    .build();
            event.getTable().addPool(pool);
            Constants.LOG.debug("KeenLib added a loot pool to {}", name);
        }
    }
}
