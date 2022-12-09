package me.emafire003.dev.seedlight_riftways.networking;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.NoSuchElementException;

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
                    player.sendMessage(Text.literal("DEUBUG: Origin server pos: " + origin_rift_pos));
                    BlockPos teleport_to = BlockPos.ORIGIN;

                    for(Map.Entry<Long, Boolean> entry : SeedLightRiftways.RIFTWAYS_LOCATIONS.entrySet()){
                        boolean is_direct = entry.getValue();
                        long pos_long = entry.getKey();
                        if(!is_direct){
                            BlockPos pos = BlockPos.fromLong(pos_long);
                            double distance_from_origin = pos.getSquaredDistance(origin_rift_pos.getX(), origin_rift_pos.getY(), origin_rift_pos.getZ());
                            double distance_from_teleportto = pos.getSquaredDistance(teleport_to.getX(), teleport_to.getY(), teleport_to.getZ());
                            if(distance_from_origin < distance_from_teleportto){
                                teleport_to = pos;
                            }
                        }
                    }
                    player.teleport(teleport_to.getX(), teleport_to.getY(), teleport_to.getZ());
                }catch (NoSuchElementException e){
                    SeedLightRiftways.LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    SeedLightRiftways.LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        })));
    }

}
