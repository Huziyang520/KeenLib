package com.huziyang520.keenlib.neoforge.platform;

import com.huziyang520.keenlib.platform.services.IRegistryHelper;
import com.huziyang520.keenlib.registry.KeenTabEntries;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * NeoForge needs one deferred register set per mod id, attached to that mod's event bus.
 */
public class NeoForgeRegistryHelper implements IRegistryHelper {

    private final Map<String, ModRegisters> registers = new HashMap<>();

    @Override
    public void bind(String modId, Object eventBus) {
        if (!(eventBus instanceof IEventBus bus)) {
            throw new IllegalArgumentException("NeoForge requires the mod event bus when binding " + modId);
        }
        DeferredRegister.Items items = DeferredRegister.createItems(modId);
        DeferredRegister<SoundEvent> sounds = DeferredRegister.create(Registries.SOUND_EVENT, modId);
        DeferredRegister.DataComponents components = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, modId);
        DeferredRegister<CreativeModeTab> tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
        items.register(bus);
        sounds.register(bus);
        components.register(bus);
        tabs.register(bus);
        registers.put(modId, new ModRegisters(items, sounds, components, tabs));
    }

    @Override
    public <T extends Item> Holder<Item> item(String modId, String path, Function<Item.Properties, T> factory) {
        return registers(modId).items.registerItem(path, factory);
    }

    @Override
    public Holder<SoundEvent> sound(String modId, String path) {
        return registers(modId).sounds.register(path, () -> SoundEvent.createVariableRangeEvent(
                com.huziyang520.keenlib.registry.KeenRegistry.of(modId).id(path)));
    }

    @Override
    public <T> Holder<DataComponentType<T>> component(String modId, String path, Codec<T> codec,
                                                      StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return cast(registers(modId).components.<T>registerComponentType(path,
                builder -> builder.persistent(codec).networkSynchronized(streamCodec)));
    }

    @Override
    public Holder<CreativeModeTab> tab(String modId, String path, Component title, Supplier<ItemStack> icon,
                                       KeenTabEntries entries) {
        return registers(modId).tabs.register(path, () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(title)
                .icon(icon)
                .displayItems((parameters, output) -> entries.addTo(output::accept))
                .build());
    }

    private ModRegisters registers(String modId) {
        ModRegisters found = registers.get(modId);
        if (found == null) {
            throw new IllegalStateException("Registry helper is not bound for " + modId);
        }
        return found;
    }

    @SuppressWarnings("unchecked")
    private static <T> Holder<DataComponentType<T>> cast(Holder<DataComponentType<?>> holder) {
        return (Holder<DataComponentType<T>>) (Holder<?>) holder;
    }

    private record ModRegisters(DeferredRegister.Items items, DeferredRegister<SoundEvent> sounds,
                                DeferredRegister.DataComponents components,
                                DeferredRegister<CreativeModeTab> tabs) {
    }
}
