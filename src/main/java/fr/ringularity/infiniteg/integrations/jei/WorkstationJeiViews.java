package fr.ringularity.infiniteg.integrations.jei;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.recipes.WorkstationRecipe;
import mezz.jei.api.recipe.RecipeType;

import java.util.List;

public final class WorkstationJeiViews {
    private WorkstationJeiViews() {}

    public record WorkstationRecipeView(WorkstationRecipe base, int page, List<WorkstationRecipe.Ingredient> window) {}

    public static final RecipeType<WorkstationRecipeView> TYPE =
            RecipeType.create(InfiniteG.MOD_ID, "workstation_view", WorkstationRecipeView.class);
}
