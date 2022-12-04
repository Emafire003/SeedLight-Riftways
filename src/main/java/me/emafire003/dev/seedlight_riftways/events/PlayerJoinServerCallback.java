package me.emafire003.dev.seedlight_riftways.events;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.minecraft.util.ActionResult;

public class PlayerJoinServerCallback {

    public static void registerJoinEvent(){
        PlayerJoinEvent.EVENT.register((player, server) -> {
            if(player.getWorld().isClient){
                return ActionResult.PASS;
            }else{
                SeedLightRiftways.sendUpdateRiftwayPacket(player);
            }
            return ActionResult.PASS;
        });
    }

    //TODO this will need to change a bi
    //currently should send a packet whenever the client joins a server.
    //It should do this only if it came from a riftway.
    public static void registerJoinFromRiftwayEvent(){
        PlayerJoinEvent.EVENT.register((player, server) -> {
            if(player.getWorld().isClient){
                SeedLightRiftwaysClient.sendComingFromRiftwayPacket(player, "localhost", false);;
            }
            return ActionResult.PASS;
        });
    }

    public static void registerEvents(){
        registerJoinEvent();
        registerJoinFromRiftwayEvent();
    }
}
