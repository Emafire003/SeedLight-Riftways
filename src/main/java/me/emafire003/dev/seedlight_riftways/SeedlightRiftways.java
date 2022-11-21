package me.emafire003.dev.seedlight_riftways;

import io.netty.handler.address.ResolveAddressHandler;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.items.SeedlightRiftwaysItems;
import me.emafire003.dev.seedlight_riftways.networking.ServerPacketsListener;
import me.emafire003.dev.seedlight_riftways.particles.RiftParticles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeedlightRiftways implements ModInitializer {

    public static final String MOD_ID = "seedlight_riftways";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path PATH = Path.of(FabricLoader.getInstance().getConfigDir() + "/" + MOD_ID + "/");
    public static final String PREFIX = "§b[§5RiftWays§b] ";

    //true equals "Direct Riftway", false means a generic blue one
    public static HashMap<Boolean, BlockPos> RIFTWAYS_LOCATIONS = new HashMap<>();
    public static List<String> SERVER_RIFTWAY_ITEMS_PASSWORD = new ArrayList<>();

    public static int LISTENER_PORT = 9000;

    @Override
    public void onInitialize() {
        SLRBlocks.registerBlocks();
        SLRBlocks.registerAllBlockEntities();
        SeedlightRiftwaysItems.registerItems();
        RiftParticles.registerParticles();
        ServerPacketsListener.registerPacketListeners();

    }

    public static boolean isAddressValid(String address){
        // Using try Logic So that if there is an error then
        // easily get the error
        try {

            // calling the function which gives the IP
            // Address from the given host
            InetAddress[] iaddress
                    = InetAddress.getAllByName(address);
            iaddress[0].toString();
            return true;
        }
        catch (UnknownHostException e) {
            System.out.println(e);
            return false;
        }
    }

}
