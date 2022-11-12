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
import org.slf4j.Logger;

import java.util.List;

public class RiftWayBlockEntity extends EndPortalBlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    public RiftWayBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SLRBlocks.RIFTWAY_BLOCKENTITY, blockPos, blockState);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, RiftWayBlockEntity blockEntity) {
        List<PlayerEntity> list = world.getEntitiesByClass(PlayerEntity.class, new Box(pos), (entity1 -> true));
        //TODO will need to send a packet to the server so it moves the player a couple of blocks back so it prevents a loop
        //TODO also, when the player gets teleported I should make them invisible, and then sprinkle particles around them
        //TODO and sprinkle some cool SFX too. Oh and maybe use shaders "during teleport"
        //TODO to teleport the player back, I should make it just before shutting down the server
        if (!list.isEmpty()) {
            if(SeedLightRiftwaysClient.IS_RIFTWAY_ACTIVE){
                SeedLightRiftwaysClient.connectToServer();
            }else{
                for(PlayerEntity player : list){
                    Vec3d backwards_vel = player.getVelocity().multiply(-1);
                    player.setVelocityClient(backwards_vel.x, backwards_vel.y, backwards_vel.z);
                }
            }

        }

    }

    public static void serverTick(World world, BlockPos pos, BlockState state, RiftWayBlockEntity blockEntity) {
        List<PlayerEntity> list = world.getEntitiesByClass(PlayerEntity.class, new Box(pos), (entity1 -> true));
        //TODO will need to send a packet to the server so it moves the player a couple of blocks back so it prevents a loop
        //TODO also, when the player gets teleported I should make them invisible, and then sprinkle particles around them
        //TODO and sprinkle some cool SFX too. Oh and maybe use shaders "during teleport"
        //TODO to teleport the player back, I should make it just before shutting down the server
        if (!list.isEmpty()) {
            if(!SeedLightRiftwaysClient.IS_RIFTWAY_ACTIVE){
                for(PlayerEntity player : list){
                    Vec3d backwards_vel = player.getVelocity().multiply(-1);
                    player.setVelocity(backwards_vel.x, backwards_vel.y, backwards_vel.z);
                }
            }

        }

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
