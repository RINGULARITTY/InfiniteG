package fr.ringularity.infiniteg.commands;

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

@EventBusSubscriber(modid=InfiniteG.MOD_ID)
public final class ModCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        var d = event.getDispatcher();

        d.register(Commands.literal("darknet")
                .then(Commands.literal("list"))
                .then(Commands.literal("networks")
                        .executes(ModCommands::list)
                )
        );
    }

    private static int list(CommandContext<CommandSourceStack> ctx) {
        var src = ctx.getSource();
        var data = DarkEnergyNetworks.get(src.getServer());
        var nets = data.networksView().values();

        if (nets.isEmpty()) {
            src.sendSuccess(() -> Component.literal("No DarkEnergy network"), false);
            return 0;
        }

        src.sendSuccess(() -> Component.literal("DarkEnergy Network: " + nets.size() + "/" + nets.size()), false);

        for (var rec : nets) {
            Component msg = DarkChat.formatNetworkSummary(rec);
            src.sendSuccess(() -> msg, false);
        }
        return nets.size();
    }
}
