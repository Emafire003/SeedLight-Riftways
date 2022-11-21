package me.emafire003.dev.seedlight_riftways.client;

import me.emafire003.dev.seedlight_riftways.SeedlightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftwayBlockEntityRenderer;
import me.emafire003.dev.seedlight_riftways.commands.SLRCommands;
import me.emafire003.dev.seedlight_riftways.particles.RiftParticle;
import me.emafire003.dev.seedlight_riftways.particles.RiftParticles;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class SeedLightRiftwaysClient implements ClientModInitializer {

    public static String SERVER_IP = "metamc.it";
    //TODO i will need to save it on a file
    public static boolean IS_RIFTWAY_ACTIVE = false;
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(SLRCommands::registerCommands);
        BlockRenderLayerMap.INSTANCE.putBlock(SLRBlocks.RIFTWAY_BLOCK, SLRRenderLayers.getRiftway());
        BlockEntityRendererRegistry.register(SLRBlocks.RIFTWAY_BLOCKENTITY, RiftwayBlockEntityRenderer::new);
        ParticleFactoryRegistry.getInstance().register(RiftParticles.RIFTWAY_BEGIN_TELEPORT_PARTICLE, RiftParticle.Factory::new);

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

    public static void connectToServer(){
        try{
            ServerInfo serverInfo = new ServerInfo("riftway_to_"+SERVER_IP, SERVER_IP, false);
            SeedlightRiftways.LOGGER.info("Trying to disconnect player form world...");
            disconnect(MinecraftClient.getInstance());
            SeedlightRiftways.LOGGER.info("Trying to connect to server...");
            ConnectScreen.connect(MinecraftClient.getInstance().currentScreen, MinecraftClient.getInstance(), ServerAddress.parse(serverInfo.address), serverInfo);
            SeedlightRiftways.LOGGER.info("aaaand possibly failed. Or maybe not!");
        }catch (Exception e){
            e.printStackTrace();
            SeedlightRiftways.LOGGER.info("AH-AH, GOTCHA, THERE IS AN ERROR!");
        }

    }

    public static void connectToServer(String ip_address){
        try{
            ServerInfo serverInfo = new ServerInfo("riftway_to_"+ip_address, ip_address, false);
            SeedlightRiftways.LOGGER.info("Trying to disconnect player form world...");
            disconnect(MinecraftClient.getInstance());
            SeedlightRiftways.LOGGER.info("Trying to connect to server...");
            ConnectScreen.connect(MinecraftClient.getInstance().currentScreen, MinecraftClient.getInstance(), ServerAddress.parse(serverInfo.address), serverInfo);
            SeedlightRiftways.LOGGER.info("aaaand possibly failed. Or maybe not!");
        }catch (Exception e){
            e.printStackTrace();
            SeedlightRiftways.LOGGER.info("AH-AH, GOTCHA, THERE IS AN ERROR!");
        }

    }
}
