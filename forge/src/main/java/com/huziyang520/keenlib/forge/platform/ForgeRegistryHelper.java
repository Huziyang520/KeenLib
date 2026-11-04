package com.huziyang520.keenlib.forge.platform;

import com.huziyang520.keenlib.platform.services.IRegistryHelper;
import com.huziyang520.keenlib.registry.KeenRegistry;
import com.huziyang520.keenlib.registry.KeenTabEntries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Forge 1.20.1 没有数据组件，component(...) 能力在本线不提供。
 */
public class ForgeRegistryHelper implements IRegistryHelper {

    private final Map<String, ModRegisters> registers = new HashMap<>();

    @Override
    public void bind(String modId, Object eventBus) {
        if (!(eventBus instanceof IEventBus bus)) {
            throw new IllegalArgumentException("Forge requires the mod event bus when binding " + modId);
        }
        DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, modId);
        DeferredRegister<SoundEvent> sounds = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, modId);
        DeferredRegister<CreativeModeTab> tabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
        items.register(bus);
        sounds.register(bus);
        tabs.register(bus);
        registers.put(modId, new ModRegisters(items, sounds, tabs));
    }

    @Override
    public <T extends Item> Holder<Item> item(String modId, String path, Function<Item.Properties, T> factory) {
        RegistryObject<Item> registered = registers(modId).items.register(path, () -> factory.apply(new Item.Properties()));
        return LazyHolder.of(registered, BuiltInRegistries.ITEM);
    }

    @Override
    public Holder<SoundEvent> sound(String modId, String path) {
        RegistryObject<SoundEvent> registered = registers(modId).sounds.register(path,
                () -> SoundEvent.createVariableRangeEvent(KeenRegistry.of(modId).id(path)));
        return LazyHolder.of(registered, BuiltInRegistries.SOUND_EVENT);
    }

    @Override
    public Holder<CreativeModeTab> tab(String modId, String path, Component title, Supplier<ItemStack> icon,
                                       KeenTabEntries entries) {
        RegistryObject<CreativeModeTab> registered = registers(modId).tabs.register(path,
                () -> CreativeModeTab.builder()
                        .title(title)
                        .icon(icon)
                        .displayItems((parameters, output) -> entries.addTo(output::accept))
                        .build());
        return LazyHolder.of(registered, BuiltInRegistries.CREATIVE_MODE_TAB);
    }

    private ModRegisters registers(String modId) {
        ModRegisters found = registers.get(modId);
        if (found == null) {
            throw new IllegalStateException("Registry helper is not bound for " + modId);
        }
        return found;
    }

    private record ModRegisters(DeferredRegister<Item> items, DeferredRegister<SoundEvent> sounds,
                                DeferredRegister<CreativeModeTab> tabs) {
    }
}
