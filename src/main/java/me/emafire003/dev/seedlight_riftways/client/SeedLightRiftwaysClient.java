package me.emafire003.dev.seedlight_riftways.client;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftwayBlockEntityRenderer;
import me.emafire003.dev.seedlight_riftways.commands.SLRCommands;
import me.emafire003.dev.seedlight_riftways.networking.ComingFromRiftwayPacketC2S;
import me.emafire003.dev.seedlight_riftways.networking.RiftwayClientInfo;
import me.emafire003.dev.seedlight_riftways.networking.UpdateRiftwayActivenessS2C;
import me.emafire003.dev.seedlight_riftways.networking.UpdateRiftwayIpS2C;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.NoSuchElementException;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;
import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.MOD_ID;

@Environment(EnvType.CLIENT)
public class SeedLightRiftwaysClient implements ClientModInitializer {

    public static String SERVER_IP = "127.0.0.1";
    public static boolean IS_RIFTWAY_ACTIVE = false;
    public static BlockPos DEPARTURE_BLOCKPOS;
    public static boolean connection_initialised = false;


    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(SLRCommands::registerCommands);
        BlockRenderLayerMap.INSTANCE.putBlock(SLRBlocks.RIFTWAY_BLOCK, SLRRenderLayers.getRiftway());
        BlockEntityRendererRegistry.register(SLRBlocks.RIFTWAY_BLOCKENTITY, RiftwayBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(SLRBlocks.SEEDLIGHT_PLANT, RenderLayer.getCutout());
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

    private void registerUpdateRiftwayIpPacket(){
        ClientPlayNetworking.registerGlobalReceiver(UpdateRiftwayIpS2C.ID, ((client, handler, buf, responseSender) -> {
            var result = UpdateRiftwayIpS2C.read(buf);

            client.execute(() -> {
                try{
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
        boolean bl = client.isInSingleplayer();
        client.world.disconnect();
        if (bl) {
            client.disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
        } else {
            client.disconnect();
        }
        TitleScreen titleScreen = new TitleScreen();
        client.setScreen(new MultiplayerScreen(titleScreen));
    }


    public static void startConnectionToServer(){
        if(!connection_initialised ){
            Thread connect_thread = new Thread(new RiftwayClientInfo());
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
    }

    public static void connectToServer(){
        try{
            ServerInfo serverInfo = new ServerInfo("riftway_to_"+SERVER_IP, SERVER_IP, false);
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

    public static void sendComingFromRiftwayPacket(ServerPlayerEntity player, String origin_server, boolean is_direct){
        try{
            ClientPlayNetworking.send(ComingFromRiftwayPacketC2S.ID, new ComingFromRiftwayPacketC2S(DEPARTURE_BLOCKPOS, origin_server, is_direct));
        }catch(Exception e){
            LOGGER.error("FAILED to send data packets to the client!");
            e.printStackTrace();
        }
    }

}
