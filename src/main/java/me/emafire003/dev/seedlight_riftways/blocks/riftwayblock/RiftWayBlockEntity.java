package me.emafire003.dev.seedlight_riftways.blocks.riftwayblock;

import com.mojang.logging.LogUtils;
import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RiftWayBlockEntity extends EndPortalBlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static HashMap<UUID, Integer> players_on_cooldown = new HashMap<>();
    //TODO maybe make this configurable
    public static int RIFTWAY_COOLDOWN = 5*2000;

    public RiftWayBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SLRBlocks.RIFTWAY_BLOCKENTITY, blockPos, blockState);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, RiftWayBlockEntity blockEntity) {
        List<PlayerEntity> list = world.getEntitiesByClass(PlayerEntity.class, new Box(pos), (entity1 -> true));
        //TODO will need to send a packet to the server so it moves the player a couple of blocks back so it prevents a loop
        //TODO also, when the player gets teleported I should make them invisible, and then sprinkle particles around them
        //TODO and sprinkle some cool SFX too. Oh and maybe use shaders "during teleport"
        //TODO to teleport the player back, I should make it just before shutting down the server
        if (!list.isEmpty() && list.contains(MinecraftClient.getInstance().player)) {
            if(SeedLightRiftwaysClient.IS_RIFTWAY_ACTIVE){
                LOGGER.info("Is player in cooldown: " + players_on_cooldown.containsKey(MinecraftClient.getInstance().player.getUuid()));
                LOGGER.info("Cooldown list: " + players_on_cooldown);
                if(!SeedLightRiftwaysClient.connection_initialised && !players_on_cooldown.containsKey(MinecraftClient.getInstance().player.getUuid())){
                    SeedLightRiftwaysClient.setDepartureBlockpos(pos);
                    //SeedLightRiftwaysClient.connectToServer();
                    //TODO config the delay between teleports? Currently it's 10 seconds
                    players_on_cooldown.put(MinecraftClient.getInstance().player.getUuid(), RIFTWAY_COOLDOWN);
                    SeedLightRiftwaysClient.startConnectionToServer();
                }


            }else{
                for(PlayerEntity player : list){
                    if(player.isSpectator()){
                        continue;
                    }
                    Vec3d v = Vec3d.ofCenter(pos).add(.5, .5, .5).subtract(player.getPos());
                    v = v.multiply(1, 0.00001, 1).multiply(-1.5);
                    v = v.normalize().multiply(.5);
                    //Vec3d backwards_vel = player.getVelocity().multiply(-1);
                    //player.setVelocity(backwards_vel.x, backwards_vel.y, backwards_vel.z);
                    player.setVelocityClient(v.x, v.y, v.z);
                    player.setVelocity(v);
                }
            }

        }
        //This ticks the cooldown and lowers it each tick by one (client side)
        for(Map.Entry<UUID, Integer> entry : players_on_cooldown.entrySet()){
            if(entry.getValue()-1 == 0){
                players_on_cooldown.remove(entry.getKey());
            }else{
                players_on_cooldown.put(entry.getKey(), entry.getValue()-1);
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
            if(!SeedLightRiftways.IS_RIFTWAY_ACTIVE){
                for(PlayerEntity player : list){
                    if(player.isSpectator()){
                        continue;
                    }
                    Vec3d v = Vec3d.ofCenter(pos).add(.5, .5, .5).subtract(player.getPos());
                    v = v.multiply(-1, 0.00001, -1).multiply(1.5);
                    v = v.normalize().multiply(.5);
                    //Vec3d backwards_vel = player.getVelocity().multiply(-1);
                    //player.setVelocity(backwards_vel.x, backwards_vel.y, backwards_vel.z);
                    player.setVelocityClient(v.x, v.y, v.z);
                    player.setVelocity(v);
                }
            }else{
                for(PlayerEntity player : list){
                    LOGGER.info("Is player in cooldown: " + players_on_cooldown.containsKey(player.getUuid()));
                    LOGGER.info("Cooldown list: " + players_on_cooldown);
                    if(player != null && !players_on_cooldown.containsKey(player.getUuid())){
                        //TODO config the delay between teleports? Currently it's 10 seconds
                        players_on_cooldown.put(player.getUuid(), RIFTWAY_COOLDOWN);
                        //player.setPosition(Vec3d.ofCenter(pos.add(-2, 0, 0)));
                        //execute at @a run particle minecraft:end_rod ~ ~ ~ 0.03 0.85 0.03 0.15 200
                        ((ServerWorld) world).spawnParticles(ParticleTypes.END_ROD, player.getX(), player.getY(), player.getZ(), 200, 0.03, 0.85, 0.03, 0.15);
                        //execute at @a run particle minecraft:dragon_breath ~ ~0.5 ~ 0.2 0.7 0.2 0.15 200
                        ((ServerWorld) world).spawnParticles(ParticleTypes.DRAGON_BREATH, player.getX(), player.getY()+0.5, player.getZ(), 200, 0.2, 0.7, 0.2, 0.15);
                        //execute at @a run particle minecraft:enchant ~ ~0.5 ~ 0.2 0.6 0.2 0.15 100
                        ((ServerWorld) world).spawnParticles(ParticleTypes.ENCHANT, player.getX(), player.getY()+0.5, player.getZ(), 100, 0.2, 0.6, 0.2, 0.15);
                        //execute at @a run particle minecraft:flash ~ ~0.5 ~ 0.2 0.7 0.2 0.15 1
                        ((ServerWorld) world).spawnParticles(ParticleTypes.FLASH, player.getX(), player.getY()+0.5, player.getZ(), 1, 0.2, 0.7, 0.2, 0.15);
                        player.playSound(SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 0.5f, 0.25f);
                        player.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 1f, 0.2f);
                        player.playSound(SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1f, 0.25f);
                        player.playSound(SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1f, 0.25f);
                        player.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 1f, 1.7f);
                        player.playSound(SoundEvents.BLOCK_PORTAL_TRIGGER, 1f, 0.1f);

                    }
                }
            }
        }

        //This ticks the cooldown and lowers it each tick by one
        for(Map.Entry<UUID, Integer> entry : players_on_cooldown.entrySet()){
            if(entry.getValue()-1 == 0){
                players_on_cooldown.remove(entry.getKey());
            }else{
                players_on_cooldown.put(entry.getKey(), entry.getValue()-1);
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
            //this.teleportCooldown = 40;
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
