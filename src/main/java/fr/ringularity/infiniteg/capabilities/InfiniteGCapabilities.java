package fr.ringularity.infiniteg.capabilities;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class InfiniteGCapabilities {
    public class ModCapabilityRegistration {
        public static void register(IEventBus modBus) {
            modBus.addListener(EventPriority.NORMAL, ModCapabilityRegistration::onRegisterCapabilities);
        }

        private static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {

        }
    }
}
