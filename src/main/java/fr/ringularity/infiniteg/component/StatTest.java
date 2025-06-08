package fr.ringularity.infiniteg.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

public class StatTest {
    private final String name;
    private final double value;

    public StatTest(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatTest)) return false;
        StatTest s = (StatTest) o;
        return Objects.equals(name, s.name) &&
                Double.compare(s.value, value) == 0;
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    public String name() { return name; }
    public double value() { return value; }

    public static final Codec<StatTest> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").forGetter(StatTest::name),
                    Codec.DOUBLE.fieldOf("value").forGetter(StatTest::value)
            ).apply(instance, StatTest::new)
    );
}
