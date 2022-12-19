package me.emafire003.dev.seedlight_riftways.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;

public class SLRCommands {

    //Based on Factions' code https://github.com/ickerio/factions
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        LiteralCommandNode<FabricClientCommandSource> lightcommands = ClientCommandManager
                .literal("connect")
                .requires(ClientCommandSource -> {
                    return ClientCommandSource.hasPermissionLevel(2);
                })
                .build();


        dispatcher.getRoot().addChild(lightcommands);

        SLRCommand[] commands = new SLRCommand[] {
                new ServerDebugCommand(),
        };

        for (SLRCommand command : commands) {
            lightcommands.addChild(command.getNode());
        }
    }

}
