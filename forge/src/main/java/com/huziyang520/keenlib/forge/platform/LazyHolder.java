package com.huziyang520.keenlib.forge.platform;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.RegistryObject;
import com.mojang.datafixers.util.Either;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Forge 1.20.1 的 RegistryObject 不是 Holder，这里包一层：第一次取值时才从注册表解析真正的 Holder。
 */
final class LazyHolder<T> implements Holder<T> {

    private final Supplier<Holder<T>> resolver;
    private Holder<T> resolved;

    private LazyHolder(Supplier<Holder<T>> resolver) {
        this.resolver = resolver;
    }

    static <T> Holder<T> of(RegistryObject<T> object, Registry<T> registry) {
        return new LazyHolder<>(() -> registry.getResourceKey(object.get())
                .flatMap(registry::getHolder)
                .map(holder -> (Holder<T>) holder)
                .orElseThrow(() -> new IllegalStateException("Not registered: " + object.getId())));
    }

    private Holder<T> holder() {
        if (resolved == null) {
            resolved = resolver.get();
        }
        return resolved;
    }

    @Override
    public T value() {
        return holder().value();
    }

    @Override
    public boolean isBound() {
        return holder().isBound();
    }

    @Override
    public boolean is(ResourceLocation id) {
        return holder().is(id);
    }

    @Override
    public boolean is(ResourceKey<T> key) {
        return holder().is(key);
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        return holder().is(predicate);
    }

    @Override
    public boolean is(TagKey<T> tag) {
        return holder().is(tag);
    }

    @Override
    public Stream<TagKey<T>> tags() {
        return holder().tags();
    }

    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        return holder().unwrap();
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return holder().unwrapKey();
    }

    @Override
    public Kind kind() {
        return holder().kind();
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> owner) {
        return holder().canSerializeIn(owner);
    }

    @Override
    public String toString() {
        return holder().toString();
    }
}
