package fr.ringularity.infiniteg.recipes;

import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.items.ModItems;
import net.minecraft.world.item.ItemStack;

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
        public final ItemStack requiredStack;
        public final long requiredAmount;
        public long currentAmount;

        public Ingredient(ItemStack requiredStack, long requiredAmount) {
            this.requiredStack = requiredStack;
            this.requiredAmount = requiredAmount;
        }

        public Ingredient(ItemStack requiredStack, long requiredAmount, long currentAmount) {
            this.requiredStack = requiredStack;
            this.requiredAmount = requiredAmount;
            this.currentAmount = currentAmount;
        }
    }

    public static final ArrayList<WorkstationRecipe> RECIPES = new ArrayList<>(List.of(
            new WorkstationRecipe(
                    Map.ofEntries(
                            Map.entry(ToolType.HAMMER, 0)
                    ),
                    new ItemStack(ModItems.DENSE_IRON_PLATE.get(), 2),
                    List.of(
                            new Ingredient(new ItemStack(ModBlocks.DENSE_IRON.asItem()), 1)
                    ),
                    100
            )
    ));

    static {
        for (int i = 0; i < 200; ++i) {
            RECIPES.add(
                    new WorkstationRecipe(
                            Map.ofEntries(
                                    Map.entry(ToolType.HAMMER, 0)
                            ),
                            new ItemStack(ModItems.DENSE_IRON_PLATE.get(), 2),
                            List.of(
                                    new Ingredient(new ItemStack(ModBlocks.DENSE_IRON.asItem()), 1)
                            ),
                            100
                    )
            );
        }
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
