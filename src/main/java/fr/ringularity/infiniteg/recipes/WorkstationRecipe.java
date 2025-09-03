package fr.ringularity.infiniteg.recipes;

import fr.ringularity.infiniteg.abstracts.ItemQuantity;
import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.component.CompactDataComponent;
import fr.ringularity.infiniteg.component.ModDataComponents;
import fr.ringularity.infiniteg.items.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.math.BigInteger;
import java.util.*;

public class WorkstationRecipe {
    public static enum ToolType {
        HAMMER,
        SCREWDRIVER,
        WRENCH,
        SAW,
        DRILL,
        WIRE_STRIPPER,
        CHISEL,
        PLIERS
    }

    public final Map<ToolType, Integer> requiredTiers;
    public final ItemStack outputStack;
    public final List<Ingredient> ingredients;
    public final int processTime;

    public WorkstationRecipe(Map<ToolType, Integer> requiredTiers, ItemStack outputStack, List<Ingredient> ingredients, int processTime) {
        this.requiredTiers = requiredTiers;
        this.outputStack = outputStack;
        this.ingredients = ingredients;
        this.processTime = processTime;
    }

    public static class Ingredient {
        public ItemQuantity requiredStack;

        public Ingredient(ItemQuantity requiredStack) {
            this.requiredStack = requiredStack;
        }
    }

    public static final ArrayList<WorkstationRecipe> RECIPES = new ArrayList<>(List.of(
            new WorkstationRecipe(
                    Map.ofEntries(
                            Map.entry(ToolType.HAMMER, 0)
                    ),
                    new ItemStack(ModItems.DENSE_IRON_PLATE.get(), 2),
                    List.of(
                            new Ingredient(new ItemQuantity(new ItemStack(ModBlocks.DENSE_IRON.asItem()), BigInteger.ONE)),
                            new Ingredient(new ItemQuantity(new ItemStack(Items.DIAMOND), BigInteger.TEN))
                    ),
                    100
            ),
            new WorkstationRecipe(
                    Map.ofEntries(
                            Map.entry(ToolType.HAMMER, 0)
                    ),
                    new ItemStack(ModBlocks.DENSE_IRON.asItem(), 1),
                    List.of(
                            new Ingredient(new ItemQuantity(new ItemStack(Blocks.ACACIA_BUTTON.asItem()), BigInteger.ONE)),
                            new Ingredient(new ItemQuantity(new ItemStack(Items.DIAMOND.asItem()), BigInteger.TEN)),
                            new Ingredient(new ItemQuantity(new ItemStack(Items.GOLD_INGOT.asItem()), BigInteger.TWO))
                    ),
                    100
            )
    ));

    static {
        final ItemStack is = new ItemStack(Items.DIAMOND);
        is.set(ModDataComponents.COMPACT_COMPONENT, new CompactDataComponent(
                81,
                Items.DIAMOND
        ));

        RECIPES.add(
                new WorkstationRecipe(
                        Map.ofEntries(
                                Map.entry(ToolType.HAMMER, 0)
                        ),
                        new ItemStack(Blocks.DIAMOND_BLOCK.asItem(), 1),
                        List.of(
                                new Ingredient(new ItemQuantity(is, BigInteger.ONE)),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.DRAGON_EGG), BigInteger.TWO)),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_PEARL), BigInteger.valueOf(100))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.WATER_BUCKET), BigInteger.valueOf(50))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.LAVA_BUCKET), BigInteger.valueOf(25))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.EMERALD), BigInteger.valueOf(5)))
                        ),
                        100
                )
        );
    }

    public static Map<Integer, WorkstationRecipe> getRecipesWithToolTiers(Map<ToolType, Integer> toolsTier) {
        final Map<Integer, WorkstationRecipe> recipes = new HashMap<>();
        for (int i = 0; i < RECIPES.size(); ++i) {
            final WorkstationRecipe wr = RECIPES.get(i);
            boolean missingItemTier = false;
            for (final Map.Entry<ToolType, Integer> toolTier : wr.requiredTiers.entrySet()) {
                if (!toolsTier.containsKey(toolTier.getKey()) || toolsTier.get(toolTier.getKey()) < toolTier.getValue()) {
                    missingItemTier = true;
                    break;
                }
            }
            if (!missingItemTier)
                recipes.put(i, new WorkstationRecipe(
                        new HashMap<>(wr.requiredTiers),
                        wr.outputStack.copy(),
                        new ArrayList<>(wr.ingredients),
                        wr.processTime
                ));
        }

        return recipes;
    }
}
