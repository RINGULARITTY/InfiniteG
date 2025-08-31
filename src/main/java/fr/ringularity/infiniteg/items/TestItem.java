package fr.ringularity.infiniteg.items;

import fr.ringularity.infiniteg.component.CustomDataComponent;
import fr.ringularity.infiniteg.component.ModDataComponents;
import fr.ringularity.infiniteg.component.StatTest;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestItem extends Item {
    final String name;


    public TestItem(Properties properties, final String name) {
        super(properties);
        this.name = name;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        CustomDataComponent current = stack.get(ModDataComponents.CUSTOM_COMPONENT);

        if (current != null) {
            CustomDataComponent updated = new CustomDataComponent(
                    current.name(),
                    current.value() + 1,
                    current.stats()
            );
            stack.set(ModDataComponents.CUSTOM_COMPONENT, updated);
        } else {
            stack.set(ModDataComponents.CUSTOM_COMPONENT, new CustomDataComponent("Test", 0, List.of(new StatTest("Kappa", 0))));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, TooltipDisplay tooltipDisplay, Consumer<Component> components, TooltipFlag tooltipFlag) {
        components.accept(Component.translatable("tooltip.infiniteg." + name + ".ambient"));

        if(Screen.hasShiftDown()) {
            components.accept(Component.translatable("tooltip.infiniteg." + name + ".shift"));
        }

        final CustomDataComponent cdc = pStack.get(ModDataComponents.CUSTOM_COMPONENT);
        if (cdc != null) {

            components.accept(Component.literal("----------"));

            List<Component> dataLines = new ArrayList<>();
            dataLines.add(Component.literal("Data:"));
            dataLines.add(Component.literal("  - Name: " + cdc.name()));
            dataLines.add(Component.literal("  - Value: " + cdc.value()));
            dataLines.add(Component.literal("  - Stats: "));

            for (StatTest s : cdc.stats()) {
                dataLines.add(Component.literal("    - " + s.value() + " " + s.name()));
            }

            dataLines.forEach(line ->
                    components.accept(
                            line.copy().withStyle(ChatFormatting.GRAY)
                    )
            );
        }

        super.appendHoverText(pStack, pContext, tooltipDisplay, components, tooltipFlag);
    }
}
