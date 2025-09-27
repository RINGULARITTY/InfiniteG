package fr.ringularity.infiniteg.network;

import fr.ringularity.infiniteg.network.ig_container_packets.IGContainerSlotsS2C;
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

        registrar.playToClient(
                IGContainerSlotsS2C.TYPE,
                IGContainerSlotsS2C.STREAM_CODEC,
                IGContainerSlotsS2C::handleClient
        );
    }
}



