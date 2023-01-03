package me.emafire003.dev.seedlight_riftways.blocks.seedlight_plant;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.items.SeedlightRiftwaysItems;
import me.emafire003.dev.structureplacerapi.StructurePlacerAPI;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.MOD_ID;

public class SeedLightPlantBlock extends CropBlock {

    public static final IntProperty AGE = IntProperty.of("age", 0, 5);
    public static int WATER_LEVEL = 0;
    public static final int MAX_WATER_LEVEL = 10;

    public SeedLightPlantBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return SeedlightRiftwaysItems.SEEDLIGHT_SEED;
    }

    @Override
    public int getMaxAge() {
        return 5;
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected int getGrowthAmount(World world) {
        return 1;
    }


    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        //Will only grow if the water level is above 3
        //If it decides to grow, it will consume some water
        //When it's fully grown it will spawn the riftway structure
        //also needs to have light level of 15
        //It has 5 stages
        if ((world.getBaseLightLevel(pos, 0) >= 15) && this.getWaterLevel() > 3)  {
            int i = this.getAge(state);
            if(i == this.getMaxAge()-1){
                spawnStructure(world, pos);
            }
            if (i < this.getMaxAge()) {
                float f = getAvailableMoisture(this, world, pos);
                if (random.nextInt((int)(35.0F / f) + 1) == 0) {
                    world.setBlockState(pos, this.withAge(i + 1), Block.NOTIFY_LISTENERS);
                    this.setWaterLevel(this.getWaterLevel() - random.nextBetween(1, 2));
                }
            }
        }

    }

    public void spawnStructure(ServerWorld world, BlockPos pos){
        StructurePlacerAPI placer = new StructurePlacerAPI(world, new Identifier(MOD_ID, "riftway_general"), pos, BlockMirror.NONE, BlockRotation.NONE, true, 1.0f, new BlockPos(-6, -1, -6));
        placer.loadStructure();
        SeedLightRiftways.addRiftwayLocation(false, pos, world);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return false;
    }

    public void setWaterLevel(int level){
        WATER_LEVEL = level;
        if(WATER_LEVEL > getMaxWaterLevel()){
            WATER_LEVEL = getMaxWaterLevel();
        }
    }

    public int getWaterLevel(){
        return WATER_LEVEL;
    }

    public int getMaxWaterLevel(){
        return MAX_WATER_LEVEL;
    }

    protected static float getAvailableMoisture(Block block, BlockView world, BlockPos pos) {
        return WATER_LEVEL;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.GRASS_BLOCK) || floor.isOf(Blocks.MOSS_BLOCK)
                || floor.isOf(Blocks.MUD) || floor.isOf(Blocks.MUDDY_MANGROVE_ROOTS)
                || floor.isOf(Blocks.MYCELIUM);
    }
}
