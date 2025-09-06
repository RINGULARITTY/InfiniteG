package fr.ringularity.infiniteg.capabilities;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class DarkChat {
    private DarkChat() {}

    public static Component formatNetworkSummary(DarkEnergyNetwork rec) {
        var agg = rec.aggregate();
        MutableComponent root = Component.literal("=== Dark Energy Network ===\n");
        root.append(Component.literal("UUID: " + rec.id() + "\n"));
        root.append(Component.literal("Quantity: " + agg.quantity() + "\n"));
        root.append(Component.literal("Specs:\n"));
        for (var e : agg.properties().entrySet()) {
            root.append(Component.literal(" - " + e.getKey() + ": " + e.getValue() + "\n"));
        }
        root.append(Component.literal("Nodes (" + rec.nodes().size() + "):\n"));
        for (var n : rec.nodes()) {
            var dim = n.level().location().toString();
            var p = n.pos();
            root.append(Component.literal(
                    " - " + dim + " @ " + p.getX() + " " + p.getY() + " " + p.getZ() + "\n"));
        }
        return root;
    }
}
