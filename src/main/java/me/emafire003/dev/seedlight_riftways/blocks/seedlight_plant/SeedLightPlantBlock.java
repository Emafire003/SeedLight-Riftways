package me.emafire003.dev.seedlight_riftways.blocks.seedlight_plant;

import me.emafire003.dev.seedlight_riftways.items.SeedlightRiftwaysItems;
import me.emafire003.dev.structureplacerapi.StructurePlacerAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import static me.emafire003.dev.seedlight_riftways.SeedlightRiftways.MOD_ID;

public class SeedLightPlantBlock extends CropBlock {

    public static final IntProperty AGE = IntProperty.of("age", 0, 5);
    public static final IntProperty WATERED = IntProperty.of("age", 0, 5);

    public SeedLightPlantBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return SeedlightRiftwaysItems.SEEDLIGHT_SEED;
    }

    @Override
    public int getMaxAge() {
        return 6;
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
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) >= 15) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getAvailableMoisture(this, world, pos);
                if (random.nextInt((int)(50.0F / f) + 1) == 0) {
                    world.setBlockState(pos, this.withAge(i + 1), Block.NOTIFY_LISTENERS);
                }
            }else{
                //TODO place the structure
                StructurePlacerAPI placer = new StructurePlacerAPI(world, new Identifier(MOD_ID, "riftway_general"), pos, BlockMirror.NONE, BlockRotation.NONE, true, 1.0f, new BlockPos(0, 0, 0));
                placer.loadStructure();
            }
        }

    }
}
