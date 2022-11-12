package me.emafire003.dev.seedlight_riftways.commands;


import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ServerDebugCommand implements SLRCommand {
    


    private int connect(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        FabricClientCommandSource source = context.getSource();

        try{
            SeedLightRiftwaysClient.connectToServer();
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    private int connectTo(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        FabricClientCommandSource source = context.getSource();
        String ip = StringArgumentType.getString(context, "ip");
        try{
            SeedLightRiftwaysClient.connectToServer();
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }

    }


    public LiteralCommandNode<FabricClientCommandSource> getNode() {
        return ClientCommandManager
                .literal("connect").executes(this::connect)
                .then(
                        ClientCommandManager.argument("ip", StringArgumentType.string()).executes(this::connectTo)
                )
                /*.then(
                        CommandManager.argument("player", EntityArgumentType.players())
                                .executes(this::activate)
                )
                .then(
                        CommandManager.argument("player", EntityArgumentType.players())
                                        .then(
                                                CommandManager.argument("delay", IntegerArgumentType.integer())
                                                        .executes(this::activateDelay)
                                        )

                )*/
                .build();
    }



}
