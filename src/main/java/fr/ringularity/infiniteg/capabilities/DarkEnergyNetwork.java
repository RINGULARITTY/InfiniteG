package fr.ringularity.infiniteg.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record DarkEnergyNetwork(
        UUID id,
        Set<DarkEnergyNetworkNodeRef> nodes,
        DarkEnergyNetworkAggregate aggregate
) {
    public static final Codec<DarkEnergyNetwork> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(DarkEnergyNetwork::id),
            DarkEnergyNetworkNodeRef.CODEC.listOf().xmap(HashSet::new, ArrayList::new).fieldOf("nodes").forGetter(n -> (HashSet<DarkEnergyNetworkNodeRef>) n.nodes),
            DarkEnergyNetworkAggregate.CODEC.fieldOf("agg").forGetter(DarkEnergyNetwork::aggregate)
    ).apply(inst, DarkEnergyNetwork::new));

    public DarkEnergyNetwork addNode(DarkEnergyNetworkNodeRef n) {
        var copy = new HashSet<>(nodes);
        copy.add(n);
        return new DarkEnergyNetwork(id, copy, aggregate);
    }

    public DarkEnergyNetwork removeNode(DarkEnergyNetworkNodeRef n) {
        var copy = new HashSet<>(nodes);
        copy.remove(n);
        return new DarkEnergyNetwork(id, copy, aggregate);
    }

    public DarkEnergyNetwork withAggregate(DarkEnergyNetworkAggregate a) {
        return new DarkEnergyNetwork(id, nodes, a);
    }
}
