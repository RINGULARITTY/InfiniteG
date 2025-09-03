package fr.ringularity.infiniteg.screens;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.blocks.entities.WorkstationBlockEntity;
import fr.ringularity.infiniteg.format.BigIntegerFormat;
import fr.ringularity.infiniteg.menus.WorkstationMenu;
import fr.ringularity.infiniteg.network.IntPayloadToServer;
import fr.ringularity.infiniteg.network.UpdateItemQuantitiesToClient;
import fr.ringularity.infiniteg.recipes.WorkstationRecipe;
import fr.ringularity.infiniteg.screens.widgets.InteractiveButton;
import fr.ringularity.infiniteg.screens.widgets.ScrollableArea;
import fr.ringularity.infiniteg.screens.widgets.ScrollableElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkstationScreen extends InfiniteGScreen<WorkstationMenu> {

    private static final ResourceLocation GUI_BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "textures/gui/container/workstation.png");
    private static final ResourceLocation ELEMENT_BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "textures/gui/container/element.png");

    private int guiAbsoluteX;
    private int guiAbsoluteY;

    private final List<InteractiveButton> externalButtons = new ArrayList<>();

    private ScrollableArea ingredientsScrollArea;
    private ScrollableArea recipesScrollArea;

    private List<UpdateItemQuantitiesToClient.RecipeItemQuantityPayload> lastIngredientPayload = List.of();

    public WorkstationScreen(WorkstationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 2 * 176;
        this.imageHeight = 200;

        initializeExternalButtons();
    }

    private void initializeExternalButtons() {
        externalButtons.add(new InteractiveButton(
                15, 140, 80, 10,
                Component.literal("Fill Items"),
                null,
                () -> {
                    ClientPacketDistributor.sendToServer(new IntPayloadToServer(WorkstationBlockEntity.CMD_PUSH));
                }
        ));
    }

    @Override
    protected void init() {
        super.init();
        this.guiAbsoluteX = (width - imageWidth) / 2;
        this.guiAbsoluteY = (height - imageHeight) / 2;

        initializeIngredientsScrollArea();
        updateIngredientsDisplay(lastIngredientPayload);

        initializeRecipesScrollArea();

        ClientPacketDistributor.sendToServer(new IntPayloadToServer(WorkstationBlockEntity.CMD_QUERY));
    }

    private void initializeIngredientsScrollArea() {
        int scrollAreaRelativeX = 15;
        int scrollAreaRelativeY = 50;
        int scrollAreaWidth = 155;
        int scrollAreaHeight = 80;

        this.ingredientsScrollArea = new ScrollableArea(
                scrollAreaRelativeX, scrollAreaRelativeY,
                scrollAreaWidth, scrollAreaHeight,
                10
        );
    }

    private void initializeRecipesScrollArea() {
        int scrollAreaRelativeX = 176;
        int scrollAreaRelativeY = 50;
        int scrollAreaWidth = 162;
        int scrollAreaHeight = 80;

        this.recipesScrollArea = new ScrollableArea(
                scrollAreaRelativeX, scrollAreaRelativeY,
                scrollAreaWidth, scrollAreaHeight,
                9
        );

        updateRecipesDisplay(Map.of(WorkstationRecipe.ToolType.HAMMER, 0));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                GUI_BACKGROUND_TEXTURE,
                guiAbsoluteX, guiAbsoluteY,
                0, 0,
                imageWidth, imageHeight,
                512, 256
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int mouseRelativeToGuiX = mouseX - guiAbsoluteX;
        int mouseRelativeToGuiY = mouseY - guiAbsoluteY;

        renderScrollAreas(guiGraphics, mouseX, mouseY);
        renderExternalButtons(guiGraphics, mouseRelativeToGuiX, mouseRelativeToGuiY);
        renderTooltips(guiGraphics, mouseX, mouseY);

        ingredientsScrollArea.renderTooltip(this, guiGraphics, mouseX, mouseY);
        recipesScrollArea.renderTooltip(this, guiGraphics, mouseX, mouseY);
        guiGraphics.renderDeferredTooltip();
    }

    private void renderScrollAreas(GuiGraphics guiGraphics, int mouseAbsoluteX, int mouseAbsoluteY) {
        int ingredientsAbsoluteX = guiAbsoluteX + ingredientsScrollArea.getRelativeX();
        int ingredientsAbsoluteY = guiAbsoluteY + ingredientsScrollArea.getRelativeY();
        int recipesAbsoluteX = guiAbsoluteX + recipesScrollArea.getRelativeX();
        int recipesAbsoluteY = guiAbsoluteY + recipesScrollArea.getRelativeY();

        ingredientsScrollArea.render(guiGraphics, this.font, ingredientsAbsoluteX, ingredientsAbsoluteY, mouseAbsoluteX, mouseAbsoluteY);
        recipesScrollArea.render(guiGraphics, this.font, recipesAbsoluteX, recipesAbsoluteY, mouseAbsoluteX, mouseAbsoluteY);
    }

    private void renderExternalButtons(GuiGraphics guiGraphics, int mouseRelativeToGuiX, int mouseRelativeToGuiY) {
        for (InteractiveButton button : externalButtons) {
            button.render(guiGraphics, font, guiAbsoluteX, guiAbsoluteY, mouseRelativeToGuiX, mouseRelativeToGuiY);
        }
    }

    private void renderTooltips(GuiGraphics guiGraphics, int mouseAbsoluteX, int mouseAbsoluteY) {
        ingredientsScrollArea.renderTooltip(this, guiGraphics, mouseAbsoluteX, mouseAbsoluteY);
        recipesScrollArea.renderTooltip(this, guiGraphics, mouseAbsoluteX, mouseAbsoluteY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        float textScale = 0.5f;
        //renderScaledText(guiGraphics, textScale, this.title, this.titleLabelX, this.titleLabelY, 4210752);
        //renderScaledText(guiGraphics, textScale, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752);
    }

    private void renderScaledText(GuiGraphics guiGraphics, float scale, Component text, int x, int y, int color) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(scale, scale);
        guiGraphics.drawString(
                this.font, text,
                (int) (x / scale), (int) (y / scale),
                color, false
        );
        guiGraphics.pose().popMatrix();
    }

    public void updateIngredientsDisplay(List<UpdateItemQuantitiesToClient.RecipeItemQuantityPayload> itemQuantities) {
        this.lastIngredientPayload = itemQuantities;
        ingredientsScrollArea.clearElements();
        for (UpdateItemQuantitiesToClient.RecipeItemQuantityPayload itemQuantity : itemQuantities) {
            final ItemStack is = itemQuantity.stack();
            final BigInteger currentAmount = itemQuantity.currentAmount();
            final BigInteger requiredAmount = itemQuantity.requiredAmount();
            final boolean incomplete = currentAmount.compareTo(requiredAmount) < 0;

            ScrollableElement element = new ScrollableElement(155, 20)
                    .addBackgroundTexture(ELEMENT_BACKGROUND_TEXTURE, 0, 0, 155, 20, 200, 20)
                    .addItemIcon(is, 3, 2)
                    .addText(Component.translatable(is.getItem().getDescriptionId()), 23, 3, 0xFF000000, 0.75f)
                    .addText(Component.literal(BigIntegerFormat.format(currentAmount) + "/" + BigIntegerFormat.format(requiredAmount)), 23, 13, incomplete ? 0xFFbf2004 : 0xFF008c05, 0.6f);

            ingredientsScrollArea.addElement(element);
        }
    }

    private void updateRecipesDisplay(Map<WorkstationRecipe.ToolType, Integer> toolsTier) {
        recipesScrollArea.clearElements();
        Map<Integer, WorkstationRecipe> availableRecipes = WorkstationRecipe.getRecipesWithToolTiers(toolsTier);

        ScrollableElement currentRow = new ScrollableElement(162, 18);
        int recipeIndex = 0;

        for (Map.Entry<Integer, WorkstationRecipe> e : availableRecipes.entrySet()) {
            int buttonX = 18 * (recipeIndex % 9);
            int buttonY = 0;
            int recipeId = e.getKey();
            WorkstationRecipe recipe = e.getValue();

            currentRow.addInteractiveButton(
                    buttonX, buttonY, 18, 18,
                    Component.literal(BigIntegerFormat.format(recipe.output.quantity)),
                    recipe.output.stack,
                    () -> ClientPacketDistributor.sendToServer(new IntPayloadToServer(recipeId))
            );

            recipeIndex++;
            if (recipeIndex % 9 == 0) {
                recipesScrollArea.addElement(currentRow);
                currentRow = new ScrollableElement(162, 18);
            }
        }

        if (recipeIndex % 9 != 0) {
            recipesScrollArea.addElement(currentRow);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int ingredientsAbsoluteX = guiAbsoluteX + ingredientsScrollArea.getRelativeX();
        int ingredientsAbsoluteY = guiAbsoluteY + ingredientsScrollArea.getRelativeY();

        if (ingredientsScrollArea.handleMouseScroll(mouseX, mouseY, scrollX, scrollY,
                ingredientsAbsoluteX, ingredientsAbsoluteY)) {
            return true;
        }

        int recipesAbsoluteX = guiAbsoluteX + recipesScrollArea.getRelativeX();
        int recipesAbsoluteY = guiAbsoluteY + recipesScrollArea.getRelativeY();

        if (recipesScrollArea.handleMouseScroll(mouseX, mouseY, scrollX, scrollY,
                recipesAbsoluteX, recipesAbsoluteY)) {
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int mouseRelativeToGuiX = (int) (mouseX - guiAbsoluteX);
        int mouseRelativeToGuiY = (int) (mouseY - guiAbsoluteY);

        for (InteractiveButton externalButton : externalButtons) {
            if (externalButton.handleMouseClick(mouseRelativeToGuiX, mouseRelativeToGuiY, button)) {
                return true;
            }
        }

        int ingredientsAbsoluteX = guiAbsoluteX + ingredientsScrollArea.getRelativeX();
        int ingredientsAbsoluteY = guiAbsoluteY + ingredientsScrollArea.getRelativeY();

        if (ingredientsScrollArea.handleMouseClick(mouseX, mouseY, button,
                ingredientsAbsoluteX, ingredientsAbsoluteY)) {
            return true;
        }

        int recipesAbsoluteX = guiAbsoluteX + recipesScrollArea.getRelativeX();
        int recipesAbsoluteY = guiAbsoluteY + recipesScrollArea.getRelativeY();

        if (recipesScrollArea.handleMouseClick(mouseX, mouseY, button,
                recipesAbsoluteX, recipesAbsoluteY)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // Extern buttons
        int mouseRelativeToGuiX = (int) (mouseX - guiAbsoluteX);
        int mouseRelativeToGuiY = (int) (mouseY - guiAbsoluteY);

        for (InteractiveButton externalButton : externalButtons) {
            if (externalButton.handleMouseRelease(mouseRelativeToGuiX, mouseRelativeToGuiY, button)) {
                return true;
            }
        }

        // Scroll areas
        int ingredientsAbsoluteX = guiAbsoluteX + ingredientsScrollArea.getRelativeX();
        int ingredientsAbsoluteY = guiAbsoluteY + ingredientsScrollArea.getRelativeY();

        if (ingredientsScrollArea.handleMouseRelease(mouseX, mouseY, button,
                ingredientsAbsoluteX, ingredientsAbsoluteY)) {
            return true;
        }

        int recipesAbsoluteX = guiAbsoluteX + recipesScrollArea.getRelativeX();
        int recipesAbsoluteY = guiAbsoluteY + recipesScrollArea.getRelativeY();

        if (recipesScrollArea.handleMouseRelease(mouseX, mouseY, button,
                recipesAbsoluteX, recipesAbsoluteY)) {
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }
}
