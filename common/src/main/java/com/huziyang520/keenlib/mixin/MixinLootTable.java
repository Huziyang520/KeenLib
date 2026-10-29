package com.huziyang520.keenlib.mixin;

import com.huziyang520.keenlib.loot.KeenLoot;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

/**
 * Keys injected loot by the table's {@code random_sequence}, which vanilla structure tables always set to their own id.
 */
@Mixin(LootTable.class)
public abstract class MixinLootTable {

    @Shadow
    private Optional<Identifier> randomSequence;

    @Inject(
            method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;",
            at = @At("RETURN"))
    private void keenlib$addExtraLoot(LootContext context, CallbackInfoReturnable<List<ItemStack>> cir) {
        Identifier id = randomSequence.orElse(null);
        if (id == null) {
            return;
        }
        RandomSource random = context.getRandom();
        KeenLoot.roll(id, random, cir.getReturnValue()::add);
    }
}
