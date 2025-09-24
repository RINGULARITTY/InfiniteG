package fr.ringularity.infiniteg.blocks.entities.assembler;

import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EliteAssemblerCasingBlockEntity extends AbstractAssemblerCasingBlockEntity {

    public EliteAssemblerCasingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELITE_ASSEMBLER_CASING_BE.get(), pos, state);
    }
}
