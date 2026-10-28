package dev.ryanhcode.sable.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

/**
 * A registered entry; replacement for Veil's RegistryObject.
 */
public record SableRegistryObject<T>(Holder.Reference<T> holder) implements Supplier<T> {

    @Override
    public T get() {
        return this.holder.value();
    }

    public Identifier getId() {
        return this.holder.key().identifier();
    }
}
