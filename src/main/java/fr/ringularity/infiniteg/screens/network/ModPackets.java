package fr.ringularity.infiniteg.screens.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPackets {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playBidirectional(
                CustomItemQuantityPayload.TYPE,
                CustomItemQuantityPayload.CODEC,
                new DirectionalPayloadHandler<>(
                        CustomItemQuantityPayload::handleClient,
                        CustomItemQuantityPayload::handleServer
                )
        );
    }
}
