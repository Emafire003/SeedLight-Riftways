package me.emafire003.dev.seedlight_riftways.events;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftWayBlockEntity;
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
            if(player.getWorld().isClient){
                return ActionResult.PASS;
            }else{
                SeedLightRiftways.sendUpdateRiftwayPacket(player);
            }
            //TODO modify cooldown if config needs it (somehow it's not 20 ticks = 1 second
            RiftWayBlockEntity.players_on_cooldown.put(player.getUuid(), RiftWayBlockEntity.RIFTWAY_COOLDOWN);
            return ActionResult.PASS;
        });
    }

    public static void registerEvents(){
        registerJoinEvent();
    }
}
