package fr.ringularity.infiniteg.menus;

import fr.ringularity.infiniteg.InfiniteG;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, InfiniteG.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<CompactorMenu>> COMPACTOR_MENU =
            registerMenuType("compactor_menu", CompactorMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<WorkstationMenu>> WORKSTATION_MENU =
            registerMenuType("workstation_menu", WorkstationMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<BasicAssemblerControllerMenu>> BASIC_ASSEMBLER_MENU =
            registerMenuType("basic_assembler_menu", BasicAssemblerControllerMenu::clientCtor);


    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name,
                                                                                                               IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
