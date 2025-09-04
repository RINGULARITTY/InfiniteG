package fr.ringularity.infiniteg.integrations.jei;

import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.format.BigIntegerFormat;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class WorkstationRecipeCategoryView implements IRecipeCategory<WorkstationJeiViews.WorkstationRecipeView> {
    private final IDrawable background;
    private final IDrawable icon;

    private static final int PAD = 8;
    private static final int SLOT = 18;
    private static final int COLS = 5;
    private static final int ROWS = 7;
    private static final int OUT_X = 125;
    private static final float SCALE = 0.5f;

    public WorkstationRecipeCategoryView(IGuiHelper helper) {
        int width = Math.max(OUT_X + SLOT + PAD, PAD + COLS * SLOT + PAD);
        int height = PAD + ROWS * SLOT + PAD;
        this.background = helper.createBlankDrawable(width, height);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.WORKSTATION.get()));
    }

    @Override public RecipeType<WorkstationJeiViews.WorkstationRecipeView> getRecipeType() { return WorkstationJeiViews.TYPE; }
    @Override public Component getTitle() { return Component.translatable("block.infiniteg.workstation"); }
    @Override public @Nullable IDrawable getIcon() { return icon; }
    @Override public IDrawable getBackground() { return background; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder b, WorkstationJeiViews.WorkstationRecipeView v, IFocusGroup f) {
        final Font font = net.minecraft.client.Minecraft.getInstance().font;

        for (int i = 0; i < v.window().size(); i++) {
            int row = i / COLS;
            int col = i % COLS;
            int x = PAD + col * SLOT;
            int y = PAD + 5 + row * SLOT;

            final Component amount = Component.literal(BigIntegerFormat.format(v.window().get(i).requiredStack.quantity));

            IRecipeSlotBuilder slot = b.addSlot(RecipeIngredientRole.INPUT, x, y)
                    .addItemStack(v.window().get(i).requiredStack.stack.copy());

            IDrawable overlay = new TextOverlayDrawable(font, amount, 0xFFFFFFFF, SCALE);
            slot.setOverlay(overlay, SLOT, SLOT);
        }

        final ItemStack s = v.base().output.stack;
        IRecipeSlotBuilder slot = b.addSlot(RecipeIngredientRole.OUTPUT, OUT_X, PAD + 5)
                .addItemStack(s);

        String outputQuantity = BigIntegerFormat.format(v.base().output.quantity);
        IDrawable overlay = new TextOverlayDrawable(font, Component.literal(outputQuantity), 0xFFFFFFFF, SCALE);
        slot.setOverlay(overlay, SLOT, SLOT);
    }

    @Override
    public void draw(WorkstationJeiViews.WorkstationRecipeView v, IRecipeSlotsView view, GuiGraphics g, double mx, double my) {
        var font = net.minecraft.client.Minecraft.getInstance().font;

        int total = v.base().ingredients.size();
        int pageSize = ROWS * COLS;
        int pageCount = Math.max(1, (int) Math.ceil(total / (double) pageSize));
        g.drawString(font, "Recipe part " + (v.page() + 1) + "/" + pageCount, 0, 0, 0xFF000000, false);
    }
}

