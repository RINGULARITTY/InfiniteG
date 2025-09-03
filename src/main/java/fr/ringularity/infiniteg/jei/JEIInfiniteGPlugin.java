package fr.ringularity.infiniteg.jei;

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
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        System.out.println("[JEI][Plugin] registerCategories START " + WorkstationJei.idInfo("categories"));
        try {
            reg.addRecipeCategories(new WorkstationRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
            System.out.println("[JEI][Plugin] registerCategories DONE");
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("[JEI][Plugin] registerCategories FAIL");
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        System.out.println("[JEI][Plugin] registerRecipes START " + WorkstationJei.idInfo("recipes"));
        java.util.List<WorkstationRecipe> list = new java.util.ArrayList<>(WorkstationRecipe.RECIPES);
        System.out.println("[JEI][Plugin] recipes count = " + list.size());
        reg.addRecipes(WorkstationJei.TYPE, list);
        System.out.println("[JEI][Plugin] registerRecipes DONE");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        System.out.println("[JEI][Plugin] registerRecipeCatalysts START " + WorkstationJei.idInfo("catalyst"));
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.WORKSTATION.get()), WorkstationJei.TYPE);
        System.out.println("[JEI][Plugin] registerRecipeCatalysts DONE");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration reg) {
        System.out.println("[JEI][Plugin] registerGuiHandlers START " + WorkstationJei.idInfo("clickArea"));
        // Coords relatives à votre GUI, pas à l’écran
        reg.addRecipeClickArea(WorkstationScreen.class, 0, 0, 300, 150, WorkstationJei.TYPE);
        System.out.println("[JEI][Plugin] registerGuiHandlers DONE");
    }
}

