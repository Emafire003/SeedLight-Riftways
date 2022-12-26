package me.emafire003.dev.seedlight_riftways.events;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftWayBlockEntity;
import net.minecraft.util.ActionResult;

public class PlayerJoinServerCallback {

    public static void registerJoinEvent(){
        PlayerJoinEvent.EVENT.register((player, server) -> {
            if(player.getWorld().isClient){
                return ActionResult.PASS;
            }
            SeedLightRiftways.sendUpdateRiftwayPacket(player);

            //TODO modify cooldown if config needs it (somehow it's not 20 ticks = 1 second
            RiftWayBlockEntity.players_on_cooldown.put(player.getUuid(), RiftWayBlockEntity.RIFTWAY_COOLDOWN);
            return ActionResult.PASS;
        });
    }

    public static void registerEvents(){
        registerJoinEvent();
    }
}
