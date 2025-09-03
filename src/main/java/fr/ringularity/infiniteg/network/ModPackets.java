package fr.ringularity.infiniteg.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPackets {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("infiniteg:payloads");

        registrar.playToServer(
                IntPayloadToServer.TYPE,
                IntPayloadToServer.CODEC,
                IntPayloadToServer::handleServer
        );

        registrar.playToClient(
                UpdateItemQuantitiesToClient.TYPE,
                UpdateItemQuantitiesToClient.CODEC,
                UpdateItemQuantitiesToClient::handleClient
        );
    }
}



