package fr.ringularity.infiniteg.component;

import fr.ringularity.infiniteg.InfiniteG;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> REGISTRY =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, InfiniteG.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompactDataComponent>> COMPACT_COMPONENT =
        REGISTRY.register("compact_component", () ->
                DataComponentType.<CompactDataComponent>builder()
                        .persistent(CompactDataComponent.CODEC)
                        .cacheEncoding()
                        .build()
        );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomDataComponent>> CUSTOM_COMPONENT =
        REGISTRY.register("custom_data", () ->
                DataComponentType.<CustomDataComponent>builder()
                        .persistent(CustomDataComponent.CODEC)
                        .cacheEncoding()
                        .build()
        );

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
