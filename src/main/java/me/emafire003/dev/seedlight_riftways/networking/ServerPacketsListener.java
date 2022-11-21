package me.emafire003.dev.seedlight_riftways.networking;

import me.emafire003.dev.seedlight_riftways.SeedlightRiftways;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.NoSuchElementException;

//The minecraft one
public class ServerPacketsListener {

    public static void registerPacketListeners(){
        registerComingFromRiftwayPacket();
    }

    private static void registerComingFromRiftwayPacket(){
        ServerPlayNetworking.registerGlobalReceiver(ComingFromRiftwayPacketC2S.ID, (((server, player, handler, buf, responseSender) -> {
            if(player.getWorld().isClient){
                return;
            }
            boolean coming_from_direct = ComingFromRiftwayPacketC2S.getIsFromDirectPortal(buf);
            String origin_server = ComingFromRiftwayPacketC2S.getOriginServer(buf);
            BlockPos origin_rift_pos = ComingFromRiftwayPacketC2S.getOriginPortalPos(buf);
            server.execute(() -> {
                try{
                    player.sendMessage(Text.literal("DEUBUG: Is Coming from direct portal: " + coming_from_direct));
                    player.sendMessage(Text.literal("DEUBUG: Origin server: " + origin_server));
                    player.sendMessage(Text.literal("DEUBUG: Origin server pos: " + origin_rift_pos));
                }catch (NoSuchElementException e){
                    SeedlightRiftways.LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    SeedlightRiftways.LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        })));
    }
}
