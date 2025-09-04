package fr.ringularity.infiniteg.integrations.jei;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.recipes.WorkstationRecipe;
import fr.ringularity.infiniteg.screens.WorkstationScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEIInfiniteGPlugin implements IModPlugin {
    private static final int COLS = 5;
    private static final int ROWS = 7;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        reg.addRecipeCategories(
                new WorkstationRecipeCategoryView(reg.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        int pageSize = ROWS * COLS;
        java.util.List<WorkstationJeiViews.WorkstationRecipeView> views = new java.util.ArrayList<>();
        for (WorkstationRecipe r : WorkstationRecipe.RECIPES) {
            java.util.List<WorkstationRecipe.Ingredient> ing = r.ingredients;
            for (int start = 0, page = 0; start < ing.size(); start += pageSize, page++) {
                int end = Math.min(start + pageSize, ing.size());
                views.add(new WorkstationJeiViews.WorkstationRecipeView(r, page, ing.subList(start, end)));
            }
        }
        registration.addRecipes(WorkstationJeiViews.TYPE, views);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.WORKSTATION.get()), WorkstationJeiViews.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration reg) {
        reg.addRecipeClickArea(WorkstationScreen.class, 14, 10, 18, 18, WorkstationJeiViews.TYPE);
    }
}
