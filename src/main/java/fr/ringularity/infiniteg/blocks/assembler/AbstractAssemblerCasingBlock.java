package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.abstracts.StructureUpgrade;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAssemblerCasingBlock extends Block implements EntityBlock {
    private final MachineTier tier;
    private final StructureUpgrade recipeType;

    protected AbstractAssemblerCasingBlock(MachineTier tier, StructureUpgrade recipeType, Properties props) {
        super(props);
        this.tier = tier;
        this.recipeType = recipeType;
    }

    public MachineTier getTier() { return tier; }

    public StructureUpgrade getRecipeType() { return recipeType; }

    @Override
    public PushReaction getPistonPushReaction(@NotNull BlockState state) {
        return PushReaction.BLOCK;
    }
}
