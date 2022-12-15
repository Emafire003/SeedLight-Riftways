package me.emafire003.dev.seedlight_riftways.networking;

import io.netty.buffer.Unpooled;
import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.NoSuchElementException;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;

public class UpdateRiftwayPasswordS2C extends PacketByteBuf {
    public static final Identifier ID = new Identifier(SeedLightRiftways.MOD_ID , "update_riftway_password_packet");

    public UpdateRiftwayPasswordS2C(String psw) {
        super(Unpooled.buffer());
        this.writeString(psw);
    }

    public static String read(PacketByteBuf buf) {
        try{
            return buf.readString();
        }catch (NoSuchElementException e){
            LOGGER.warn("No value in the packet while reading, probably not a big problem");
            return null;
        }catch (Exception e){
            LOGGER.error("There was an error while reading the packet!");
            e.printStackTrace();
            return null;
        }

    }
}
