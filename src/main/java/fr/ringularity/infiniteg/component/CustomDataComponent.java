package fr.ringularity.infiniteg.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Objects;

public class CustomDataComponent {
    private final String name;
    private final float value;
    private final List<StatTest> stats;

    public CustomDataComponent(String name, float value, List<StatTest> stats) {
        this.name = name;
        this.value = value;
        this.stats = List.copyOf(stats);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomDataComponent)) return false;
        CustomDataComponent that = (CustomDataComponent) o;
        return Float.compare(that.value, value) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(stats, that.stats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, stats);
    }

    public String name() { return name; }
    public float value() { return value; }
    public List<StatTest> stats() { return stats; }

    public static final Codec<CustomDataComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").forGetter(CustomDataComponent::name),
                    Codec.FLOAT.fieldOf("value").forGetter(CustomDataComponent::value),
                    Codec.list(StatTest.CODEC).fieldOf("stats").forGetter(CustomDataComponent::stats)
            ).apply(instance, CustomDataComponent::new)
    );
}
