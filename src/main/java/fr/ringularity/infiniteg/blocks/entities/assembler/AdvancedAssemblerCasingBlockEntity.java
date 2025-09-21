package fr.ringularity.infiniteg.blocks.entities.assembler;

import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedAssemblerCasingBlockEntity extends AbstractAssemblerCasingBlockEntity {

    public AdvancedAssemblerCasingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_ASSEMBLER_CASING_BE.get(), pos, state);
    }
}
