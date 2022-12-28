package me.emafire003.dev.seedlight_riftways.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;

public class RiftwayDataPersistentState extends PersistentState {

    public HashMap<Long, Boolean> riftway_local_pos = new HashMap<>();

    public RiftwayDataPersistentState(HashMap<Long, Boolean> map) {
        this.riftway_local_pos = map;
    }

    public static RiftwayDataPersistentState getInstance() {
        RiftwayDataPersistentState hp = new RiftwayDataPersistentState(new HashMap<Long, Boolean>());
        hp.markDirty();
        return hp;
    }

    public void addLocalRiftwayLocation(boolean isDirect, BlockPos pos){
        LOGGER.info("=========Adding a new riftloc to the dat file, which corresponds to: " + pos.toString() + " long: " + pos.asLong());
        riftway_local_pos.put(pos.asLong(), isDirect);
    }

    public void removeLocalRiftwayLocation(boolean isDirect, BlockPos pos){
        riftway_local_pos.remove(pos.asLong(), isDirect);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound locations = new NbtCompound();
        locations.putLongArray("rift_locations", riftway_local_pos.keySet().stream().toList());
        LOGGER.info("==========Saving the riftway location to a file");
        LOGGER.info("????????????? The stuff to save: " + riftway_local_pos.toString());
        for(Map.Entry entry : this.riftway_local_pos.entrySet()){
            locations.putBoolean("rift_" + entry.getKey().toString(), (Boolean) entry.getValue());
        }
        LOGGER.info("The Saved NBT compound: " + locations);
        LOGGER.info("The locations array: " + locations.getLongArray("rift_location").toString());
        nbt.put("RiftwayLocations", locations);
        return nbt;
    }

    public static RiftwayDataPersistentState readNbt(NbtCompound nbt) {
        NbtCompound locations = nbt.getCompound("RiftwayLocations");
        HashMap<Long, Boolean> rift_locs = new HashMap<>();
        LOGGER.info("=============0 Reading NBT");
        for(Long pos : locations.getLongArray("rift_locations")){
            rift_locs.put(pos, locations.getBoolean(pos.toString()));
        }
        LOGGER.info("The riftlocs: " + rift_locs.toString());
        return new RiftwayDataPersistentState(rift_locs);
    }
}
