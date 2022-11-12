package me.emafire003.dev.seedlight_riftways.networking;

import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.*;

//Needs to send: portal_location, origin_server
//with the option in the future of sending the inventory, the HP, the abilities and such.
public class ComingFromRiftwayPacketC2S extends PacketByteBuf {

    public ComingFromRiftwayPacketC2S(BlockPos portal_location, String origin_server) {
        super(Unpooled.buffer());
        this.writeInt(2);
        this.writeBlockPos(portal_location);
        this.writeString(origin_server, 50);
    }

    public static @Nullable BlockPos getOriginPortalPos(PacketByteBuf buf) {
        try {
            buf.readInt();
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
            buf.readInt();
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
}
