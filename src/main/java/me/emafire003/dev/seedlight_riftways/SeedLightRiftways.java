package me.emafire003.dev.seedlight_riftways;

import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.events.PlayerJoinServerCallback;
import me.emafire003.dev.seedlight_riftways.items.SeedlightRiftwaysItems;
import me.emafire003.dev.seedlight_riftways.networking.RiftwayServerInfo;
import me.emafire003.dev.seedlight_riftways.networking.ServerPacketsListener;
import me.emafire003.dev.seedlight_riftways.networking.UpdateRiftwayActivenessS2C;
import me.emafire003.dev.seedlight_riftways.networking.UpdateRiftwayIpS2C;
import me.emafire003.dev.seedlight_riftways.util.ConfigDataSaver;
import me.emafire003.dev.seedlight_riftways.util.LootTableModifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeedLightRiftways implements ModInitializer {

    public static final String MOD_ID = "seedlight_riftways";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path PATH = FabricLoader.getInstance().getConfigDir(); //+ "/" + MOD_ID + "/";
    public static final String PREFIX = "§b[§5RiftWays§b] ";

    //true equals "Direct Riftway", false means a generic blue one
    public static HashMap<Long, Boolean> RIFTWAYS_LOCATIONS = new HashMap<>();
    public static List<String> SERVER_RIFTWAY_ITEMS_PASSWORD = new ArrayList<>();
    //This IP is not the IP to which the client connects, but the IP the server has stored in itself, AKA
    //this is the IP of the server which will be the arrival point if you go through a riftway on THIS server.
    public static String SAVED_SERVER_IP = "127.0.0.1";
    public static boolean IS_RIFTWAY_ACTIVE = false;

    public static int LISTENER_PORT = 9000;

    @Override
    public void onInitialize() {
        SLRBlocks.registerBlocks();
        SLRBlocks.registerAllBlockEntities();
        SeedlightRiftwaysItems.registerItems();
        getValuesFromFile();
        ServerPacketsListener.registerPacketListeners();
        PlayerJoinServerCallback.registerEvents();
        LootTableModifier.modifyLootTables();
        LOGGER.info("Starting Listener server...");
        Thread listener_thread = new Thread(new RiftwayServerInfo());
        listener_thread.setName(MOD_ID + " Listener_Thread");
        listener_thread.start();
    }

    public static HashMap<Long, Boolean> getRiftwaysLocations(){
        return RIFTWAYS_LOCATIONS;
    }

    public static List<String> getServerRiftwayItemsPassword(){
        return SERVER_RIFTWAY_ITEMS_PASSWORD;
    }

    /**
     * CAUTION
     * This IP is not the IP to which the client connects, but the IP the server has stored in itself, AKA
     * this is the IP of the server which will be the arrival point if you go through a riftway on THIS server.
     * */
    public static String getSavedServerIp(){
        return SAVED_SERVER_IP;
    }

    public static int getListenerPort(){
        return LISTENER_PORT;
    }

    public static boolean getIsRiftwayActive(){
        return IS_RIFTWAY_ACTIVE;
    }

    public static void updateConfig(){
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().is_riftway_active = getIsRiftwayActive();
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().riftway_locations = getRiftwaysLocations();
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().server_riftway_items_password = getServerRiftwayItemsPassword();
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().saved_server_ip = getSavedServerIp();
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().listener_port = getListenerPort();
        ConfigDataSaver.CONFIG_INSTANCE.save();

    }

    private  void getValuesFromFile(){
        try{
            ConfigDataSaver.CONFIG_INSTANCE.load();
            ConfigDataSaver CONFIG = ConfigDataSaver.CONFIG_INSTANCE.getConfig();
            LOGGER.info("Getting variables values from the data file...");

            if(CONFIG.riftway_locations != null && !CONFIG.riftway_locations.isEmpty()){
                if(!RIFTWAYS_LOCATIONS.isEmpty()){
                    RIFTWAYS_LOCATIONS.putAll(CONFIG.riftway_locations);
                }else{
                    RIFTWAYS_LOCATIONS = CONFIG.riftway_locations;
                }
            }

            if(CONFIG.server_riftway_items_password != null && !CONFIG.server_riftway_items_password.isEmpty()){
                SERVER_RIFTWAY_ITEMS_PASSWORD = CONFIG.server_riftway_items_password;
            }

            IS_RIFTWAY_ACTIVE = CONFIG.is_riftway_active;
            SAVED_SERVER_IP = CONFIG.saved_server_ip;
            LISTENER_PORT = CONFIG.listener_port;


            LOGGER.info("Done!");
        }catch (Exception e){
            LOGGER.error("There was an error while getting the values from the file onto the mod");
            e.printStackTrace();
        }

        ServerLifecycleEvents.SERVER_STARTED.register(SeedLightRiftways::sendUpdateRiftwayToPlayers);
    }

    public static void addRiftwayLocation(boolean isDirect, BlockPos pos){
        RIFTWAYS_LOCATIONS.put(pos.asLong(), isDirect);
        ConfigDataSaver.CONFIG_INSTANCE.save();
    }

    public static void removeRiftwayLocation(boolean isDirect, BlockPos pos){
        RIFTWAYS_LOCATIONS.remove(pos.asLong(), isDirect);
        ConfigDataSaver.CONFIG_INSTANCE.save();
    }

    public static void sendUpdateRiftwayPacket(ServerPlayerEntity player){
        try{
            ServerPlayNetworking.send(player, UpdateRiftwayActivenessS2C.ID, new UpdateRiftwayActivenessS2C(IS_RIFTWAY_ACTIVE));
            ServerPlayNetworking.send(player, UpdateRiftwayIpS2C.ID, new UpdateRiftwayIpS2C(SAVED_SERVER_IP));
        }catch(Exception e){
            LOGGER.error("FAILED to send data packets to the client!");
            e.printStackTrace();
        }
    }

    public static void sendUpdateRiftwayToPlayers(MinecraftServer server){
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : players) {
            if(player.getWorld().isClient){
                return;
            }
            sendUpdateRiftwayPacket(player);
        }
    }

    //TODO will need to make a listner for the coming from riftway packet

}
