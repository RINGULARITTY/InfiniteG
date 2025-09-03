package fr.ringularity.infiniteg.jei;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.recipes.WorkstationRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class WorkstationRecipeCategory implements IRecipeCategory<WorkstationRecipe> {
    private static final ResourceLocation GUI_TEX =
            ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "textures/gui/container/workstation.png");

    private final IDrawable background;
    private final IDrawable icon;

    public WorkstationRecipeCategory(IGuiHelper helper) {
        System.out.println("[JEI][Category] ctor begin");

        IDrawable bg;
        try {
            // Test minimal: blank 160x80 (remplace temporairement pour isoler texture)
            // bg = helper.createBlankDrawable(160, 80);
            // Version définitive: dessiner dans la plage valide du PNG
            bg = helper.createDrawable(GUI_TEX, 0, 0, 160, 80);
            System.out.println("[JEI][Category] background ok");
        } catch (Throwable t) {
            t.printStackTrace();
            // Fallback pour éviter d'interrompre l’enregistrement
            bg = helper.createBlankDrawable(160, 80);
            System.out.println("[JEI][Category] background fallback");
        }
        this.background = bg;

        IDrawable icn;
        try {
            // Pour isoler un éventuel problème de BlockItem non résolu, tester d’abord un item vanilla:
            // icn = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.CRAFTING_TABLE));
            icn = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.WORKSTATION.get()));
            System.out.println("[JEI][Category] icon ok");
        } catch (Throwable t) {
            t.printStackTrace();
            icn = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(net.minecraft.world.item.Items.CRAFTING_TABLE));
            System.out.println("[JEI][Category] icon fallback");
        }
        this.icon = icn;

        System.out.println("[JEI][Category] ctor end");
    }

    @Override
    public RecipeType<WorkstationRecipe> getRecipeType() {
        System.out.println("[JEI][Category] getRecipeType");
        return WorkstationJei.TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("test");
    }

    @Override
    public IDrawable getBackground() {
        System.out.println("[JEI][Category] getBackground");
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        System.out.println("[JEI][Category] getIcon");
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WorkstationRecipe recipe, IFocusGroup focuses) {
        System.out.println("[JEI][Category] setRecipe for output=" + recipe.outputStack);
        for (int i = 0; i < recipe.ingredients.size(); ++i) {
            ItemStack stack = recipe.ingredients.get(i).requiredStack.stack.copy();
            builder.addSlot(RecipeIngredientRole.INPUT, 8, 8 + 18 * i).addItemStack(stack);
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 8).addItemStack(recipe.outputStack.copy());
    }

    @Override
    public void draw(WorkstationRecipe recipe, IRecipeSlotsView view, GuiGraphics g, double mouseX, double mouseY) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        for (int i = 0; i < recipe.ingredients.size(); ++i) {
            String qty = "x " + recipe.ingredients.get(i).requiredStack.quantity;
            g.drawString(mc.font, qty, 28, 12 + 18 * i, 0xFF404040, false);
        }
    }
}

