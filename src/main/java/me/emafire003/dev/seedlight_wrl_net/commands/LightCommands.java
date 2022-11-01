package me.emafire003.dev.seedlight_wrl_net.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
public class LightCommands {

    //Based on Factions' code https://github.com/ickerio/factions
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        LiteralCommandNode<FabricClientCommandSource> lightcommands = ClientCommandManager
                .literal("server")
                .requires(ClientCommandSource -> {
                    return ClientCommandSource.hasPermissionLevel(2);
                })
                .build();


        dispatcher.getRoot().addChild(lightcommands);

        LightCommand[] commands = new LightCommand[] {
                new ActivateLightCommand(),
        };

        for (LightCommand command : commands) {
            lightcommands.addChild(command.getNode());
        }
    }

}
