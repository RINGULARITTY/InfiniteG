package fr.ringularity.infiniteg.blocks.entities.assembler;

import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ImprovedAssemblerEnergyCasingBlockEntity extends AbstractAssemblerCasingBlockEntity {

    public ImprovedAssemblerEnergyCasingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IMPROVED_ASSEMBLER_ENERGY_CASING_BE.get(), pos, state);
    }
}
