package me.emafire003.dev.seedlight_riftways.blocks.riftwayblock;

import com.mojang.logging.LogUtils;
import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.SeedLightRiftwaysClient;
import me.emafire003.dev.seedlight_riftways.mixin.BundleItemInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.slf4j.Logger;

import java.util.*;

public class RiftWayBlockEntity extends EndPortalBlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static HashMap<UUID, Integer> players_on_cooldown = new HashMap<>();
    //TODO maybe make this configurable
    public static int RIFTWAY_COOLDOWN = 5*2000;

    public RiftWayBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SLRBlocks.RIFTWAY_BLOCKENTITY, blockPos, blockState);
    }

    //I used directly MinecraftClient.getInstance().player since it gave weird errors if I tried using a variable
    public static void clientTick(World world, BlockPos pos, BlockState state, RiftWayBlockEntity blockEntity) {
        List<PlayerEntity> list = world.getEntitiesByClass(PlayerEntity.class, new Box(pos), (entity1 -> true));
        if (!list.isEmpty() && list.contains(MinecraftClient.getInstance().player)) {
            if(SeedLightRiftwaysClient.IS_RIFTWAY_ACTIVE){
                if(MinecraftClient.getInstance().player.getStackInHand(Hand.MAIN_HAND).isOf(Items.BUNDLE)){
                    setPassFromBundle(MinecraftClient.getInstance().player.getStackInHand(Hand.MAIN_HAND));
                }else if(MinecraftClient.getInstance().player.getStackInHand(Hand.OFF_HAND).isOf(Items.BUNDLE)){
                    setPassFromBundle(MinecraftClient.getInstance().player.getStackInHand(Hand.OFF_HAND));
                }
                if(!SeedLightRiftwaysClient.connection_initialised && !players_on_cooldown.containsKey(MinecraftClient.getInstance().player.getUuid())){
                    SeedLightRiftwaysClient.setDepartureBlockpos(pos);

                    //TODO config the delay between teleports? Currently it's 10 seconds
                    players_on_cooldown.put(MinecraftClient.getInstance().player.getUuid(), RIFTWAY_COOLDOWN);
                    SeedLightRiftwaysClient.startConnectionToServer();
                    playEnterRiftwaySoundEffect(world, pos);
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
                    if(player != null && !players_on_cooldown.containsKey(player.getUuid())){
                        //TODO config the delay between teleports? Currently it's 10 seconds
                        players_on_cooldown.put(player.getUuid(), RIFTWAY_COOLDOWN);
                        spawnEnterRiftwayParticles(world, Vec3d.ofCenter(pos).add(0,1,0));
                        playEnterRiftwaySoundEffect(world, pos);
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

    @Environment(EnvType.CLIENT)
    private static void setPassFromBundle(ItemStack bundle){
        List<ItemStack> items = BundleItemInvoker.getBundledStacksInv(bundle).toList();
        if(!items.isEmpty()) {
            try{
                List<String> combination = new ArrayList<>();
                for(ItemStack item : items){
                    combination.add(item.getItem().getName().getString().toString());
                }
                SeedLightRiftwaysClient.SERVER_ITEMS_PASSWORD = combination.toString();
            }catch (Exception e){
                e.printStackTrace();
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
        return Block.shouldDrawSide(this.getCachedState(), Objects.requireNonNull(this.world), this.getPos(), direction, this.getPos().offset(direction));
    }

    public int getDrawnSidesCount() {
        int i = 0;
        Direction[] var2 = Direction.values();
        int var3 = var2.length;

        for (Direction direction : var2) {
            i += this.shouldDrawSide(direction) ? 1 : 0;
        }

        return i;
    }

    public static void playEnterRiftwaySoundEffect(World world, BlockPos pos){
        LOGGER.info("Trying to play enter riftway sound on world...");
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 1f, 0.2f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, SoundCategory.BLOCKS, 1f, 0.25f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, SoundCategory.BLOCKS, 1f, 0.25f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 1f, 1.7f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.BLOCKS, 1f, 0.1f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_AMBIENT, SoundCategory.BLOCKS, 1f, 0.5f, false);
        LOGGER.info("Played");
    }

    public static void spawnEnterRiftwayParticles(World world, Vec3d pos){
        ((ServerWorld) world).spawnParticles(ParticleTypes.END_ROD, pos.getX(), pos.getY(), pos.getZ(), 200, 0.03, 0.85, 0.03, 0.15);
        ((ServerWorld) world).spawnParticles(ParticleTypes.DRAGON_BREATH, pos.getX(), pos.getY()+0.5, pos.getZ(), 200, 0.2, 0.7, 0.2, 0.15);
        ((ServerWorld) world).spawnParticles(ParticleTypes.ENCHANT, pos.getX(), pos.getY()+0.5, pos.getZ(), 100, 0.2, 0.6, 0.2, 0.15);
        ((ServerWorld) world).spawnParticles(ParticleTypes.FLASH, pos.getX(), pos.getY()+0.5, pos.getZ(), 1, 0.2, 0.7, 0.2, 0.15);
    }

    public static void playExitRiftwaySoundEffect(World world, BlockPos pos){
        //playsound minecraft:block.portal.travel block @p ~ ~ ~ 1 0.5
        //playsound minecraft:entity.allay.ambient_with_item player @a ~ ~ ~ 0.8 0.4
        //playsound minecraft:block.portal.trigger ambient @p ~ ~ ~ 1 1.78
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 1f, 0.5f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, SoundCategory.BLOCKS, 0.8f, 0.4f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.BLOCKS, 1f, 1.78f, false);
    }

    public static void spawnExitRiftwayParticles(World world, Vec3d pos){
        //execute at @a run particle minecraft:end_rod ~ ~ ~ 0.01 1 0.01 0.1 200
        //execute at @a run particle minecraft:cloud ~ ~1 ~ 0.5 0.6 0.5 0.06 100
        //execute at @a run particle minecraft:enchant ~ ~0.5 ~ 0.2 0.6 0.2 0.15 100
        //execute at @a run particle minecraft:flash ~ ~0.5 ~ 0.2 0.7 0.2 0.15 1
        //execute at @a run particle minecraft:glow ~ ~1 ~ 0.5 0.7 0.5 0.7 100
        ((ServerWorld) world).spawnParticles(ParticleTypes.END_ROD, pos.getX(), pos.getY(), pos.getZ(), 200, 0.01, 1, 0.01, 0.1);
        ((ServerWorld) world).spawnParticles(ParticleTypes.CLOUD, pos.getX(), pos.getY()+1, pos.getZ(), 100, 0.5, 0.6, 0.5, 0.06);
        ((ServerWorld) world).spawnParticles(ParticleTypes.ENCHANT, pos.getX(), pos.getY()+0.5, pos.getZ(), 100, 0.2, 0.6, 0.2, 0.15);
        ((ServerWorld) world).spawnParticles(ParticleTypes.FLASH, pos.getX(), pos.getY()+0.5, pos.getZ(), 1, 0.2, 0.7, 0.2, 0.15);
        ((ServerWorld) world).spawnParticles(ParticleTypes.GLOW, pos.getX(), pos.getY()+0.5, pos.getZ(), 100, 0.5, 0.7, 0.5, 0.7);
    }

    public static void playTurnOffRiftway(World world, BlockPos pos){
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_PLACE, SoundCategory.BLOCKS, 1f, 1.5f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3f, 0.3f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.BLOCKS, 1f, 0.6f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS, 1f, 0.1f, false);
    }

    public static void playTurnOffRiftway(PlayerEntity player){
        player.playSound(SoundEvents.BLOCK_AMETHYST_CLUSTER_PLACE, 1, 1.5f);
        player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.3f, 0.3f);
        player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 0.6f);
        player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1, 0.1f);
    }

    public static void playSetRiftwayDestination(PlayerEntity player){
        player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.45f, 0.7f);
        player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.45f, 1.7f);
        player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1, 0.7f);
        player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1, 1.6f);
        player.playSound(SoundEvents.BLOCK_AZALEA_BREAK, 1, 1.3f);
    }

    public static void playSetRiftwayDestination(World world, BlockPos pos){
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 0.45f, 0.7f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 0.45f, 1.7f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_AZALEA_BREAK, SoundCategory.BLOCKS, 1f, 1.4f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS, 1f, 0.7f, false);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS, 1f, 1.6f, false);
    }
    
}
