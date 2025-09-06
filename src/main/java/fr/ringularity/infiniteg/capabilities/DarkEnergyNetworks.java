package fr.ringularity.infiniteg.capabilities;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public final class DarkEnergyNetworks {
    // Récupération/attachement: Overworld recommandé si global
    public static DarkEnergyNetworksData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(DarkEnergyNetworksData.ID);
    }

    // Variante “global overworld”
    public static DarkEnergyNetworksData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(DarkEnergyNetworksData.ID);
    }
}