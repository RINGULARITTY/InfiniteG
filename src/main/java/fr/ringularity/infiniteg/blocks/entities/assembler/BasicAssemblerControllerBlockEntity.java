package fr.ringularity.infiniteg.blocks.entities.assembler;

import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BasicAssemblerControllerBlockEntity extends AbstractAssemblerControllerBlockEntity {
    public BasicAssemblerControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BASIC_ASSEMBLER_CONTROLLER_BE.get(), pos, state);
    }
}
