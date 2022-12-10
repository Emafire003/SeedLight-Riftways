package me.emafire003.dev.seedlight_riftways.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

//Needs to send: portal_location, origin_server, is_direct_portal
//with the option in the future of sending the inventory, the HP, the abilities and such.

//Will create another packet to send LightWithin data maybe
//PortalLocation Coming From RiftWay Packet
public class PortalLocCminRiftPacketC2S extends PacketByteBuf {

    public static final Identifier ID = new Identifier(SeedLightRiftways.MOD_ID , "portal_loc_coming_from_riftway_packet");

    public PortalLocCminRiftPacketC2S(BlockPos portal_location) {
        super(Unpooled.buffer());
        this.writeBlockPos(portal_location);
    }

    public static @Nullable BlockPos read(PacketByteBuf buf) {
        try {
            return buf.readBlockPos();
        }catch (NoSuchElementException e){
            //LOGGER.warn("No value in the packet while reading, probably not a big problem");
            return null;
        }catch (Exception e){
            //LOGGER.error("There was an error while reading the packet!");
            e.printStackTrace();
            return null;
        }

    }

}
