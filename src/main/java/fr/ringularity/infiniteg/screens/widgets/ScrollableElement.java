package fr.ringularity.infiniteg.screens.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Élément qui peut être affiché dans une ScrollableArea.
 * Contient différents types de contenu : texte, icônes, textures et boutons.
 */
public class ScrollableElement {

    // Listes de contenu
    private final List<TextElement> textElements = new ArrayList<>();
    private final List<ItemIconElement> itemIconElements = new ArrayList<>();
    private final List<BackgroundTextureElement> backgroundTextureElements = new ArrayList<>();
    private final List<InteractiveButton> interactiveButtons = new ArrayList<>();

    // Dimensions
    private final int width;
    private final int height;

    /**
     * Crée un nouvel élément scrollable
     * @param width Largeur de l'élément
     * @param height Hauteur de l'élément
     */
    public ScrollableElement(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // ===================== MÉTHODES D'AJOUT DE CONTENU =====================

    /**
     * Ajoute un texte à l'élément
     */
    public ScrollableElement addText(Component text, int x, int y, int color) {
        return addText(text, x, y, color, 1.0f);
    }

    /**
     * Ajoute un texte avec échelle à l'élément
     */
    public ScrollableElement addText(Component text, int x, int y, int color, float scale) {
        textElements.add(new TextElement(text, x, y, color, scale));
        return this;
    }

    /**
     * Ajoute une icône d'item à l'élément
     */
    public ScrollableElement addItemIcon(ItemStack itemStack, int x, int y) {
        itemIconElements.add(new ItemIconElement(itemStack, x, y));
        return this;
    }

    /**
     * Ajoute une texture d'arrière-plan à l'élément
     */
    public ScrollableElement addBackgroundTexture(ResourceLocation texture, int x, int y, int width, int height,
                                                  int textureWidth, int textureHeight) {
        backgroundTextureElements.add(new BackgroundTextureElement(texture, x, y, width, height, textureWidth, textureHeight));
        return this;
    }

    /**
     * Ajoute un bouton interactif à l'élément
     */
    public ScrollableElement addInteractiveButton(int x, int y, int width, int height,
                                                  Component text, ItemStack stack, Runnable action) {
        interactiveButtons.add(new InteractiveButton(x, y, width, height, text, stack, action));
        return this;
    }

    // ===================== RENDU =====================

    /**
     * Rend l'élément et retourne l'ItemStack à afficher en tooltip si applicable
     *
     * @param guiGraphics Contexte de rendu
     * @param font Police à utiliser
     * @param elementAbsoluteX Position X absolue de l'élément sur l'écran
     * @param elementAbsoluteY Position Y absolue de l'élément sur l'écran
     * @param mouseRelativeToElementX Position X de la souris relative à l'élément
     * @param mouseRelativeToElementY Position Y de la souris relative à l'élément
     * @return ItemStack pour tooltip ou null
     */
    public ItemStack render(GuiGraphics guiGraphics, Font font,
                            int elementAbsoluteX, int elementAbsoluteY,
                            int mouseRelativeToElementX, int mouseRelativeToElementY) {

        ItemStack tooltipItem = null;

        // 1. Rendre les textures d'arrière-plan en premier
        for (BackgroundTextureElement texture : backgroundTextureElements) {
            renderBackgroundTexture(guiGraphics, texture, elementAbsoluteX, elementAbsoluteY);
        }

        // 2. Rendre les icônes d'items avec détection de survol
        for (ItemIconElement icon : itemIconElements) {
            renderItemIcon(guiGraphics, icon, elementAbsoluteX, elementAbsoluteY);

            // Vérifier le survol pour le tooltip
            if (isMouseOverItemIcon(icon, mouseRelativeToElementX, mouseRelativeToElementY)) {
                tooltipItem = icon.itemStack;
            }
        }

        // 3. Rendre le texte par-dessus
        for (TextElement text : textElements) {
            renderText(guiGraphics, font, text, elementAbsoluteX, elementAbsoluteY);
        }

        // 4. Rendre les boutons en dernier
        for (InteractiveButton button : interactiveButtons) {
            ItemStack buttonTooltipItem = button.render(
                    guiGraphics, font,
                    elementAbsoluteX, elementAbsoluteY,
                    mouseRelativeToElementX, mouseRelativeToElementY
            );

            if (buttonTooltipItem != null) {
                tooltipItem = buttonTooltipItem;
            }
        }

        return tooltipItem;
    }

    // ===================== MÉTHODES DE RENDU INTERNES =====================

    private void renderBackgroundTexture(GuiGraphics guiGraphics, BackgroundTextureElement texture,
                                         int elementAbsoluteX, int elementAbsoluteY) {
        guiGraphics.blit(
                RenderType.GUI_TEXTURED,
                texture.resourceLocation,
                elementAbsoluteX + texture.x,
                elementAbsoluteY + texture.y,
                0, 0,
                texture.width, texture.height,
                texture.textureWidth, texture.textureHeight
        );
    }

    private void renderItemIcon(GuiGraphics guiGraphics, ItemIconElement icon,
                                int elementAbsoluteX, int elementAbsoluteY) {
        guiGraphics.renderItem(
                icon.itemStack,
                elementAbsoluteX + icon.x,
                elementAbsoluteY + icon.y
        );
    }

    private void renderText(GuiGraphics guiGraphics, Font font, TextElement text,
                            int elementAbsoluteX, int elementAbsoluteY) {
        if (text.scale != 1.0f) {
            // Texte avec échelle
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(text.scale, text.scale, 1.0f);
            guiGraphics.drawString(
                    font,
                    text.component,
                    (int)((elementAbsoluteX + text.x) / text.scale),
                    (int)((elementAbsoluteY + text.y) / text.scale),
                    text.color,
                    false
            );
            guiGraphics.pose().popPose();
        } else {
            // Texte normal
            guiGraphics.drawString(
                    font,
                    text.component,
                    elementAbsoluteX + text.x,
                    elementAbsoluteY + text.y,
                    text.color,
                    false
            );
        }
    }

    private boolean isMouseOverItemIcon(ItemIconElement icon, int mouseRelativeX, int mouseRelativeY) {
        return mouseRelativeX >= icon.x &&
                mouseRelativeY >= icon.y &&
                mouseRelativeX <= icon.x + 16 &&
                mouseRelativeY <= icon.y + 16;
    }

    // ===================== ACCESSEURS =====================

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public List<ItemIconElement> getItemIcons() {
        return itemIconElements;
    }

    public List<InteractiveButton> getInteractiveButtons() {
        return interactiveButtons;
    }

    // ===================== CLASSES INTERNES =====================

    /**
     * Représente un élément de texte
     */
    public static class TextElement {
        final Component component;
        final int x, y, color;
        final float scale;

        TextElement(Component component, int x, int y, int color, float scale) {
            this.component = component;
            this.x = x;
            this.y = y;
            this.color = color;
            this.scale = scale;
        }
    }

    /**
     * Représente une icône d'item
     */
    public static class ItemIconElement {
        final ItemStack itemStack;
        final int x, y;

        ItemIconElement(ItemStack itemStack, int x, int y) {
            this.itemStack = itemStack;
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Représente une texture d'arrière-plan
     */
    public static class BackgroundTextureElement {
        final ResourceLocation resourceLocation;
        final int x, y, width, height, textureWidth, textureHeight;

        BackgroundTextureElement(ResourceLocation resourceLocation, int x, int y, int width, int height,
                                 int textureWidth, int textureHeight) {
            this.resourceLocation = resourceLocation;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }
    }
}
