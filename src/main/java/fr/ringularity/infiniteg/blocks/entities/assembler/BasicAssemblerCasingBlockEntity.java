package fr.ringularity.infiniteg.blocks.entities.assembler;

import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BasicAssemblerCasingBlockEntity extends AbstractAssemblerCasingBlockEntity {

    public BasicAssemblerCasingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BASIC_ASSEMBLER_CASING_BE.get(), pos, state);
    }
}
