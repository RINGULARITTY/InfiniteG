package fr.ringularity.infiniteg.items;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class InfiniteGCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, InfiniteG.MOD_ID);

    public static final Supplier<CreativeModeTab> INFINITEG_TAB = TABS.register("infiniteg", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + InfiniteG.MOD_ID + ".infiniteg"))
                    .icon(() -> new ItemStack(ModItems.HAMMER.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.HAMMER.get());
                        output.accept(ModItems.IRON_PLATE.get());
                        output.accept(ModItems.DENSE_IRON_PLATE.get());
                        output.accept(ModItems.DIAMOND_COMPACTOR.get());
                        output.accept(ModItems.LARGE_PLANK.get());
                        output.accept(ModItems.TEST.get());
                        output.accept(ModBlocks.WORKSTATION.get());
                        output.accept(ModBlocks.COMPACTOR.get());
                        output.accept(ModBlocks.DENSE_COBBLESTONE.get());
                        output.accept(ModBlocks.DENSE_WOOD.get());
                        output.accept(ModBlocks.DENSE_PLANKS.get());
                        output.accept(ModBlocks.DENSE_IRON.get());
                        output.accept(ModBlocks.DARK_ENERGY_NETWORK_CONTROLLER);
                        output.accept(ModBlocks.DARK_ENERGY_PIPE);
                        output.accept(ModBlocks.DARK_ENERGY_GENERATOR);
                        output.accept(ModBlocks.DARK_ENERGY_PURIFIER);
                    })
                    .build()
    );

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
