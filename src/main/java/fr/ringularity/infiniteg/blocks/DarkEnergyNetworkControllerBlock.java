package fr.ringularity.infiniteg.blocks;

import com.mojang.serialization.MapCodec;
import fr.ringularity.infiniteg.blocks.entities.DarkEnergyNetworkControllerBlockEntity;
import fr.ringularity.infiniteg.capabilities.DarkChat;
import fr.ringularity.infiniteg.capabilities.DarkEnergyNetworkNodeRef;
import fr.ringularity.infiniteg.capabilities.DarkEnergyNetworks;
import fr.ringularity.infiniteg.capabilities.DarkEnergyNetworksData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// DarkEnergyNetworkBlock.java
public class DarkEnergyNetworkControllerBlock extends BaseEntityBlock {
    public static final MapCodec<DarkEnergyNetworkControllerBlock> CODEC = simpleCodec(DarkEnergyNetworkControllerBlock::new);

    public DarkEnergyNetworkControllerBlock(Properties props) {
        super(props);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DarkEnergyNetworkControllerBlockEntity(pos, state);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (level.isClientSide) return;

        ServerLevel sl = (ServerLevel) level;
        BlockEntity be = sl.getBlockEntity(pos);
        if (be instanceof DarkEnergyNetworkControllerBlockEntity networkBE) {
            if (networkBE.getNetworkId() == null) {
                // Créer le réseau et enregistrer ce nœud
                DarkEnergyNetworksData data = DarkEnergyNetworks.get(sl);
                UUID id = data.createEmptyNetwork();
                DarkEnergyNetworkNodeRef node = new DarkEnergyNetworkNodeRef(sl.dimension(), pos);
                data.addNodeTo(id, node);
                networkBE.setNetworkId(id);

                // Optionnel: invalider des capabilities si d’autres blocs s’abonnent à ce nœud via caches
                // ((ILevelExtension) sl).invalidateCapabilities(pos);
            }
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ServerLevel sl = (ServerLevel) level;
        var be = sl.getBlockEntity(pos);
        if (!(be instanceof DarkEnergyNetworkControllerBlockEntity networkBE)) {
            return InteractionResult.CONSUME;
        }
        UUID id = networkBE.getNetworkId();
        if (id == null) {
            ((ServerPlayer) player).sendSystemMessage(Component.literal("DarkNet: aucun réseau associé"));
            return InteractionResult.CONSUME;
        }
        var data = DarkEnergyNetworks.get(sl);
        var rec = data.networksView().get(id);
        if (rec == null) {
            ((ServerPlayer) player).sendSystemMessage(Component.literal("DarkNet: réseau introuvable: " + id));
            return InteractionResult.CONSUME;
        }

        Component msg = DarkChat.formatNetworkSummary(rec);
        ((ServerPlayer) player).sendSystemMessage(msg);
        return InteractionResult.CONSUME;
    }
}

