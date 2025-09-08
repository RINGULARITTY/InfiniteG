package fr.ringularity.infiniteg.items;

import fr.ringularity.infiniteg.component.CompactDataComponent;
import fr.ringularity.infiniteg.component.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CompactItem extends Item {
    private final Item storedItemType;

    public CompactItem(Properties properties, Item storedItemType) {
        super(properties.stacksTo(1));
        this.storedItemType = storedItemType;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @NotNull TooltipContext pContext, @NotNull TooltipDisplay tooltipDisplay,
                                @NotNull Consumer<Component> components, @NotNull TooltipFlag tooltipFlag) {
        final CompactDataComponent cdc = pStack.get(ModDataComponents.COMPACT_COMPONENT);
        if (cdc != null) {
            MutableComponent tooltipLine = Component.translatable("tooltip.infiniteg.contains")
                    .append(Component.literal(" " + cdc.quantity() + " "))
                    .append(Component.translatable(cdc.item().getDescriptionId()))
                    .append(Component.literal(cdc.quantity() > 1 ? "s" : ""))
                    .withStyle(ChatFormatting.YELLOW);

            components.accept(tooltipLine);
        }
        super.appendHoverText(pStack, pContext, tooltipDisplay, components, tooltipFlag);
    }
}
