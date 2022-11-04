package me.emafire003.dev.seedlight_riftways.blocks.riftwayblock;

import com.mojang.logging.LogUtils;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class RiftWayBlockEntity extends EndPortalBlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int field_31368 = 200;
    private static final int field_31369 = 40;
    private static final int field_31370 = 2400;
    private static final int field_31371 = 1;
    private static final int field_31372 = 10;
    private long age;
    private int teleportCooldown;
    @Nullable
    private BlockPos exitPortalPos;
    private boolean exactTeleport;

    public RiftWayBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SLRBlocks.SQUARE_PORTAL_BLOCKENTITY, blockPos, blockState);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, RiftWayBlockEntity blockEntity) {
        List<PlayerEntity> list = world.getEntitiesByClass(PlayerEntity.class, new Box(pos), (entity1 -> true));
        boolean active = false;
        if (!list.isEmpty() && active) {
            SeedLightRiftwaysClient.connetToOtherServer();
        }

    }

    public static void serverTick(World world, BlockPos pos, BlockState state, RiftWayBlockEntity blockEntity) {
        /*boolean bl2 = blockEntity.needsCooldownBeforeTeleporting();
        if (bl2) {
            --blockEntity.teleportCooldown;
        } else {
            List<Entity> list = world.getEntitiesByClass(Entity.class, new Box(pos), RiftWayBlockEntity::canTeleport);
            if (!list.isEmpty()) {
                tryTeleportingEntity(world, pos, state, (Entity)list.get(world.random.nextInt(list.size())), blockEntity);
            }

            if (blockEntity.age % 2400L == 0L) {
                startTeleportCooldown(world, pos, state, blockEntity);
            }
        }

        if (bl2 != blockEntity.needsCooldownBeforeTeleporting()) {
            markDirty(world, pos, state);
        }*/

    }


    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 1) {
            return true;
        } else {
            return super.onSyncedBlockEvent(type, data);
        }
    }


    public boolean shouldDrawSide(Direction direction) {
        return Block.shouldDrawSide(this.getCachedState(), this.world, this.getPos(), direction, this.getPos().offset(direction));
    }

    public int getDrawnSidesCount() {
        int i = 0;
        Direction[] var2 = Direction.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Direction direction = var2[var4];
            i += this.shouldDrawSide(direction) ? 1 : 0;
        }

        return i;
    }
}
