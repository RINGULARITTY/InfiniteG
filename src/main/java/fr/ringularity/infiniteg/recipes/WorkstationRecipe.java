package fr.ringularity.infiniteg.recipes;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WorkstationRecipe {
    final ItemStack outputStack;
    final List<Ingredient> ingredients;

    public WorkstationRecipe(ItemStack outputStack, List<Ingredient> ingredients) {
        this.outputStack = outputStack;
        this.ingredients = ingredients;
    }

    public static class Ingredient {
        final ItemStack requiredStack;
        final long requiredAmount;
        long currentAmount;

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
}
