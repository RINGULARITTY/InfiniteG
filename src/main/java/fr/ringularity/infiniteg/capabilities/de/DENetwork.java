package fr.ringularity.infiniteg.capabilities.de;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record DENetwork(
        UUID id,
        Set<DENetworkNodeRef> nodes,
        DENetworkAggregate aggregate
) {
    public static final Codec<DENetwork> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(DENetwork::id),
            DENetworkNodeRef.CODEC.listOf().xmap(HashSet::new, ArrayList::new).fieldOf("nodes").forGetter(n -> (HashSet<DENetworkNodeRef>) n.nodes),
            DENetworkAggregate.CODEC.fieldOf("agg").forGetter(DENetwork::aggregate)
    ).apply(inst, DENetwork::new));

    public DENetwork addNode(DENetworkNodeRef n) {
        var copy = new HashSet<>(nodes);
        copy.add(n);
        return new DENetwork(id, copy, aggregate);
    }

    public DENetwork removeNode(DENetworkNodeRef n) {
        var copy = new HashSet<>(nodes);
        copy.remove(n);
        return new DENetwork(id, copy, aggregate);
    }

    public DENetwork withAggregate(DENetworkAggregate a) {
        return new DENetwork(id, nodes, a);
    }
}
