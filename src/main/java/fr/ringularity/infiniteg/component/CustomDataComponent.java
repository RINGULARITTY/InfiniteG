package fr.ringularity.infiniteg.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Objects;

public record CustomDataComponent(String name, float value, List<StatTest> stats) {
    public CustomDataComponent(String name, float value, List<StatTest> stats) {
        this.name = name;
        this.value = value;
        this.stats = List.copyOf(stats);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomDataComponent(String name1, float value1, List<StatTest> stats1))) return false;
        return Float.compare(value1, value) == 0 &&
                Objects.equals(name, name1) &&
                Objects.equals(stats, stats1);
    }

    public static final Codec<CustomDataComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").forGetter(CustomDataComponent::name),
                    Codec.FLOAT.fieldOf("value").forGetter(CustomDataComponent::value),
                    Codec.list(StatTest.CODEC).fieldOf("stats").forGetter(CustomDataComponent::stats)
            ).apply(instance, CustomDataComponent::new)
    );
}
