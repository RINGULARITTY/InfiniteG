package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.component.CompactDataComponent;
import fr.ringularity.infiniteg.component.ModDataComponents;
import fr.ringularity.infiniteg.items.ModItems;
import fr.ringularity.infiniteg.menus.WorkstationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class WorkstationBlockEntity extends BlockEntity implements MenuProvider {
    public static final int OUTPUT_SLOT = 6;

    public final ItemStackHandler itemHandler = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    public WorkstationBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.WORKSTATION_BE.get(), pos, blockState);

        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> WorkstationBlockEntity.this.progress;
                    case 1 -> WorkstationBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0: WorkstationBlockEntity.this.progress = value;
                    case 1: WorkstationBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.infiniteg.workstation");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new WorkstationMenu(i, inventory, this, this.data);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        drops();
        super.preRemoveSideEffects(pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("workstation.progress", progress);
        pTag.putInt("workstation.max_progress", maxProgress);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory").get());
        progress = pTag.getInt("workstation.progress").get();
        maxProgress = pTag.getInt("workstation.max_progress").get();
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void craftItem() {
        final ItemStack compactItem = itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (compactItem.isEmpty()) {
            for (int i = 0; i < OUTPUT_SLOT; ++i) {
                ItemStack s = itemHandler.getStackInSlot(i);
                if (!s.isEmpty() && s.getItem() == Items.DIAMOND) {
                    itemHandler.extractItem(i, 1, false);

                    ItemStack output = new ItemStack(ModItems.DIAMOND_COMPACTOR.get(), 1);
                    output.set(ModDataComponents.COMPACT_COMPONENT, new CompactDataComponent(
                            1,
                            Items.DIAMOND
                    ));

                    itemHandler.setStackInSlot(OUTPUT_SLOT, output);
                }
            }

            return;
        }

        CompactDataComponent cdc = compactItem.get(ModDataComponents.COMPACT_COMPONENT);
        if (cdc == null) {
            return;
        }

        for (int i = 0; i < OUTPUT_SLOT; ++i)
            itemHandler.extractItem(i, 1, false);

        itemHandler.getStackInSlot(OUTPUT_SLOT).set(ModDataComponents.COMPACT_COMPONENT, new CompactDataComponent(
                cdc.quantity() + 6,
                cdc.item()
        ));
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 100;
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        final ItemStack compactItem = itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (compactItem.isEmpty()) {
            for (int i = 0; i < OUTPUT_SLOT; ++i) {
                ItemStack s = itemHandler.getStackInSlot(i);
                if (!s.isEmpty() && s.getItem() == Items.DIAMOND)
                    return true;
            }
            return false;
        }

        CompactDataComponent cdc = compactItem.get(ModDataComponents.COMPACT_COMPONENT);
        if (cdc == null) {
            return false;
        }

        for (int i = 0; i < OUTPUT_SLOT; ++i) {
            final ItemStack s = itemHandler.getStackInSlot(i);
            if (s.isEmpty() || s.getItem() != cdc.item()) {
                return false;
            }
        }

        return true;
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
