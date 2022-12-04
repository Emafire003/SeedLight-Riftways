package me.emafire003.dev.seedlight_riftways.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.NoSuchElementException;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;

public class UpdateRiftwayActivenessS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(SeedLightRiftways.MOD_ID , "update_riftway_activeness_packet");

    public UpdateRiftwayActivenessS2C(boolean isActive) {
        super(Unpooled.buffer());
        this.writeBoolean(isActive);
    }

    public static boolean read(PacketByteBuf buf) {
        try{
            return buf.readBoolean();
        }catch (NoSuchElementException e){
            LOGGER.warn("No value in the packet while reading, probably not a big problem");
            return buf.readBoolean();
        }catch (Exception e){
            LOGGER.error("There was an error while reading the packet!");
            e.printStackTrace();
            return buf.readBoolean();
        }
    }
}
