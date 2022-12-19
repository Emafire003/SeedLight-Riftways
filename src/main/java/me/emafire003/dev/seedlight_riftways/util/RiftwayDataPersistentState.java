package me.emafire003.dev.seedlight_riftways.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.List;
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
        riftway_local_pos.put(pos.asLong(), isDirect);
    }


    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound locations = new NbtCompound();
        locations.putLongArray("rift_locations", (List<Long>) riftway_local_pos.keySet());
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
            rift_locs.put(pos, locations.getBoolean(pos.toString()));
        }

        return new RiftwayDataPersistentState(rift_locs);
    }
}
