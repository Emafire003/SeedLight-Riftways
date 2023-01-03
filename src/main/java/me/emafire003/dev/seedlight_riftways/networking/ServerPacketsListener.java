package me.emafire003.dev.seedlight_riftways.networking;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftWayBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;

//The minecraft one
public class ServerPacketsListener {

    public static void registerPacketListeners(){
        registerComingFromRiftwayPacket();
    }

    private static void registerComingFromRiftwayPacket(){
        ServerPlayNetworking.registerGlobalReceiver(PortalLocCminRiftPacketC2S.ID, (((server, player, handler, buf, responseSender) -> {
            if(player.getWorld().isClient){
                return;
            }
            //boolean coming_from_direct = ComingFromRiftwayPacketC2S.getIsFromDirectPortal(buf);
            //String origin_server = ComingFromRiftwayPacketC2S.getOriginServer(buf);
            BlockPos origin_rift_pos = PortalLocCminRiftPacketC2S.read(buf);
            server.execute(() -> {
                try{
                    //player.sendMessage(Text.literal("DEUBUG: Is Coming from direct portal: " + coming_from_direct));
                    //player.sendMessage(Text.literal("DEUBUG: Origin server: " + origin_server));
                    BlockPos teleport_to = SeedLightRiftways.getClosestRiftway(origin_rift_pos, player.getWorld());
                    LOGGER.debug("Position chosen: " + teleport_to);
                    RiftWayBlockEntity.playExitRiftwaySoundEffect(player.getWorld(), teleport_to);
                    RiftWayBlockEntity.spawnExitRiftwayParticles(player.getWorld(), Vec3d.ofCenter(teleport_to.add(0,1,0)));
                    player.teleport(teleport_to.getX()+0.5, teleport_to.getY(), teleport_to.getZ()+0.5);
                    }catch (NoSuchElementException e){
                    LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        })));
    }

}
