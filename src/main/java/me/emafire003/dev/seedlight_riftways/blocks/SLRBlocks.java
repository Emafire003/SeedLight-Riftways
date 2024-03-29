package me.emafire003.dev.seedlight_riftways.blocks;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftWayBlock;
import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftWayBlockEntity;
import me.emafire003.dev.seedlight_riftways.blocks.seedlight_plant.SeedLightPlantBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class SLRBlocks {

    public static final Block RIFTWAY_BLOCK = registerBlock("riftway",
            new RiftWayBlock(FabricBlockSettings.of(Material.AMETHYST).strength(-1f).collidable(false).luminance(8).sounds(BlockSoundGroup.AMETHYST_BLOCK)), ItemGroups.NATURAL);

    /*public static final Block RIFT_LOG = registerBlock("rift_log",
            createLogBlock(MapColor.CYAN, MapColor.TEAL), ItemGroup.BUILDING_BLOCKS
    );*/

    //Lore -> this wood is too unstable to be cut into planks!
    public static final Block RIFT_WOOD = registerBlock("rift_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD, MapColor.OAK_TAN).strength(2.0F).sounds(BlockSoundGroup.WOOD))
            , ItemGroups.BUILDING_BLOCKS
    );

    public static final Block SEEDLIGHT_RIFT_LEAVES = registerBlock("seedlight_rift_leaves",
            //createLeavesBlock(BlockSoundGroup.AZALEA_LEAVES, false),
            new LeavesBlock(FabricBlockSettings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.AZALEA_LEAVES).nonOpaque().allowsSpawning(SLRBlocks::canSpawnOnLeaves).suffocates(SLRBlocks::never).blockVision(SLRBlocks::never)),
            ItemGroups.NATURAL
    );

    public static final Block SEEDLIGHT_RIFT_FLOWERING_LEAVES = registerBlock("seedlight_rift_flowering_leaves",
            new LeavesBlock(FabricBlockSettings.of(Material.LEAVES).strength(0.2F).luminance(5).ticksRandomly().sounds(BlockSoundGroup.AZALEA_LEAVES).nonOpaque().allowsSpawning(SLRBlocks::canSpawnOnLeaves).suffocates(SLRBlocks::never).blockVision(SLRBlocks::never)),
            ItemGroups.NATURAL
    );


    public static final Block SEEDLIGHT_PLANT = registerBlockWithoutItem("seedlight_plant",
            new SeedLightPlantBlock(FabricBlockSettings.copy(Blocks.WHEAT)));


    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(SeedLightRiftways.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(Registries.ITEM, new Identifier(SeedLightRiftways.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registries.BLOCK, new Identifier(SeedLightRiftways.MOD_ID, name), block);
    }


    private static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return false;
    }

    private static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }

    private static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }


    public static PillarBlock createLogBlock(MapColor topMapColor, MapColor sideMapColor) {
        return new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (state) -> {
            return state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor;
        }).strength(2.0F).sounds(BlockSoundGroup.WOOD));
    }

    public static void registerBlocks() {
        SeedLightRiftways.LOGGER.debug("Registering Blocks for " + SeedLightRiftways.MOD_ID);
    }

    public static BlockEntityType<RiftWayBlockEntity> RIFTWAY_BLOCKENTITY;

    public static void registerAllBlockEntities() {
        RIFTWAY_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(SeedLightRiftways.MOD_ID, "riftway"),
                FabricBlockEntityTypeBuilder.create(RiftWayBlockEntity::new,
                        RIFTWAY_BLOCK).build());
    }
}
