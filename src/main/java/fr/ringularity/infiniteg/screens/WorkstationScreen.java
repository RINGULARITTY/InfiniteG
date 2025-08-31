package fr.ringularity.infiniteg.screens;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.menus.WorkstationMenu;
import fr.ringularity.infiniteg.recipes.ItemQuantity;
import fr.ringularity.infiniteg.recipes.WorkstationRecipe;
import fr.ringularity.infiniteg.screens.widgets.InteractiveButton;
import fr.ringularity.infiniteg.screens.widgets.ScrollableArea;
import fr.ringularity.infiniteg.screens.widgets.ScrollableElement;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkstationScreen extends AbstractContainerScreen<WorkstationMenu> {

    private static final ResourceLocation GUI_BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "textures/gui/container/workstation.png");
    private static final ResourceLocation ELEMENT_BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "textures/gui/container/element.png");

    // Position absolue du GUI sur l'écran
    private int guiAbsoluteX;
    private int guiAbsoluteY;

    // Boutons externes au GUI principal
    private final List<InteractiveButton> externalButtons = new ArrayList<>();

    // Zones de défilement
    private ScrollableArea ingredientsScrollArea;
    private ScrollableArea recipesScrollArea;

    // Données temporaires pour les tests
    private final List<ItemStack> temporaryTestItems = List.of(
            new ItemStack(Items.DIAMOND),
            new ItemStack(Items.GOLD_INGOT),
            new ItemStack(Items.EMERALD),
            new ItemStack(Items.COAL),
            new ItemStack(Items.IRON_INGOT),
            new ItemStack(Items.COPPER_INGOT),
            new ItemStack(Items.GLOWSTONE)
    );

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
                    System.out.println("Fill Items button clicked");
                }
        ));
    }

    @Override
    protected void init() {
        super.init();

        // Calculer les positions absolues du GUI
        this.guiAbsoluteX = (width - imageWidth) / 2;
        this.guiAbsoluteY = (height - imageHeight) / 2;

        // Initialiser les zones de défilement avec des positions relatives au GUI
        initializeIngredientsScrollArea();
        initializeRecipesScrollArea();
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

        updateIngredientsDisplay(new ArrayList<>(List.of(
                new ItemQuantity(new ItemStack(Items.DIAMOND), 100),
                new ItemQuantity(new ItemStack(Items.EMERALD), 80),
                new ItemQuantity(new ItemStack(Items.GLOWSTONE), 150),
                new ItemQuantity(new ItemStack(Items.REDSTONE), 180),
                new ItemQuantity(new ItemStack(Items.COAL), 250),
                new ItemQuantity(new ItemStack(Items.GOLD_INGOT), 120)
        )));
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
                RenderType.GUI_TEXTURED,
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

        // Calculer les coordonnées de la souris relatives au GUI
        int mouseRelativeToGuiX = mouseX - guiAbsoluteX;
        int mouseRelativeToGuiY = mouseY - guiAbsoluteY;

        renderScrollAreas(guiGraphics, mouseX, mouseY);

        renderExternalButtons(guiGraphics, mouseRelativeToGuiX, mouseRelativeToGuiY);

        renderTooltips(guiGraphics, mouseX, mouseY);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderScrollAreas(GuiGraphics guiGraphics, int mouseAbsoluteX, int mouseAbsoluteY) {
        int ingredientsAbsoluteX = guiAbsoluteX + ingredientsScrollArea.getRelativeX();
        int ingredientsAbsoluteY = guiAbsoluteY + ingredientsScrollArea.getRelativeY();

        int recipesAbsoluteX = guiAbsoluteX + recipesScrollArea.getRelativeX();
        int recipesAbsoluteY = guiAbsoluteY + recipesScrollArea.getRelativeY();

        ingredientsScrollArea.render(
                guiGraphics, this.font,
                ingredientsAbsoluteX, ingredientsAbsoluteY,
                mouseAbsoluteX, mouseAbsoluteY
        );

        recipesScrollArea.render(
                guiGraphics, this.font,
                recipesAbsoluteX, recipesAbsoluteY,
                mouseAbsoluteX, mouseAbsoluteY
        );
    }

    private void renderExternalButtons(GuiGraphics guiGraphics, int mouseRelativeToGuiX, int mouseRelativeToGuiY) {
        for (InteractiveButton button : externalButtons) {
            button.render(
                    guiGraphics, font,
                    guiAbsoluteX, guiAbsoluteY,
                    mouseRelativeToGuiX, mouseRelativeToGuiY
            );
        }
    }

    private void renderTooltips(GuiGraphics guiGraphics, int mouseAbsoluteX, int mouseAbsoluteY) {
        ingredientsScrollArea.renderTooltip(guiGraphics, font, mouseAbsoluteX, mouseAbsoluteY);
        recipesScrollArea.renderTooltip(guiGraphics, font, mouseAbsoluteX, mouseAbsoluteY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        float textScale = 0.5f;
        //renderScaledText(guiGraphics, textScale, this.title, this.titleLabelX, this.titleLabelY, 4210752);
        //renderScaledText(guiGraphics, textScale, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752);
    }

    private void renderScaledText(GuiGraphics guiGraphics, float scale, Component text, int x, int y, int color) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0f);
        guiGraphics.drawString(
                this.font, text,
                (int) (x / scale), (int) (y / scale),
                color, false
        );
        guiGraphics.pose().popPose();
    }

    private void updateIngredientsDisplay(List<ItemQuantity> itemQuantities) {
        ingredientsScrollArea.clearElements();

        for (ItemQuantity itemQuantity : itemQuantities) {
            ScrollableElement element = new ScrollableElement(155, 20)
                    .addBackgroundTexture(ELEMENT_BACKGROUND_TEXTURE, 0, 0, 155, 20, 200, 20)
                    .addItemIcon(itemQuantity.stack, 3, 2)
                    .addText(Component.translatable(itemQuantity.stack.getItem().getDescriptionId()),
                            23, 3, 0x000000, 0.75f)
                    .addText(Component.literal(itemQuantity.quantity + "/" + 100),
                            23, 13, itemQuantity.quantity < 100 ? 0xbf2004 : 0x008c05, 0.6f);

            ingredientsScrollArea.addElement(element);
        }
    }

    private void updateRecipesDisplay(Map<WorkstationRecipe.ToolType, Integer> toolsTier) {
        recipesScrollArea.clearElements();

        Map<Integer, WorkstationRecipe> availableRecipes = WorkstationRecipe.getRecipesWithToolTiers(toolsTier);

        // Créer des éléments pour les recettes par groupes de 9
        ScrollableElement currentRecipeRow = new ScrollableElement(162, 18);
        int recipeIndex = 0;

        for (Map.Entry<Integer, WorkstationRecipe> recipeEntry : availableRecipes.entrySet()) {
            int buttonX = 18 * (recipeIndex % 9);
            int buttonY = 0;

            currentRecipeRow.addInteractiveButton(
                    buttonX, buttonY, 18, 18,
                    Component.literal(String.valueOf(recipeEntry.getValue().outputStack.getCount())),
                    recipeEntry.getValue().outputStack,
                    () -> {
                        System.out.println("Selected recipe: " + recipeEntry.getKey());
                    }
            );

            recipeIndex++;

            // Nouvelle ligne tous les 9 éléments
            if (recipeIndex % 9 == 0) {
                recipesScrollArea.addElement(currentRecipeRow);
                currentRecipeRow = new ScrollableElement(162, 18);
            }
        }

        // Ajouter la dernière ligne si elle n'est pas vide
        if (recipeIndex % 9 != 0) {
            recipesScrollArea.addElement(currentRecipeRow);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        // Convertir les coordonnées absolues en coordonnées relatives pour chaque scroll area
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
        // Gestion des boutons externes
        int mouseRelativeToGuiX = (int) (mouseX - guiAbsoluteX);
        int mouseRelativeToGuiY = (int) (mouseY - guiAbsoluteY);

        for (InteractiveButton externalButton : externalButtons) {
            if (externalButton.handleMouseClick(mouseRelativeToGuiX, mouseRelativeToGuiY, button)) {
                return true;
            }
        }

        // Gestion des scroll areas
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
        // Gestion des boutons externes
        int mouseRelativeToGuiX = (int) (mouseX - guiAbsoluteX);
        int mouseRelativeToGuiY = (int) (mouseY - guiAbsoluteY);

        for (InteractiveButton externalButton : externalButtons) {
            if (externalButton.handleMouseRelease(mouseRelativeToGuiX, mouseRelativeToGuiY, button)) {
                return true;
            }
        }

        // Gestion des scroll areas
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
