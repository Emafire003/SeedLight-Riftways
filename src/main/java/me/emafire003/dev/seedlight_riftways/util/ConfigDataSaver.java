package me.emafire003.dev.seedlight_riftways.util;

import com.google.common.reflect.TypeToken;
import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.GsonConfigInstance;
import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.List;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.MOD_ID;


public class ConfigDataSaver {

    public static GsonConfigInstance<ConfigDataSaver> CONFIG_INSTANCE = new GsonConfigInstance<>(ConfigDataSaver.class, SeedLightRiftways.PATH.resolve(MOD_ID+"_config_data.json"));

    @ConfigEntry
    public boolean is_riftway_active = SeedLightRiftways.getIsRiftwayActive();

    @ConfigEntry
    public HashMap<Long, Boolean> riftway_locations = SeedLightRiftways.getRiftwaysLocations();

    @ConfigEntry
    public List<String> server_riftway_items_password = SeedLightRiftways.getServerRiftwayItemsPassword();

    @ConfigEntry
    public String saved_server_ip = SeedLightRiftways.getSavedServerIp();

    @ConfigEntry
    public int listener_port = SeedLightRiftways.getListenerPort();

    public Screen createGui(Screen parent) {
        // time to use YOCL!
        return new TitleScreen();
    }

}
