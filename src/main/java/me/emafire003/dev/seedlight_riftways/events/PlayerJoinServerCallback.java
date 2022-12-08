package me.emafire003.dev.seedlight_riftways.events;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;

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
                LOGGER.info("------------Sending COMING FROM RIFTWAY PACKET-----------------");
                player.sendMessage(Text.literal("------------Sending COMING FROM RIFTWAY PACKET-----------------"));
                SeedLightRiftwaysClient.sendComingFromRiftwayPacket(player, "tests", false);;
            }
            return ActionResult.PASS;
        });
    }

    public static void registerEvents(){
        registerJoinEvent();
        registerJoinFromRiftwayEvent();
    }
}
