package fr.ringularity.infiniteg.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

public record StatTest(String name, double value) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatTest(String name1, double value1))) return false;
        return Objects.equals(name, name1) &&
                Double.compare(value1, value) == 0;
    }

    public static final Codec<StatTest> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").forGetter(StatTest::name),
                    Codec.DOUBLE.fieldOf("value").forGetter(StatTest::value)
            ).apply(instance, StatTest::new)
    );
}
