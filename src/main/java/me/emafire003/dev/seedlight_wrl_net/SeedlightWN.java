package me.emafire003.dev.seedlight_wrl_net;

import me.emafire003.dev.seedlight_wrl_net.items.LightItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeedlightWN implements ModInitializer {

    public static final String MOD_ID = "seedlight_wrl_net";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LightItems.registerItems();
    }
}
