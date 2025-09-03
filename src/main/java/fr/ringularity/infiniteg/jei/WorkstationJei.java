// WorkstationJei.java
package fr.ringularity.infiniteg.jei;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.recipes.WorkstationRecipe;
import mezz.jei.api.recipe.RecipeType;

public final class WorkstationJei {
    private WorkstationJei() {}
    public static final RecipeType<WorkstationRecipe> TYPE =
            RecipeType.create(InfiniteG.MOD_ID, "workstation", WorkstationRecipe.class);

    public static String idInfo(String where) {
        return where + " -> TYPE@" + System.identityHashCode(TYPE) + " uid=" + TYPE.getUid();
    }
}
