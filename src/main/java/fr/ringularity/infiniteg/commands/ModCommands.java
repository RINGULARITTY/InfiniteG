package fr.ringularity.infiniteg.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.capabilities.DarkChat;
import fr.ringularity.infiniteg.capabilities.DarkEnergyNetworks;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = InfiniteG.MOD_ID)
public final class ModCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        var d = event.getDispatcher();

        d.register(Commands.literal("darknet") // pas de .requires => accessible à tous
                .then(Commands.literal("list")
                        .executes(ctx -> list(ctx, Integer.MAX_VALUE))
                )
                .then(Commands.literal("list")
                        .then(Commands.argument("limit", IntegerArgumentType.integer(1, 1000))
                                .executes(ctx -> list(ctx, IntegerArgumentType.getInteger(ctx, "limit")))
                        )
                )
                .then(Commands.literal("networks") // alias
                        .executes(ctx -> list(ctx, Integer.MAX_VALUE))
                )
        );
    }

    private static int list(CommandContext<CommandSourceStack> ctx, int limit) {
        var src = ctx.getSource();
        // Overworld recommandé si SavedData global multi-dimension
        var data = DarkEnergyNetworks.get(src.getServer());
        var nets = data.networksView().values();

        if (nets.isEmpty()) {
            src.sendSuccess(() -> Component.literal("DarkNet: aucun réseau existant."), false);
            return 0;
        }
        int count = 0;
        for (var rec : nets) {
            if (count++ >= limit) break;
            Component msg = DarkChat.formatNetworkSummary(rec);
            // message au seul appelant, pas de broadcast ops
            src.sendSuccess(() -> msg, false);
        }
        int finalCount = count;
        src.sendSuccess(() -> Component.literal("DarkNet: " + Math.min(finalCount, nets.size()) + "/" + nets.size() + " réseaux listés."), false);
        return count;
    }
}
