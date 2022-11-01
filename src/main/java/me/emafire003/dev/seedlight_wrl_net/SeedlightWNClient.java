package me.emafire003.dev.seedlight_wrl_net;

import me.emafire003.dev.seedlight_wrl_net.commands.LightCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import me.emafire003.dev.seedlight_wrl_net.items.LightItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class SeedlightWNClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(LightCommands::registerCommands);
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

    public static void connetToOtherServer(){
        try{
            ServerInfo serverInfo = new ServerInfo("emaworld", "metamc.it", false);
            SeedlightWN.LOGGER.info("Trying to disconnect player form world...");
            disconnect(MinecraftClient.getInstance());
            SeedlightWN.LOGGER.info("Trying to connect to server...");
            ConnectScreen.connect(MinecraftClient.getInstance().currentScreen, MinecraftClient.getInstance(), ServerAddress.parse(serverInfo.address), serverInfo);
            SeedlightWN.LOGGER.info("aaaand possibly failed. Or maybe not!");
        }catch (Exception e){
            e.printStackTrace();
            SeedlightWN.LOGGER.info("AH-AH, GOTCHA, THERE IS AN ERROR!");
        }

    }
}
