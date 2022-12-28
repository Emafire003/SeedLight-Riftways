package me.emafire003.dev.seedlight_riftways.util;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.util.config.ConfigEntry;
import me.emafire003.dev.seedlight_riftways.util.config.GsonConfigInstance;

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

    @ConfigEntry
    public boolean requires_password = SeedLightRiftways.getRequiresPassword();

    @ConfigEntry
    public List<String> is_riftway_active_in_world = SeedLightRiftways.getIsRiftwayActiveInWorld();

    /*public Screen createGui(Screen parent) {
        // time to use YOCL!
        return new TitleScreen();
    }*/

}
