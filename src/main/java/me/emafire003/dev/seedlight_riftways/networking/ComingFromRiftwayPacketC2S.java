package me.emafire003.dev.seedlight_riftways.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.*;

//Needs to send: portal_location, origin_server, is_direct_portal
//with the option in the future of sending the inventory, the HP, the abilities and such.

//TODO actually implement the rest of this stuff

//Will create another packet to send LightWithin data maybe
public class ComingFromRiftwayPacketC2S extends PacketByteBuf {

    public static final Identifier ID = new Identifier(SeedLightRiftways.MOD_ID , "coming_from_riftway_packet");

    public ComingFromRiftwayPacketC2S(BlockPos portal_location, String origin_server, boolean is_direct_portal) {
        super(Unpooled.buffer());
        this.writeBlockPos(portal_location);
        this.writeString(origin_server);
        this.writeBoolean(is_direct_portal);
    }

    public static @Nullable BlockPos getOriginPortalPos(PacketByteBuf buf) {
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

    public static @Nullable String getOriginServer(PacketByteBuf buf) {
        try {
            buf.readBlockPos();
            return buf.readString();
        }catch (NoSuchElementException e){
            //LOGGER.warn("No value in the packet while reading, probably not a big problem");
            return null;
        }catch (Exception e){
            //LOGGER.error("There was an error while reading the packet!");
            e.printStackTrace();
            return null;
        }

    }

    public static @Nullable boolean getIsFromDirectPortal(PacketByteBuf buf) {
        try {
            buf.readBlockPos();
            buf.readString();
            return buf.readBoolean();
        }catch (NoSuchElementException e){
            //LOGGER.warn("No value in the packet while reading, probably not a big problem");
            return false;
        }catch (Exception e){
            //LOGGER.error("There was an error while reading the packet!");
            e.printStackTrace();
            return false;
        }

    }
}
