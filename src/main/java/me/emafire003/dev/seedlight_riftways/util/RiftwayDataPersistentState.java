package me.emafire003.dev.seedlight_riftways.util;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;

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
        SeedLightRiftways.LOGGER.debug(" Registering position on persistent state as BlockPos: " + pos + " asLong: " + pos.asLong());
        riftway_local_pos.put(pos.asLong(), isDirect);
    }

    public void removeLocalRiftwayLocation(boolean isDirect, BlockPos pos){
        riftway_local_pos.remove(pos.asLong(), isDirect);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound locations = new NbtCompound();
        locations.putLongArray("rift_locations", riftway_local_pos.keySet().stream().toList());
        //SeedLightRiftways.LOGGER.debug("Positions to be saved: " + riftway_local_pos.keySet().stream().toList().toString());
        for(Map.Entry entry : this.riftway_local_pos.entrySet()){
            locations.putBoolean("rift_" + entry.getKey().toString(), (Boolean) entry.getValue());
        }
        nbt.put("RiftwayLocations", locations);
        return nbt;
    }

    public static RiftwayDataPersistentState readNbt(NbtCompound nbt) {
        NbtCompound locations = nbt.getCompound("RiftwayLocations");
        HashMap<Long, Boolean> rift_locs = new HashMap<>();
        for(Long pos : locations.getLongArray("rift_locations")){
            //SeedLightRiftways.LOGGER.info("èèèèèèèèèèèèèèè Reading from dat file, BlockPos: " + BlockPos.fromLong(pos) + " asLong: " + pos);
            rift_locs.put(pos, locations.getBoolean(pos.toString()));
        }
        return new RiftwayDataPersistentState(rift_locs);
    }
}
