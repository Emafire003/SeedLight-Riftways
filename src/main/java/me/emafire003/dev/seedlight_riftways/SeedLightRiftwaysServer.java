package me.emafire003.dev.seedlight_riftways;

import me.emafire003.dev.seedlight_riftways.networking.RiftwayListener;
import net.fabricmc.api.DedicatedServerModInitializer;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.MOD_ID;

public class SeedLightRiftwaysServer implements DedicatedServerModInitializer {
    /**
     * Runs the mod initializer on the server environment.
     */
    @Override
    public void onInitializeServer() {
        Thread listener_thread = new Thread(new RiftwayListener());
        listener_thread.setName(MOD_ID + " Listener_Thread");
        listener_thread.start();
    }
}
