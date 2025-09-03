package fr.ringularity.infiniteg.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.Objects;

public class CompactDataComponent {
    private final long quantity;
    private final Item item;

    public CompactDataComponent(long quantity, Item item) {
        this.quantity = quantity;
        this.item = item;
    }

    public long quantity() { return quantity; }
    public Item item() { return item; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompactDataComponent)) return false;
        CompactDataComponent that = (CompactDataComponent) o;
        return quantity == that.quantity && item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, item);
    }

    public static final Codec<CompactDataComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("quantity").forGetter(CompactDataComponent::quantity),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(CompactDataComponent::item)
            ).apply(instance, CompactDataComponent::new)
    );
}
