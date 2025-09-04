package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.abstracts.ItemQuantity;
import fr.ringularity.infiniteg.menus.WorkstationMenu;
import fr.ringularity.infiniteg.network.UpdateItemQuantitiesToClient;
import fr.ringularity.infiniteg.data.codec.BigIntegerCodecs;
import fr.ringularity.infiniteg.recipes.WorkstationRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkstationBlockEntity extends BlockEntity implements MenuProvider {
    public static final int
        DATA_PROGRESS = 0,
        DATA_MAX_PROCESS = 1;

    public static final int CMD_NONE = -1, CMD_PUSH = -2, CMD_QUERY = -3;

    private List<ItemQuantity> storedItems = new ArrayList<>();
    public int selectedRecipeId = CMD_NONE;
    private WorkstationRecipe selectedRecipe = null;

    private int progress = 0;
    private int maxProgress = 100;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int i) {
            return switch (i) {
                case DATA_PROGRESS -> WorkstationBlockEntity.this.progress;
                case DATA_MAX_PROCESS -> WorkstationBlockEntity.this.maxProgress;
                default -> 0;
            };
        }
        @Override
        public void set(int i, int value) {
            switch (i) {
                case DATA_PROGRESS -> WorkstationBlockEntity.this.progress = value;
                case DATA_MAX_PROCESS -> WorkstationBlockEntity.this.maxProgress = value;
            }
        }
        @Override
        public int getCount() {
            return 2;
        }
    };

    public WorkstationBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.WORKSTATION_BE.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.infiniteg.workstation");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
        return new WorkstationMenu(windowId, inv, this, this.data);
    }

    public List<UpdateItemQuantitiesToClient.RecipeItemQuantityPayload> updateRecipeItems(Player player, int recipeCmd) {
        if (recipeCmd == CMD_NONE) {
            drops(this.worldPosition); // keep for explicit deselection
            updateRecipeInternal(CMD_NONE);
            markAndSync();
            return Collections.emptyList();
        }

        if (recipeCmd == CMD_QUERY) {
            // No side effects, just send current tallies back
            return buildPayloadsForCurrentRecipe();
        }

        if (recipeCmd >= 0) {
            if (recipeCmd != this.selectedRecipeId) {
                // Only drop when switching to a different recipe
                drops(this.worldPosition);
                updateRecipeInternal(recipeCmd);
                markAndSync();
                // Build zero-current payloads for the new recipe (optional)
                List<UpdateItemQuantitiesToClient.RecipeItemQuantityPayload> payloads = new ArrayList<>();
                for (WorkstationRecipe.Ingredient ing : selectedRecipe.ingredients) {
                    payloads.add(new UpdateItemQuantitiesToClient.RecipeItemQuantityPayload(
                            ing.requiredStack.stack.copy(), BigInteger.ZERO, ing.requiredStack.quantity
                    ));
                }
                return payloads;
            } else {
                // Same recipe as before: do not drop; just return current tallies
                return buildPayloadsForCurrentRecipe();
            }
        }

        if (recipeCmd == CMD_PUSH) {
            if (this.selectedRecipe == null) {
                return Collections.emptyList();
            }

            pushFromPlayerInventory(player);
            markAndSync();
            return buildPayloadsForCurrentRecipe();
        }

        return Collections.emptyList();
    }

    private void updateRecipeInternal(int recipeId) {
        this.selectedRecipeId = recipeId;
        if (recipeId >= 0) {
            this.selectedRecipe = WorkstationRecipe.RECIPES.get(recipeId);
        } else {
            this.selectedRecipe = null;
        }
    }

    private List<UpdateItemQuantitiesToClient.RecipeItemQuantityPayload> buildPayloadsForCurrentRecipe() {
        if (selectedRecipe == null) {
            return Collections.emptyList();
        }
        List<UpdateItemQuantitiesToClient.RecipeItemQuantityPayload> payloads = new ArrayList<>();
        for (WorkstationRecipe.Ingredient ing : selectedRecipe.ingredients) {
            ItemStack needed = ing.requiredStack.stack;
            BigInteger required = ing.requiredStack.quantity;
            BigInteger current = getCurrentAmountFor(needed);
            payloads.add(new UpdateItemQuantitiesToClient.RecipeItemQuantityPayload(
                    needed.copy(), current, required
            ));
        }
        return payloads;
    }

    private BigInteger getCurrentAmountFor(ItemStack pattern) {
        BigInteger total = BigInteger.ZERO;
        for (ItemQuantity iq : storedItems) {
            if (ItemStack.isSameItemSameComponents(iq.stack, pattern)) {
                total = total.add(iq.quantity);
            }
        }
        return total;
    }

    private void addToStored(ItemStack pattern, BigInteger add) {
        if (add.compareTo(BigInteger.ZERO) <= 0) return;
        // Find matching entry by same item+components
        for (ItemQuantity iq : storedItems) {
            if (ItemStack.isSameItemSameComponents(iq.stack, pattern)) {
                iq.quantity = iq.quantity.add(add);
                return;
            }
        }
        // Create new entry with a canonical copy of the pattern (count ignored)
        storedItems.add(new ItemQuantity(pattern.copy(), add));
    }

    private void pushFromPlayerInventory(Player player) {
        if (selectedRecipe == null) return;

        Inventory inv = player.getInventory();
        // For each ingredient, compute shortage and try to gather from inventory
        for (WorkstationRecipe.Ingredient ing : selectedRecipe.ingredients) {
            ItemStack needed = ing.requiredStack.stack;
            BigInteger required = ing.requiredStack.quantity;
            BigInteger have = getCurrentAmountFor(needed);
            BigInteger shortage = required.subtract(have);
            if (shortage.compareTo(BigInteger.ZERO) <= 0) {
                continue;
            }

            // Sweep the entire inventory
            for (int slot = 0; slot < inv.getContainerSize(); slot++) {
                if (shortage.compareTo(BigInteger.ZERO) <= 0) break;
                ItemStack slotStack = inv.getItem(slot);
                if (slotStack.isEmpty()) continue;

                if (ItemStack.isSameItemSameComponents(slotStack, needed)) {
                    int available = slotStack.getCount();
                    int toTake = Math.min(available, shortage.min(BigInteger.valueOf(Integer.MAX_VALUE)).intValue());
                    if (toTake > 0) {
                        slotStack.shrink(toTake);
                        inv.setChanged();
                        addToStored(needed, BigInteger.valueOf(toTake));
                        shortage = shortage.subtract(BigInteger.valueOf(toTake));
                    }
                }
            }
        }
    }

    public void drops(BlockPos pos) {
        if (storedItems == null || this.level == null) return;

        for (ItemQuantity iq : storedItems) {
            BigInteger remainQuantity = iq.quantity; // no need to copy string
            final int maxStackSize = iq.stack.getMaxStackSize();

            while (remainQuantity.compareTo(BigInteger.valueOf(maxStackSize)) >= 0) {
                ItemStack stack = iq.stack.copy();
                stack.setCount(maxStackSize);
                Containers.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), stack);
                remainQuantity = remainQuantity.subtract(BigInteger.valueOf(maxStackSize));
            }
            if (remainQuantity.compareTo(BigInteger.ZERO) > 0) {
                ItemStack stack = iq.stack.copy();
                stack.setCount(remainQuantity.intValue());
                Containers.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
        storedItems.clear();
    }

    private void markAndSync() {
        setChanged();
        if (this.level != null) {
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }


    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        drops(pos);
        super.preRemoveSideEffects(pos, state);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        this.storedItems = new ArrayList<>();

        ValueInput.TypedInputList<ItemStackWithSlot> typedInputList = input.listOrEmpty("workstation.stored_items", ItemStackWithSlot.CODEC);

        for (ItemStackWithSlot slotEntry : typedInputList) {
            int slot = slotEntry.slot();
            ItemStack stack = slotEntry.stack();

            BigInteger quantity = input.read("workstation.stored_items_quantity_" + slot, BigIntegerCodecs.BIG_INT_CODEC).orElse(BigInteger.ZERO);
            this.storedItems.add(new ItemQuantity(stack, quantity));
        }

        this.selectedRecipeId = input.getIntOr("workstation.selected_recipe_id", -1);
        updateRecipeInternal(this.selectedRecipeId);

        this.progress = input.getIntOr("workstation.progress", 0);
        this.maxProgress = input.getIntOr("workstation.max_progress", 0);
    }


    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput.TypedOutputList<ItemStackWithSlot> typedoutputlist = output.list("workstation.stored_items", ItemStackWithSlot.CODEC);

        output.putInt("workstation.stored_items_size", storedItems.size());

        for (int i = 0; i < storedItems.size(); ++i) {
            final ItemQuantity iq = storedItems.get(i);

            if (iq.quantity.compareTo(BigInteger.ZERO) == 0)
                continue;

            typedoutputlist.add(new ItemStackWithSlot(i, iq.stack));
            output.store("workstation.stored_items_quantity_" + i, BigIntegerCodecs.BIG_INT_CODEC, iq.quantity);
        }

        output.putInt("workstation.selected_recipe_id", selectedRecipeId);

        output.putInt("workstation.progress", progress);
        output.putInt("workstation.max_progress", maxProgress);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        //setChanged(level, blockPos, blockState);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
