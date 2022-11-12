package me.emafire003.dev.seedlight_riftways.blocks.directriftblock;

import me.emafire003.dev.seedlight_riftways.blocks.riftwayblock.RiftWayBlockEntity;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class DirectRiftwayBlockEnity extends RiftWayBlockEntity {
    public DirectRiftwayBlockEnity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, RiftWayBlockEntity blockEntity) {
        List<PlayerEntity> list = world.getEntitiesByClass(PlayerEntity.class, new Box(pos), (entity1 -> true));
        boolean active = false;
        if (!list.isEmpty() && active) {
            SeedLightRiftwaysClient.connectToServer();
        }

    }
}
