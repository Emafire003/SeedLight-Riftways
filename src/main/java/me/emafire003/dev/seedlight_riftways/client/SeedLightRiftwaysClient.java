package me.emafire003.dev.seedlight_riftways.client;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftWayBlockEntity;
import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftwayBlockEntityRenderer;
import me.emafire003.dev.seedlight_riftways.commands.SLRCommands;
import me.emafire003.dev.seedlight_riftways.events.PlayerJoinServerCallback;
import me.emafire003.dev.seedlight_riftways.networking.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;
import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.MOD_ID;

@Environment(EnvType.CLIENT)
public class SeedLightRiftwaysClient implements ClientModInitializer {

    public static String SERVER_IP = "127.0.0.1";
    public static String PREVIOUS_SERVER_IP = SERVER_IP;
    public static boolean IS_RIFTWAY_ACTIVE = false;
    public static BlockPos DEPARTURE_BLOCKPOS;
    public static String SERVER_ITEMS_PASSWORD = "";
    public static boolean connection_initialised = false;
    private static boolean coming_from_riftway = false;
    //this variable gets modified by the RiftwayClient thread
    //The Render Thread, aka the minecraft client, checks it. Whenever it is true it connects to the
    //server with SERVER_IP, etc provied above
    private static boolean connection_allowed = false;


    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(SLRCommands::registerCommands);
        BlockRenderLayerMap.INSTANCE.putBlock(SLRBlocks.RIFTWAY_BLOCK, SLRRenderLayers.getRiftway());
        BlockEntityRendererRegistry.register(SLRBlocks.RIFTWAY_BLOCKENTITY, RiftwayBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(SLRBlocks.SEEDLIGHT_PLANT, RenderLayer.getCutout());
        registerUpdateRiftwayActivenessPacket();
        registerUpdateRiftwayIpPacket();
        registerUpdateRiftwayPasswordPacket();
        PlayerJoinServerCallback.registerJoinEvent();

        registerComingFromRiftwaySenderEvent();

        //This checks weather or not the client can connect to a new server
        //whenever the connection_allowed turns true it connects to through the new Riftway
        // (it only happens when a RiftwayClient thread has checked that all is good)
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if(connection_allowed){
                connection_allowed = false;
                connectToServer();
            }
        });
    }

    /**This method should only be called by the RiftwayClient thread
     *
     * This method will make the client connect to the server. It
     * is kind of the equivalent of connectToServer() but can be called
     * by other threads other than the Render Thread*/
    public static void setConnectionAllowed(){
        connection_allowed = true;
    }

    public static void stopConnectionAllowed(){
        connection_allowed = false;
    }

    public static boolean getConnectionAllowed(){
        return connection_allowed;
    }

    private void registerUpdateRiftwayActivenessPacket(){
        ClientPlayNetworking.registerGlobalReceiver(UpdateRiftwayActivenessS2C.ID, ((client, handler, buf, responseSender) -> {
            var result = UpdateRiftwayActivenessS2C.read(buf);

            client.execute(() -> {
                try{
                    IS_RIFTWAY_ACTIVE = result;
                }catch (NoSuchElementException e){
                    LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        }));
    }

    private void registerUpdateRiftwayPasswordPacket(){
        ClientPlayNetworking.registerGlobalReceiver(UpdateRiftwayPasswordS2C.ID, ((client, handler, buf, responseSender) -> {
            var result = UpdateRiftwayPasswordS2C.read(buf);

            client.execute(() -> {
                try{
                    SERVER_ITEMS_PASSWORD = result;
                }catch (NoSuchElementException e){
                    LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        }));
    }

    private void registerUpdateRiftwayIpPacket(){
        ClientPlayNetworking.registerGlobalReceiver(UpdateRiftwayIpS2C.ID, ((client, handler, buf, responseSender) -> {
            var result = UpdateRiftwayIpS2C.read(buf);

            client.execute(() -> {
                try{
                    PREVIOUS_SERVER_IP = SERVER_IP;
                    SERVER_IP = result;
                }catch (NoSuchElementException e){
                    LOGGER.warn("No value in the packet, probably not a big problem");
                }catch (Exception e){
                    LOGGER.error("There was an error while getting the packet!");
                    e.printStackTrace();
                }
            });
        }));
    }

    /**
     * This sends the "coming from riftway packet"
     * whenever the client has passed through the riftway*/
    private void registerComingFromRiftwaySenderEvent(){
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            //TODO modify cooldown if config needs it
            RiftWayBlockEntity.players_on_cooldown.put(client.player.getUuid(), RiftWayBlockEntity.RIFTWAY_COOLDOWN*2);

            if(coming_from_riftway){
                sendComingFromRiftwayPacket(PREVIOUS_SERVER_IP, false);
                coming_from_riftway = false;
                playExitRiftwaySoundEffect();
            }

        });
    }

    /**
     * The one saved on the client*/
    public static String getCurrentServerIp(){
        return SERVER_IP;
    }

    //TODO will need to send the IP of the connected server when the player joins a new server.
    public static void setCurrentServerIp(String ip){
        SERVER_IP = ip;
    }

    public static boolean isIsRiftwayActive(){
        return IS_RIFTWAY_ACTIVE;
    }
    
    public static void disconnect(MinecraftClient client){
        //TODO debug
        LOGGER.debug("Trying to disconnect from the server/world...");

        boolean bl = client.isInSingleplayer();
        client.world.disconnect();
        if (bl) {
            client.disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
        } else {
            client.disconnect();
        }
        TitleScreen titleScreen = new TitleScreen();
        client.setScreen(new MultiplayerScreen(titleScreen));
        //TODO debug
        LOGGER.debug("Disconnected!");
    }

    public static void playEnterRiftwaySoundEffect(){
        LOGGER.info("Trying to play sound...");
        SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
        soundManager.play(PositionedSoundInstance.master(SoundEvents.BLOCK_PORTAL_TRAVEL, 0.2f));
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 0.25f));
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 0.25f));
        soundManager.play(PositionedSoundInstance.master(SoundEvents.BLOCK_PORTAL_TRAVEL, 1.7f));
        soundManager.play(PositionedSoundInstance.master(SoundEvents.BLOCK_PORTAL_TRIGGER, 0.1f));
        LOGGER.info("Played");
    }

    public static void playExitRiftwaySoundEffect(){
        LOGGER.info("Trying to play exit sound...");
        SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
        soundManager.play(PositionedSoundInstance.master(SoundEvents.BLOCK_PORTAL_TRAVEL, 0.5f, 0.8f));
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 0.4f));
        soundManager.play(PositionedSoundInstance.master(SoundEvents.BLOCK_PORTAL_TRIGGER, 1.78f));
        LOGGER.info("Played");
    }


    public static void startConnectionToServer(){
        if(!connection_initialised ){
            Thread connect_thread = new Thread(new RiftwayClient());
            connect_thread.setName(MOD_ID + "New Connection Thread");
            connect_thread.start();
            connection_initialised = true;
        }

    }

    public static void sendFailedConnectionMessage(String reason){
        MinecraftClient.getInstance().player.sendMessage(Text.literal(SeedLightRiftways.PREFIX +
                " §cAn error occurred when trying to connect to that server!"));
        MinecraftClient.getInstance().player.sendMessage(Text.literal(SeedLightRiftways.PREFIX +
                " §c" + reason));
        connection_initialised = false;
    }


    public static void connectToServer(){
        try{
            ServerInfo serverInfo = new ServerInfo("riftway_to_"+SERVER_IP, SERVER_IP, false);
            LOGGER.debug("Trying to disconnect player form world...");
            disconnect(MinecraftClient.getInstance());
            LOGGER.debug("Trying to connect to server...");
            ConnectScreen.connect(MinecraftClient.getInstance().currentScreen, MinecraftClient.getInstance(), ServerAddress.parse(serverInfo.address), serverInfo);
            coming_from_riftway = true;
            playEnterRiftwaySoundEffect();
            connection_initialised = false;
        }catch (Exception e){
            e.printStackTrace();
            connection_initialised = false;
        }

    }

    public static void connectToLocalWorld(String level_name) throws ExecutionException, InterruptedException {
        LOGGER.debug("Trying to disconnect player from server...");
        disconnect(MinecraftClient.getInstance());
        LOGGER.debug("Trying to connect to world...");
        MinecraftClient client = MinecraftClient.getInstance();
        coming_from_riftway = true;
        playEnterRiftwaySoundEffect();
        connection_initialised = false;
        List<LevelSummary> levels_summaries = client.getLevelStorage().loadSummaries(client.getLevelStorage().getLevelList()).get();

        LevelStorage.LevelSave save = new LevelStorage.LevelSave(client.getLevelStorage().getSavesDirectory().resolve(level_name));

        for(LevelSummary levelSummary : levels_summaries){
            if(!levelSummary.isLocked() && levelSummary.getName().equals(level_name)){
                WorldListWidget levelList = null;
                levelList = new WorldListWidget(new SelectWorldScreen(new TitleScreen()), MinecraftClient.getInstance(), 100, 100, 48, 100 - 64, 36, "hello", levelList);
                levelList.new WorldEntry(levelList, levelSummary).play();
                break;
            }
        }
    }

    /**This returns the LevelSummary of the last played world. If there are no worlds, returns null*/
    @Nullable
    public static LevelSummary getLastPlayedWorld() throws ExecutionException, InterruptedException {
        MinecraftClient client = MinecraftClient.getInstance();
        List<LevelSummary> levels_summaries = client.getLevelStorage().loadSummaries(client.getLevelStorage().getLevelList()).get();

        LevelSummary levelSum = null;
        for(LevelSummary levelSummary : levels_summaries){
            if(levelSum == null){
                levelSum = levelSummary;
            }
            if(levelSummary.getLastPlayed() < levelSum.getLastPlayed()){
                levelSum = levelSummary;
            }
        }
        return levelSum;

    }

    public static void connectToLocalWorldLastPlayed() throws ExecutionException, InterruptedException {
        LOGGER.debug("Trying to disconnect player form server...");
        disconnect(MinecraftClient.getInstance());
        LOGGER.debug("Trying to connect to world...");
        MinecraftClient client = MinecraftClient.getInstance();
        coming_from_riftway = true;
        playEnterRiftwaySoundEffect();
        connection_initialised = false;
        List<LevelSummary> levels_summaries = client.getLevelStorage().loadSummaries(client.getLevelStorage().getLevelList()).get();

        LevelSummary levelSum = getLastPlayedWorld();

        if(levelSum != null && !levelSum.isLocked()){
            WorldListWidget levelList = null;
            SelectWorldScreen selectWorldScreen = new SelectWorldScreen(new TitleScreen());
            client.setScreen(selectWorldScreen);
            levelList = new WorldListWidget(selectWorldScreen, MinecraftClient.getInstance(), selectWorldScreen.width, selectWorldScreen.height, 48, selectWorldScreen.height - 64, 36, "Connecting to the local world...", levelList);
            levelList.new WorldEntry(levelList, levelSum).play();
        }
    }

    public static void connectToServer(String ip_address){
        try{
            ServerInfo serverInfo = new ServerInfo("riftway_to_"+ip_address, ip_address, false);
            LOGGER.info("Trying to disconnect player form world...");
            disconnect(MinecraftClient.getInstance());
            LOGGER.info("Trying to connect to server...");
            ConnectScreen.connect(MinecraftClient.getInstance().currentScreen, MinecraftClient.getInstance(), ServerAddress.parse(serverInfo.address), serverInfo);
            LOGGER.info("aaaand possibly failed. Or maybe not!");
            connection_initialised = false;
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.info("AH-AH, GOTCHA, THERE IS AN ERROR!");
        }

    }

    public static void setDepartureBlockpos(BlockPos pos){
        DEPARTURE_BLOCKPOS = pos;
    }

    public static void sendComingFromRiftwayPacket(String origin_server, boolean is_direct){
        try{
            //ClientPlayNetworking.send(ComingFromRiftwayPacketC2S.ID, new ComingFromRiftwayPacketC2S(DEPARTURE_BLOCKPOS, origin_server, is_direct));
            ClientPlayNetworking.send(PortalLocCminRiftPacketC2S.ID, new PortalLocCminRiftPacketC2S(DEPARTURE_BLOCKPOS));
        }catch(Exception e){
            LOGGER.error("FAILED to send data packets to the client!");
            e.printStackTrace();
        }
    }

}
