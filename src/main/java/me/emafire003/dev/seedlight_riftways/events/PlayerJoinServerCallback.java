package me.emafire003.dev.seedlight_riftways.events;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;

public class PlayerJoinServerCallback {

    public static void registerJoinEvent(){
        PlayerJoinEvent.EVENT.register((player, server) -> {
            LOGGER.info(player.getName()+" has joined the server!");
            if(player.getWorld().isClient){
                return ActionResult.PASS;
            }else{
                SeedLightRiftways.sendUpdateRiftwayPacket(player);
            }
            return ActionResult.PASS;
        });
    }

    public static void registerEvents(){
        registerJoinEvent();
    }
}
