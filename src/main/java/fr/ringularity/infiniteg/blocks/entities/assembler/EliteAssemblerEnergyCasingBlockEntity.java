package fr.ringularity.infiniteg.blocks.entities.assembler;

import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EliteAssemblerEnergyCasingBlockEntity extends AbstractAssemblerCasingBlockEntity {

    public EliteAssemblerEnergyCasingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELITE_ASSEMBLER_ENERGY_CASING_BE.get(), pos, state);
    }
}
