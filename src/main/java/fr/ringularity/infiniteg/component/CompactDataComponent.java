package fr.ringularity.infiniteg.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.Objects;

public record CompactDataComponent(long quantity, Item item) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompactDataComponent(long quantity1, Item item1))) return false;
        return quantity == quantity1 && item.equals(item1);
    }

    public static final Codec<CompactDataComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("quantity").forGetter(CompactDataComponent::quantity),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(CompactDataComponent::item)
            ).apply(instance, CompactDataComponent::new)
    );
}
