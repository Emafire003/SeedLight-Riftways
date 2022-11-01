package me.emafire003.dev.seedlight_wrl_net.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.emafire003.dev.seedlight_wrl_net.SeedlightWN.LOGGER;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {



    @Inject(method = "connect(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setCurrentServerEntry(Lnet/minecraft/client/network/ServerInfo;)V"),
            cancellable = true
    )
    private static void connectOutput(Screen screen, MinecraftClient client, ServerAddress address, ServerInfo info, CallbackInfo ci){
        LOGGER.info("Ok trying to connect to the server...?");
        LOGGER.info("The screen is: " + screen.toString());
        LOGGER.info("The client is: " + client);
        LOGGER.info("The address is: " + address.getAddress());
        LOGGER.info("The serverinfo is: " + info.toString());
    }

    @Inject(method = "connect(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V",
            at = @At("TAIL"),
            cancellable = true
    )
    private static void connectOutput1(Screen screen, MinecraftClient client, ServerAddress address, ServerInfo info, CallbackInfo ci){
        LOGGER.info("It looks like it should have done something?");

    }
}
