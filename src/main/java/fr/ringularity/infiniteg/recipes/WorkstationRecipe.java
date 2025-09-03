package fr.ringularity.infiniteg.recipes;

import fr.ringularity.infiniteg.abstracts.ItemQuantity;
import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.component.CompactDataComponent;
import fr.ringularity.infiniteg.component.ModDataComponents;
import fr.ringularity.infiniteg.items.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkstationRecipe {
    public static enum ToolType {
        HAMMER,
        SCREWDRIVER,
        WRENCH,
        SAW,
        DRILL,
        WIRE_STRIPPER,
        CHISEL,
        PLIERS
    }

    public final Map<ToolType, Integer> requiredTiers;
    public final ItemQuantity output;
    public final List<Ingredient> ingredients;
    public final int processTime;

    public WorkstationRecipe(Map<ToolType, Integer> requiredTiers, ItemQuantity output, List<Ingredient> ingredients, int processTime) {
        this.requiredTiers = requiredTiers;
        this.output = output;
        this.ingredients = ingredients;
        this.processTime = processTime;
    }

    public static class Ingredient {
        public ItemQuantity requiredStack;

        public Ingredient(ItemQuantity requiredStack) {
            this.requiredStack = requiredStack;
        }
    }

    public static final ArrayList<WorkstationRecipe> RECIPES = new ArrayList<>(List.of(
            new WorkstationRecipe(
                    Map.ofEntries(
                            Map.entry(ToolType.HAMMER, 0)
                    ),
                    new ItemQuantity(new ItemStack(ModItems.DENSE_IRON_PLATE.get()), BigInteger.TWO),
                    List.of(
                            new Ingredient(new ItemQuantity(new ItemStack(ModBlocks.DENSE_IRON.asItem()), BigInteger.ONE)),
                            new Ingredient(new ItemQuantity(new ItemStack(Items.DIAMOND), BigInteger.TEN))
                    ),
                    100
            ),
            new WorkstationRecipe(
                    Map.ofEntries(
                            Map.entry(ToolType.HAMMER, 0)
                    ),
                    new ItemQuantity(new ItemStack(ModItems.DENSE_IRON_ITEM.get()), BigInteger.ONE),
                    List.of(
                            new Ingredient(new ItemQuantity(new ItemStack(Blocks.ACACIA_BUTTON.asItem()), BigInteger.ONE)),
                            new Ingredient(new ItemQuantity(new ItemStack(Items.DIAMOND.asItem()), BigInteger.TEN)),
                            new Ingredient(new ItemQuantity(new ItemStack(Items.GOLD_INGOT.asItem()), BigInteger.TWO))
                    ),
                    100
            )
    ));

    static {
        final ItemStack compactedDiamond = new ItemStack(ModItems.DIAMOND_COMPACTOR.get());
        compactedDiamond.set(ModDataComponents.COMPACT_COMPONENT, new CompactDataComponent(
                81,
                Items.DIAMOND
        ));

        RECIPES.add(
                new WorkstationRecipe(
                        Map.ofEntries(
                                Map.entry(ToolType.HAMMER, 0)
                        ),
                        new ItemQuantity(new ItemStack(Items.DIAMOND_BLOCK.asItem()), BigInteger.valueOf(128700000)),
                        List.of(
                                new Ingredient(new ItemQuantity(new ItemStack(Items.DIAMOND_PICKAXE), BigInteger.valueOf(72))),
                                new Ingredient(new ItemQuantity(compactedDiamond, BigInteger.ONE)),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.DRAGON_EGG), BigInteger.TWO)),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_PEARL), BigInteger.valueOf(100))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.WATER_BUCKET), BigInteger.valueOf(50))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.LAVA_BUCKET), BigInteger.valueOf(25))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.EMERALD), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.GOLD_INGOT), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.FISHING_ROD), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_DRAGON_SPAWN_EGG), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ANDESITE), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.REDSTONE), BigInteger.valueOf(1278))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.EMERALD), BigInteger.valueOf(1278))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.COBBLESTONE), BigInteger.valueOf(1278))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.GRASS_BLOCK), BigInteger.valueOf(235))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.BLAZE_ROD), new BigInteger("4451235241569999999102510214523210245874541232154000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.GRAVEL), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_PEARL), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.WITHER_SKELETON_SKULL), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ACACIA_LOG), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.CHAINMAIL_BOOTS), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.DIAMOND_PICKAXE), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(compactedDiamond, BigInteger.ONE)),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.DRAGON_EGG), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_PEARL), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.WATER_BUCKET), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.LAVA_BUCKET), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.EMERALD), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.GOLD_INGOT), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.FISHING_ROD), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_DRAGON_SPAWN_EGG), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ANDESITE), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.REDSTONE), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.EMERALD), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.COBBLESTONE), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.GRASS_BLOCK), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.BLAZE_ROD), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.GRAVEL), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_PEARL), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.CREEPER_HEAD), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.STONE_SWORD), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.TROPICAL_FISH), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.PUFFERFISH), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.BEEF), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.HONEY_BOTTLE), BigInteger.valueOf(56230000))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.POTATO), BigInteger.valueOf(1000))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ARROW), BigInteger.valueOf(75421))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ACACIA_FENCE), BigInteger.valueOf(5421563))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.STRIPPED_BAMBOO_BLOCK), BigInteger.valueOf(78))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.WITHER_SKELETON_SKULL), BigInteger.valueOf(58))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ACACIA_LOG), BigInteger.valueOf(43))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.CHAINMAIL_BOOTS), BigInteger.valueOf(36))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.DIAMOND_PICKAXE), BigInteger.valueOf(72))),
                                new Ingredient(new ItemQuantity(compactedDiamond, BigInteger.ONE)),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.DRAGON_EGG), BigInteger.TWO)),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_PEARL), BigInteger.valueOf(100))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.WATER_BUCKET), BigInteger.valueOf(50))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.LAVA_BUCKET), BigInteger.valueOf(25))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.EMERALD), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.GOLD_INGOT), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.FISHING_ROD), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_DRAGON_SPAWN_EGG), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ANDESITE), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.REDSTONE), BigInteger.valueOf(1278))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.EMERALD), BigInteger.valueOf(1278))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.COBBLESTONE), BigInteger.valueOf(1278))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.GRASS_BLOCK), BigInteger.valueOf(235))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.BLAZE_ROD), new BigInteger("4451235241569999999102510214523210245874541232154000000000000"))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.GRAVEL), BigInteger.valueOf(10))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_PEARL), BigInteger.valueOf(12))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.CREEPER_HEAD), BigInteger.valueOf(1))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.STONE_SWORD), BigInteger.valueOf(58))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.TROPICAL_FISH), BigInteger.valueOf(65))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.PUFFERFISH), BigInteger.valueOf(120))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.BEEF), BigInteger.valueOf(785415))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.HONEY_BOTTLE), BigInteger.valueOf(56230000))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.POTATO), BigInteger.valueOf(1000))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ARROW), BigInteger.valueOf(75421))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ACACIA_FENCE), BigInteger.valueOf(5421563))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.STRIPPED_BAMBOO_BLOCK), BigInteger.valueOf(78))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.WITHER_SKELETON_SKULL), BigInteger.valueOf(58))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ACACIA_LOG), BigInteger.valueOf(43))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.CHAINMAIL_BOOTS), BigInteger.valueOf(36))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.DIAMOND_PICKAXE), BigInteger.valueOf(72))),
                                new Ingredient(new ItemQuantity(compactedDiamond, BigInteger.ONE)),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.DRAGON_EGG), BigInteger.TWO)),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_PEARL), BigInteger.valueOf(100))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.WATER_BUCKET), BigInteger.valueOf(50))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.LAVA_BUCKET), BigInteger.valueOf(25))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.EMERALD), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.GOLD_INGOT), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.FISHING_ROD), BigInteger.valueOf(5))),
                                new Ingredient(new ItemQuantity(new ItemStack(Items.ENDER_DRAGON_SPAWN_EGG), BigInteger.valueOf(5)))
                        ),
                        100
                )
        );
    }

    public static Map<Integer, WorkstationRecipe> getRecipesWithToolTiers(Map<ToolType, Integer> toolsTier) {
        final Map<Integer, WorkstationRecipe> recipes = new HashMap<>();
        for (int i = 0; i < RECIPES.size(); ++i) {
            final WorkstationRecipe wr = RECIPES.get(i);
            boolean missingItemTier = false;
            for (final Map.Entry<ToolType, Integer> toolTier : wr.requiredTiers.entrySet()) {
                if (!toolsTier.containsKey(toolTier.getKey()) || toolsTier.get(toolTier.getKey()) < toolTier.getValue()) {
                    missingItemTier = true;
                    break;
                }
            }
            if (!missingItemTier)
                recipes.put(i, new WorkstationRecipe(
                        new HashMap<>(wr.requiredTiers),
                        new ItemQuantity(wr.output.stack, wr.output.quantity),
                        new ArrayList<>(wr.ingredients),
                        wr.processTime
                ));
        }

        return recipes;
    }
}
