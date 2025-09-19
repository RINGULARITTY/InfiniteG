package fr.ringularity.infiniteg;

import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import fr.ringularity.infiniteg.capabilities.ModCapabilityRegistration;
import fr.ringularity.infiniteg.component.ModDataComponents;
import fr.ringularity.infiniteg.items.InfiniteGCreativeTab;
import fr.ringularity.infiniteg.items.ModItems;
import fr.ringularity.infiniteg.menus.ModMenuTypes;
import fr.ringularity.infiniteg.network.ModPackets;
import fr.ringularity.infiniteg.screens.AssemblerScreen;
import fr.ringularity.infiniteg.screens.CompactorScreen;
import fr.ringularity.infiniteg.screens.WorkstationScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(InfiniteG.MOD_ID)
public class InfiniteG
{
    public static final String MOD_ID = "infiniteg";

    public InfiniteG(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);

        InfiniteGCreativeTab.register(modEventBus);

        ModDataComponents.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid=MOD_ID, value=Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.COMPACTOR_MENU.get(), CompactorScreen::new);
            event.register(ModMenuTypes.WORKSTATION_MENU.get(), WorkstationScreen::new);
            event.register(ModMenuTypes.ASSEMBLER_MENU.get(), AssemblerScreen::new);
        }

        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event) {
            ModPackets.register(event);
        }

        @SubscribeEvent
        public static void registerCaps(RegisterCapabilitiesEvent event) {
            ModCapabilityRegistration.registerCapabilities(event);
        }
    }
}
