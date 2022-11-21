package me.emafire003.dev.seedlight_riftways.blocks;

import me.emafire003.dev.seedlight_riftways.SeedlightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftWayBlock;
import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftWayBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public class SLRBlocks {

    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(SeedlightRiftways.MOD_ID, name), block);
    }

    public static final Block RIFTWAY_BLOCK = registerBlock("riftway",
            new RiftWayBlock(FabricBlockSettings.of(Material.AMETHYST).strength(0.7f).collidable(false).luminance(8).sounds(BlockSoundGroup.AMETHYST_BLOCK)), ItemGroup.DECORATIONS);

    /*public static final Block RIFT_LOG = registerBlock("rift_log",
            createLogBlock(MapColor.CYAN, MapColor.TEAL), ItemGroup.BUILDING_BLOCKS
    );*/

    //Lore -> this wood is too unstable to be cut into planks!
    public static final Block RIFT_WOOD = registerBlock("rift_wood",
            new PillarBlock(FabricBlockSettings.of(Material.WOOD, MapColor.OAK_TAN).strength(2.0F).sounds(BlockSoundGroup.WOOD))
            , ItemGroup.BUILDING_BLOCKS
    );

    public static final Block SEEDLIGHT_RIFT_LEAVES = registerBlock("seedlight_rift_leaves",
            //createLeavesBlock(BlockSoundGroup.AZALEA_LEAVES, false),
            new LeavesBlock(FabricBlockSettings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.AZALEA_LEAVES).nonOpaque().allowsSpawning(SLRBlocks::canSpawnOnLeaves).suffocates(SLRBlocks::never).blockVision(SLRBlocks::never)),
            ItemGroup.DECORATIONS
    );

    public static final Block SEEDLIGHT_RIFT_FLOWERING_LEAVES = registerBlock("seedlight_rift_flowering_leaves",
            new LeavesBlock(FabricBlockSettings.of(Material.LEAVES).strength(0.2F).luminance(5).ticksRandomly().sounds(BlockSoundGroup.AZALEA_LEAVES).nonOpaque().allowsSpawning(SLRBlocks::canSpawnOnLeaves).suffocates(SLRBlocks::never).blockVision(SLRBlocks::never)),
            ItemGroup.DECORATIONS
    );

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(Registry.ITEM, new Identifier(SeedlightRiftways.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
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
        SeedlightRiftways.LOGGER.debug("Registering Blocks for " + SeedlightRiftways.MOD_ID);
    }

    public static BlockEntityType<RiftWayBlockEntity> RIFTWAY_BLOCKENTITY;

    public static void registerAllBlockEntities() {
        RIFTWAY_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(SeedlightRiftways.MOD_ID, "square_portal"),
                FabricBlockEntityTypeBuilder.create(RiftWayBlockEntity::new,
                        RIFTWAY_BLOCK).build());
    }
}
