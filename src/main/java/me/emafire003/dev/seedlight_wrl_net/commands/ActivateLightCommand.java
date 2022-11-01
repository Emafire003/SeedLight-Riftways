package me.emafire003.dev.seedlight_wrl_net.commands;


import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.seedlight_wrl_net.SeedlightWNClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ActivateLightCommand implements LightCommand{
    

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
            SeedlightWNClient.connetToOtherServer();
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
