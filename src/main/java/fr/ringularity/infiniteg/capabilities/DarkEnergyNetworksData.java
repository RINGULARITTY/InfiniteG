package fr.ringularity.infiniteg.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.math.BigDecimal;
import java.util.*;

public class DarkEnergyNetworksData extends SavedData {
    private final Map<UUID, DarkEnergyNetwork> networks = new HashMap<>();
    private final Map<DarkEnergyNetworkNodeRef, UUID> nodeToNetwork = new HashMap<>();
    private final Map<ResourceLocation, DarkEnergyBridgeRule> bridges = new HashMap<>();

    public Optional<UUID> networkOf(DarkEnergyNetworkNodeRef node) {
        return Optional.ofNullable(nodeToNetwork.get(node));
    }

    public DarkEnergyNetwork getOrCreate(UUID id) {
        return networks.computeIfAbsent(id, k -> new DarkEnergyNetwork(k, new HashSet<>(),
                new DarkEnergyNetworkAggregate(BigDecimal.ZERO, new HashMap<>())));
    }

    public UUID createEmptyNetwork() {
        UUID id = UUID.randomUUID();
        networks.put(id, new DarkEnergyNetwork(id, new HashSet<>(),
                new DarkEnergyNetworkAggregate(BigDecimal.ZERO, new HashMap<>())));
        setDirty();
        return id;
    }

    public void addNodeTo(UUID networkId, DarkEnergyNetworkNodeRef node) {
        var rec = getOrCreate(networkId).addNode(node);
        networks.put(networkId, rec);
        nodeToNetwork.put(node, networkId);
        setDirty();
    }

    public void removeNode(DarkEnergyNetworkNodeRef node) {
        var netId = nodeToNetwork.remove(node);
        if (netId != null) {
            var rec = networks.get(netId);
            if (rec != null) {
                rec = rec.removeNode(node);
                networks.put(netId, rec);
            }
            setDirty();
        }
    }

    public void deleteNetwork(UUID id) {
        var rec = networks.remove(id);
        if (rec != null) {
            for (var n : rec.nodes()) nodeToNetwork.remove(n);
            setDirty();
        }
    }

    public void mergeNetworks(UUID keep, UUID remove) {
        var a = networks.get(keep);
        var b = networks.get(remove);
        if (a == null || b == null) return;
        var mergedNodes = new HashSet<>(a.nodes());
        mergedNodes.addAll(b.nodes());
        var agg = a.aggregate().mixIn(b.aggregate().quantity(), b.aggregate().properties(), false);
        var merged = new DarkEnergyNetwork(keep, mergedNodes, agg);
        networks.put(keep, merged);
        // réindexer
        for (var n : b.nodes()) nodeToNetwork.put(n, keep);
        networks.remove(remove);
        setDirty();
    }

    public void modifyDarkEnergy(UUID networkId, BigDecimal mIn, Map<String, BigDecimal> props) {
        var rec = networks.get(networkId);
        if (rec == null) return;
        var agg2 = rec.aggregate().mixIn(mIn, props, true);
        networks.put(networkId, rec.withAggregate(agg2));
        setDirty();
    }

    public void injectDarkEnergy(UUID networkId, BigDecimal qIn, Map<String, BigDecimal> props) {
        var rec = networks.get(networkId);
        if (rec == null) return;
        var agg2 = rec.aggregate().mixIn(qIn, props, false);
        networks.put(networkId, rec.withAggregate(agg2));
        setDirty();
    }

    public void addBridge(ResourceLocation id, DarkEnergyBridgeRule rule) {
        bridges.put(id, rule);
        setDirty();
    }

    public void removeBridge(ResourceLocation id) {
        if (bridges.remove(id) != null) setDirty();
    }

    public Map<UUID, DarkEnergyNetwork> networksView() { return Collections.unmodifiableMap(networks); }
    public Map<ResourceLocation, DarkEnergyBridgeRule> bridgesView() { return Collections.unmodifiableMap(bridges); }

    // --- CODEC & SavedDataType ---

    // DTO sérialisable
    public record SerializableState(
            Map<String, DarkEnergyNetwork> networks, // clé = uuid string
            Map<String, String> nodeIndex,       // "dim|x,y,z" -> uuid string
            Map<String, DarkEnergyBridgeRule> bridges
    ) {
        static final Codec<SerializableState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.unboundedMap(Codec.STRING, DarkEnergyNetwork.CODEC).fieldOf("networks").forGetter(SerializableState::networks),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("node_index").forGetter(SerializableState::nodeIndex),
                Codec.unboundedMap(Codec.STRING, DarkEnergyBridgeRule.CODEC).fieldOf("bridges").forGetter(SerializableState::bridges)
        ).apply(inst, SerializableState::new));
    }

    private static String nodeKey(DarkEnergyNetworkNodeRef n) {
        return n.level().location() + "|" + n.pos().getX() + "," + n.pos().getY() + "," + n.pos().getZ();
    }

    private SerializableState toSerializable() {
        Map<String, DarkEnergyNetwork> nets = new HashMap<>();
        for (var e : networks.entrySet()) nets.put(e.getKey().toString(), e.getValue());

        Map<String, String> idx = new HashMap<>();
        for (var e : nodeToNetwork.entrySet()) idx.put(nodeKey(e.getKey()), e.getValue().toString());

        Map<String, DarkEnergyBridgeRule> br = new HashMap<>();
        for (var e : bridges.entrySet()) {
            br.put(e.getKey().toString(), e.getValue());
        }
        return new SerializableState(nets, idx, br);
    }

    private static DarkEnergyNetworksData fromSerializable(SerializableState s) {
        var data = new DarkEnergyNetworksData();
        for (var e : s.networks.entrySet()) {
            var id = UUID.fromString(e.getKey());
            data.networks.put(id, e.getValue());
            for (var n : e.getValue().nodes()) {
                data.nodeToNetwork.put(n, id);
            }
        }
        for (var e : s.bridges.entrySet()) {
            data.bridges.put(ResourceLocation.parse(e.getKey()), e.getValue());
        }
        return data;
    }

    // SavedDataType (sans contexte)
    public static final SavedDataType<DarkEnergyNetworksData> ID =
            new SavedDataType<>("dark_energy_networks",
                    DarkEnergyNetworksData::new,
                    SerializableState.CODEC.xmap(DarkEnergyNetworksData::fromSerializable, DarkEnergyNetworksData::toSerializable)
            );

    // ctors
    public DarkEnergyNetworksData() {}

    // Aucun override save(NBT) nécessaire: c’est géré par le SavedDataType + Codec
}
