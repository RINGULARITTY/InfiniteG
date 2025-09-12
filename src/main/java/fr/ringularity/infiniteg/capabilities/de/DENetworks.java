package fr.ringularity.infiniteg.capabilities.de;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public final class DENetworks {
    // Récupération/attachement: Overworld recommandé si global
    public static DENetworksData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(DENetworksData.ID);
    }

    // Variante “global overworld”
    public static DENetworksData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(DENetworksData.ID);
    }
}