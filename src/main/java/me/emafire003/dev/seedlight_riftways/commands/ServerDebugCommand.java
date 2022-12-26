package me.emafire003.dev.seedlight_riftways.commands;


import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.emafire003.dev.seedlight_riftways.SeedLightRiftwaysClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

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
            if(ip.startsWith("local:")){
                ip = ip.replaceAll("local:", "");
                SeedLightRiftwaysClient.connectToLocalWorld(ip);
            }else{
                SeedLightRiftwaysClient.connectToServer();
            }
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    private int connectLocal(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        FabricClientCommandSource source = context.getSource();

        try{
            SeedLightRiftwaysClient.connectToLocalWorldLastPlayed();
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }

    }


    public LiteralCommandNode<FabricClientCommandSource> getNode() {
        return ClientCommandManager
                .literal("server").executes(this::connect)
                .then(
                        ClientCommandManager.literal("local").executes(this::connectLocal)
                )
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
