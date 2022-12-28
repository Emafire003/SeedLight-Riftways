package me.emafire003.dev.seedlight_riftways;

import com.mojang.datafixers.util.Pair;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.events.PlayerJoinServerCallback;
import me.emafire003.dev.seedlight_riftways.items.SeedlightRiftwaysItems;
import me.emafire003.dev.seedlight_riftways.networking.*;
import me.emafire003.dev.seedlight_riftways.util.ConfigDataSaver;
import me.emafire003.dev.seedlight_riftways.util.LootTableModifier;
import me.emafire003.dev.seedlight_riftways.util.RiftwayDataPersistentState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.*;

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
    public static boolean REQUIRES_PASSWORD = false;

    public static List<String> IS_RIFTWAY_ACTIVE_IN_WORLD = new ArrayList<>();

    public static int LISTENER_PORT = 27999;

    @Override
    public void onInitialize() {
        SLRBlocks.registerBlocks();
        SLRBlocks.registerAllBlockEntities();
        SeedlightRiftwaysItems.registerItems();
        getValuesFromFile();
        ServerPacketsListener.registerPacketListeners();
        PlayerJoinServerCallback.registerEvents();
        LootTableModifier.modifyLootTables();
        registerRiftwayPersistentData();
        LOGGER.info("Starting Listener server...");

    }

    public static HashMap<Long, Boolean> getRiftwaysLocations(){
        return RIFTWAYS_LOCATIONS;
    }

    public static List<String> getIsRiftwayActiveInWorld(){
        return IS_RIFTWAY_ACTIVE_IN_WORLD;
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

    public static boolean getRequiresPassword(){
        return REQUIRES_PASSWORD;
    }

    public static void updateConfig(){
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().is_riftway_active = getIsRiftwayActive();
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().riftway_locations = getRiftwaysLocations();
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().is_riftway_active_in_world = getIsRiftwayActiveInWorld();
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().server_riftway_items_password = getServerRiftwayItemsPassword();
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().saved_server_ip = getSavedServerIp();
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().listener_port = getListenerPort();
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().requires_password = getRequiresPassword();
        ConfigDataSaver.CONFIG_INSTANCE.getConfig().is_riftway_active_in_world = getIsRiftwayActiveInWorld();
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

            if(CONFIG.is_riftway_active_in_world != null && !CONFIG.is_riftway_active_in_world.isEmpty()){
                if(!IS_RIFTWAY_ACTIVE_IN_WORLD.isEmpty()){
                    IS_RIFTWAY_ACTIVE_IN_WORLD.addAll(CONFIG.is_riftway_active_in_world);
                }else{
                    IS_RIFTWAY_ACTIVE_IN_WORLD = CONFIG.is_riftway_active_in_world;
                }
            }

            if(CONFIG.server_riftway_items_password != null && !CONFIG.server_riftway_items_password.isEmpty()){
                SERVER_RIFTWAY_ITEMS_PASSWORD = CONFIG.server_riftway_items_password;
            }

            IS_RIFTWAY_ACTIVE = CONFIG.is_riftway_active;
            SAVED_SERVER_IP = CONFIG.saved_server_ip;
            LISTENER_PORT = CONFIG.listener_port;
            REQUIRES_PASSWORD = CONFIG.requires_password;


            LOGGER.info("Done!");
        }catch (Exception e){
            LOGGER.error("There was an error while getting the values from the file onto the mod");
            e.printStackTrace();
        }

        ServerLifecycleEvents.SERVER_STARTED.register(SeedLightRiftways::sendUpdateRiftwayToPlayers);
    }

    public static void addRiftwayLocation(boolean isDirect, BlockPos pos, World world){
        RIFTWAYS_LOCATIONS.put(pos.asLong(), isDirect);
        addRiftwayLocationLocal(isDirect, pos, world);
        LOGGER.info("Adding riftway location, " + RIFTWAYS_LOCATIONS.toString());
        updateConfig();
    }


    public static void addRiftwayLocationLocal(boolean isDirect, BlockPos pos, World world){
        try{
            if(!world.isClient){ //Ensures it runs on the server
                LOGGER.info("Nope, world isn't client (which is ok, server thread yknow), is the server dedicated? : " + ((ServerWorld) world).getServer().isDedicated());
                //If it's not dedicated it's integrated so we are in in singleplayer
                if(!((ServerWorld) world).getServer().isDedicated()){
                    ((ServerWorld) world).getPersistentStateManager()
                            .getOrCreate(RiftwayDataPersistentState::readNbt, RiftwayDataPersistentState::getInstance, "riftways_locations")
                            .addLocalRiftwayLocation(isDirect, pos);
                    ((ServerWorld) world).getPersistentStateManager().getOrCreate(RiftwayDataPersistentState::readNbt, RiftwayDataPersistentState::getInstance, "riftways_locations").markDirty();
                    //LOGGER.info("Adding riftway location locally, " + locations_data.riftway_local_pos.toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void registerRiftwayPersistentData(){
        ServerWorldEvents.LOAD.register( (server, world) -> {
            RiftwayDataPersistentState data = world.getPersistentStateManager().getOrCreate(RiftwayDataPersistentState::readNbt, RiftwayDataPersistentState::getInstance, "riftways_locations");
            data.markDirty();
        });
    }

    public static void removeRiftwayLocation(boolean isDirect, BlockPos pos, World world){
        RIFTWAYS_LOCATIONS.remove(pos.asLong(), isDirect);
        removeRiftwayLocationLocal(isDirect, pos, world);
        LOGGER.info("Removing Riftway Location at " + pos);
        ConfigDataSaver.CONFIG_INSTANCE.save();
    }

    public static void removeRiftwayLocationLocal(boolean isDirect, BlockPos pos, World world){
        try{
            if(!world.isClient){ //Ensures it runs on the server
                //If it's not dedicated it's integrated so we are in in singleplayer
                if(!((ServerWorld) world).getServer().isDedicated()){
                    ((ServerWorld) world).getPersistentStateManager()
                            .getOrCreate(RiftwayDataPersistentState::readNbt, RiftwayDataPersistentState::getInstance, "riftways_locations")
                            .removeLocalRiftwayLocation(isDirect, pos);
                    ((ServerWorld) world).getPersistentStateManager().getOrCreate(RiftwayDataPersistentState::readNbt, RiftwayDataPersistentState::getInstance, "riftways_locations").markDirty();
                    //LOGGER.info("Adding riftway location locally, " + locations_data.riftway_local_pos.toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setIsRiftwayActiveInWorld(boolean isActive, String name){
        if(isActive){
            IS_RIFTWAY_ACTIVE_IN_WORLD.add(name);
        }else IS_RIFTWAY_ACTIVE_IN_WORLD.remove(name);
        ConfigDataSaver.CONFIG_INSTANCE.save();
    }

    public static void sendUpdateRiftwayPacket(ServerPlayerEntity player){
        try{
            ServerPlayNetworking.send(player, UpdateRiftwayActivenessS2C.ID, new UpdateRiftwayActivenessS2C(IS_RIFTWAY_ACTIVE));
            ServerPlayNetworking.send(player, UpdateRiftwayIpS2C.ID, new UpdateRiftwayIpS2C(SAVED_SERVER_IP));
            ServerPlayNetworking.send(player, UpdateRiftwayPasswordS2C.ID, new UpdateRiftwayPasswordS2C(SERVER_RIFTWAY_ITEMS_PASSWORD.toString()));
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

    public static BlockPos getClosestRiftway(BlockPos origin_rift_pos, World world){
        LOGGER.debug("DEUBUG: Origin server pos: " + origin_rift_pos);
        BlockPos teleport_to = BlockPos.ORIGIN.add(0,70,0); //At least try to spawn above surface if something goes wrong
        boolean first = true;
        Set<Map.Entry<Long, Boolean>> entrySet = SeedLightRiftways.RIFTWAYS_LOCATIONS.entrySet();
        if(!world.isClient){
            if(!world.getServer().isDedicated()){ //If not dedicated it's integrated so singleplayer
                RiftwayDataPersistentState locations_data = ((ServerWorld) world).getPersistentStateManager().getOrCreate(RiftwayDataPersistentState::readNbt, RiftwayDataPersistentState::getInstance, "riftways_locations");
                entrySet = locations_data.riftway_local_pos.entrySet();
                }
        }
        for(Map.Entry<Long, Boolean> entry : entrySet){
            boolean is_direct = entry.getValue();
            long pos_long = entry.getKey();
            if(!is_direct){
                BlockPos pos = BlockPos.fromLong(pos_long);
                if(first){
                    first = false;
                    teleport_to = pos;
                    continue;
                }
                double distance_from_origin = pos.getSquaredDistance(origin_rift_pos.getX(), origin_rift_pos.getY(), origin_rift_pos.getZ());
                double distance_from_teleportto = pos.getSquaredDistance(teleport_to.getX(), teleport_to.getY(), teleport_to.getZ());
                LOGGER.debug("Current pos: " + pos);
                if(distance_from_origin < distance_from_teleportto){
                    teleport_to = pos;
                    LOGGER.debug("Switching to new pos: " + teleport_to);
                }
            }
        }
        return teleport_to;

    }

}
