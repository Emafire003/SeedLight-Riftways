package me.emafire003.dev.seedlight_riftways;

import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.items.SeedlightRiftwaysItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class SeedlightRiftways implements ModInitializer {

    public static final String MOD_ID = "seedlight_riftways";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Path PATH = Path.of(FabricLoader.getInstance().getConfigDir() + "/" + MOD_ID + "/");

    @Override
    public void onInitialize() {
        SLRBlocks.registerBlocks();
        SLRBlocks.registerAllBlockEntities();
        SeedlightRiftwaysItems.registerItems();
    }
}
