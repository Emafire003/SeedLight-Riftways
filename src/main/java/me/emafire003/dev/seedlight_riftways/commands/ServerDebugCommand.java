package me.emafire003.dev.seedlight_riftways.commands;


import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ServerDebugCommand implements SLRCommand {
    

    private int activateDelay(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {;
        int delay = IntegerArgumentType.getInteger(context, "delay");
        FabricClientCommandSource source = context.getSource();

        try{

            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    private int activate(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        FabricClientCommandSource source = context.getSource();

        try{
            SeedLightRiftwaysClient.connetToOtherServer();
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }

    }


    public LiteralCommandNode<FabricClientCommandSource> getNode() {
        return ClientCommandManager
                .literal("connect").executes(this::activate)
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
