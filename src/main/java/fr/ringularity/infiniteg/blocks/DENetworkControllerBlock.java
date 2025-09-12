package fr.ringularity.infiniteg.blocks;

import com.mojang.serialization.MapCodec;
import fr.ringularity.infiniteg.blocks.entities.DENetworkControllerBlockEntity;
import fr.ringularity.infiniteg.capabilities.de.DEChat;
import fr.ringularity.infiniteg.capabilities.de.DENetworkNodeRef;
import fr.ringularity.infiniteg.capabilities.de.DENetworks;
import fr.ringularity.infiniteg.capabilities.de.DENetworksData;
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
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DENetworkControllerBlock extends DEMachineBlock {
    public static final MapCodec<DENetworkControllerBlock> CODEC = simpleCodec(DENetworkControllerBlock::new);

    public DENetworkControllerBlock(Properties props) {
        super(props);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DENetworkControllerBlockEntity(pos, state);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (level.isClientSide) return;

        ServerLevel sl = (ServerLevel) level;
        BlockEntity be = sl.getBlockEntity(pos);
        if (be instanceof DENetworkControllerBlockEntity networkBE) {
            if (networkBE.getDENetworkId() == null) {
                // Créer le réseau et enregistrer ce nœud
                DENetworksData data = DENetworks.get(sl);
                UUID id = data.createEmptyNetwork();
                DENetworkNodeRef node = new DENetworkNodeRef(sl.dimension(), pos);
                data.addNodeTo(id, node);
                networkBE.setDENetworkId(id);

                // Optionnel: invalider des capabilities si d’autres blocs s’abonnent à ce nœud via caches
                // ((ILevelExtension) sl).invalidateCapabilities(pos);
            }
        }
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ServerLevel sl = (ServerLevel) level;
        var be = sl.getBlockEntity(pos);
        if (!(be instanceof DENetworkControllerBlockEntity networkBE)) {
            return InteractionResult.CONSUME;
        }
        UUID id = networkBE.getDENetworkId();
        if (id == null) {
            ((ServerPlayer) player).sendSystemMessage(Component.literal("DarkNet: aucun réseau associé"));
            return InteractionResult.CONSUME;
        }
        var data = DENetworks.get(sl);
        var rec = data.networksView().get(id);
        if (rec == null) {
            ((ServerPlayer) player).sendSystemMessage(Component.literal("DarkNet: réseau introuvable: " + id));
            return InteractionResult.CONSUME;
        }

        Component msg = DEChat.formatNetworkSummary(rec);
        ((ServerPlayer) player).sendSystemMessage(msg);
        return InteractionResult.CONSUME;
    }
}

