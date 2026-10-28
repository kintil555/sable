package dev.ryanhcode.sable.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

/**
 * A minimal replacement for Veil's RegistrationProvider: owns a standalone vanilla {@link MappedRegistry}
 * and registers entries into it eagerly.
 */
public final class SableRegistrationProvider<T> {

    private final MappedRegistry<T> registry;

    private SableRegistrationProvider(final ResourceKey<? extends Registry<T>> key) {
        this.registry = new MappedRegistry<>(key, Lifecycle.stable());
    }

    public static <T> SableRegistrationProvider<T> get(final ResourceKey<? extends Registry<T>> key, final String modId) {
        return new SableRegistrationProvider<>(key);
    }

    public <I extends T> SableRegistryObject<I> register(final Identifier id, final Supplier<I> supplier) {
        final ResourceKey<T> key = ResourceKey.create(this.registry.key(), id);
        @SuppressWarnings("unchecked") final Holder.Reference<I> holder = (Holder.Reference<I>) this.registry.register(key, supplier.get(), RegistrationInfo.BUILT_IN);
        return new SableRegistryObject<>(holder);
    }

    public Registry<T> asVanillaRegistry() {
        return this.registry;
    }
}
