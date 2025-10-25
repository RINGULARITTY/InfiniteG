package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.abstracts.ItemQuantity;
import fr.ringularity.infiniteg.data.codec.BigIntegerCodecs;
import fr.ringularity.infiniteg.network.dto.DTOIndexItemQuantity;
import fr.ringularity.infiniteg.network.dto.DTOItemQuantity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractIGBEContainer extends BlockEntity {
    private static final String STORED_ITEMS_SIZE_KEY = "ig_stored_items_size";
    private static final String STORED_ITEMS_INDEXES_KEY = "ig_stored_items_indexes";
    private static final String STORED_ITEMS_STACKS_KEY = "ig_stored_items_stacks";
    private static final String STORED_ITEMS_QUANTITIES_KEY = "ig_stored_items_quantities";

    public HashMap<Integer, ItemQuantity> storedItems = new HashMap<>();

    public AbstractIGBEContainer(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        storedItems.clear();

        int storedItemsAmount = input.getIntOr(STORED_ITEMS_SIZE_KEY, 0);
        for (int i = 0; i < storedItemsAmount; ++i) {
            int slotIndex = input.getIntOr(STORED_ITEMS_INDEXES_KEY + "_" + i, -1);
            if (slotIndex != -1) {
                ItemStack stack = input.read(STORED_ITEMS_STACKS_KEY + "_" + i, ItemStack.CODEC).orElse(ItemStack.EMPTY);
                BigInteger quantity = input.read(STORED_ITEMS_QUANTITIES_KEY + "_" + i, BigIntegerCodecs.CODEC).orElse(BigInteger.ZERO);

                storedItems.put(slotIndex, new ItemQuantity(stack, quantity));
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.putInt(STORED_ITEMS_SIZE_KEY, storedItems.size());
        int i = 0;
        for (int slotIndex : storedItems.keySet()) {
            output.putInt(STORED_ITEMS_INDEXES_KEY + "_" + i, slotIndex);

            ItemQuantity iq = storedItems.get(slotIndex);
            output.store(STORED_ITEMS_STACKS_KEY + "_" + i, ItemStack.CODEC, iq.stack);
            output.store(STORED_ITEMS_QUANTITIES_KEY + "_" + i, BigIntegerCodecs.CODEC, iq.quantity);

            i++;
        }
    }

    public List<DTOIndexItemQuantity> getItemsPacket() {
        List<DTOIndexItemQuantity> items = new ArrayList<>();

        for (int slotIndex : storedItems.keySet()) {
            ItemQuantity iq = storedItems.get(slotIndex);
            items.add(new DTOIndexItemQuantity(
                    slotIndex, new DTOItemQuantity(iq.stack, iq.quantity)
            ));
        }

        return items;
    }
}
