package fr.ringularity.infiniteg.screens;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.abstracts.ItemQuantity;
import fr.ringularity.infiniteg.blocks.entities.WorkstationBlockEntity;
import fr.ringularity.infiniteg.format.BigIntegerFormat;
import fr.ringularity.infiniteg.menus.BasicAssemblerControllerMenu;
import fr.ringularity.infiniteg.menus.IGSlot;
import fr.ringularity.infiniteg.network.IntPayloadToServer;
import fr.ringularity.infiniteg.network.UpdateItemQuantitiesToClient;
import fr.ringularity.infiniteg.recipes.WorkstationRecipe;
import fr.ringularity.infiniteg.screens.widgets.Primitives;
import fr.ringularity.infiniteg.screens.widgets.ScrollableArea;
import fr.ringularity.infiniteg.screens.widgets.ScrollableElement;
import fr.ringularity.infiniteg.utils.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BasicAssemblerScreen extends AbstractContainerScreen<BasicAssemblerControllerMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID,"textures/gui/growth_chamber/growth_chamber_gui.png");

    private int guiAbsoluteX;
    private int guiAbsoluteY;
    private ScrollableArea ingredientsScrollArea;
    private final Map<Integer, IGSlot> slots;

    public BasicAssemblerScreen(BasicAssemblerControllerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        slots = menu.getSlots();
    }

    @Override
    protected void init() {
        super.init();
        this.guiAbsoluteX = (width - imageWidth) / 2;
        this.guiAbsoluteY = (height - imageHeight) / 2;

        initializeIngredientsScrollArea();
        updateIngredientsDisplay(slots);
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

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 512, 256);

        ScreenTools.renderScaledText(
            guiGraphics, Minecraft.getInstance().font, new Primitives.TextElement(Component.literal("Test"), x + 10, y + 10, 0, 1)
        );
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        renderScrollArea(pGuiGraphics, pMouseX, pMouseY);
        ingredientsScrollArea.renderTooltip(this, pGuiGraphics, pMouseX, pMouseY);
        pGuiGraphics.renderDeferredTooltip();
    }

    private void renderScrollArea(GuiGraphics guiGraphics, int mouseAbsoluteX, int mouseAbsoluteY) {
        int ingredientsAbsoluteX = guiAbsoluteX + ingredientsScrollArea.getRelativeX();
        int ingredientsAbsoluteY = guiAbsoluteY + ingredientsScrollArea.getRelativeY();

        ingredientsScrollArea.render(guiGraphics, this.font, ingredientsAbsoluteX, ingredientsAbsoluteY, mouseAbsoluteX, mouseAbsoluteY);
    }

    public void updateIngredientsDisplay(Map<Integer, IGSlot> slots) {
        ingredientsScrollArea.clearElements();

        int recipeIndex = 0;
        ScrollableElement currentRow = new ScrollableElement(162, 12);
        for (IGSlot slot : Maps.mapToIndexedList(slots)) {
            if (slot == null) continue;

            int buttonX = 12 * (recipeIndex % 9);
            int buttonY = 0;

            final ItemQuantity iq = slot.slotStack.getItemQuantity();
            currentRow.addInteractiveButton(
                    buttonX, buttonY, 12, 12,
                    Component.literal(BigIntegerFormat.format(iq.quantity)),
                    iq.stack,
                    () -> {}
            );

            recipeIndex++;
            if (recipeIndex % 9 == 0) {
                ingredientsScrollArea.addElement(currentRow);
                currentRow = new ScrollableElement(162, 12);
            }
        }

        if (recipeIndex % 9 != 0) {
            ingredientsScrollArea.addElement(currentRow);
        }
    }
}
