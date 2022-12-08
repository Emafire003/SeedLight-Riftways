package me.emafire003.dev.seedlight_riftways.blocks.riftwayblock;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import me.emafire003.dev.seedlight_riftways.items.InterRiftwaysLeafItem;
import me.emafire003.dev.seedlight_riftways.mixin.BundleItemInvoker;
import me.emafire003.dev.seedlight_riftways.util.CheckValidAddress;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BundleItem;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient.SERVER_IP;
import static me.emafire003.dev.seedlight_riftways.items.InterRiftwaysLeafItem.NBT_SERVERIP_KEY;

public class RiftWayBlock extends BlockWithEntity {
    public RiftWayBlock(Settings settings) {
        super(settings);
    }


    //Display particles
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof RiftWayBlockEntity) {
            int i = ((RiftWayBlockEntity) blockEntity).getDrawnSidesCount();

            for (int j = 0; j < i; ++j) {
                double d = (double) pos.getX() + random.nextDouble();
                double e = (double) pos.getY() + random.nextDouble();
                double f = (double) pos.getZ() + random.nextDouble();
                double g = (random.nextDouble() - 0.5D) * 0.5D;
                double h = (random.nextDouble() - 0.5D) * 0.5D;
                double k = (random.nextDouble() - 0.5D) * 0.5D;
                int l = random.nextInt(2) * 2 - 1;
                if (random.nextBoolean()) {
                    f = (double) pos.getZ() + 0.5D + 0.25D * (double) l;
                    k = (random.nextFloat() * 2.0F * (float) l);
                } else {
                    d = (double) pos.getX() + 0.5D + 0.25D * (double) l;
                    g = (random.nextFloat() * 2.0F * (float) l);
                }

                //TODO add new particles
                world.addParticle(ParticleTypes.DRIPPING_WATER, d, e, f, g, h, k);
            }

        }
    }

    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    public boolean canBucketPlace(BlockState state, Fluid fluid) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RiftWayBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, SLRBlocks.RIFTWAY_BLOCKENTITY, world.isClient ? RiftWayBlockEntity::clientTick : RiftWayBlockEntity::serverTick);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);

        if((itemStack.getItem() instanceof InterRiftwaysLeafItem)){
            if(itemStack.getNbt().get(NBT_SERVERIP_KEY) == null || itemStack.getNbt().getString(NBT_SERVERIP_KEY).equalsIgnoreCase("")){
                return super.onUse(state, world, pos, player, hand, hit);
            }

            //SERVER BITS
            if(!player.getWorld().isClient){
                SeedLightRiftways.SAVED_SERVER_IP = itemStack.getNbt().getString(NBT_SERVERIP_KEY);
                //TODO lang translatable
                player.sendMessage(Text.literal(SeedLightRiftways.PREFIX+" §bA new destination has been set for the portal, §d" + SeedLightRiftways.SAVED_SERVER_IP));
                SeedLightRiftways.IS_RIFTWAY_ACTIVE = true;
                //Updates the saved stuff
                SeedLightRiftways.updateConfig();
                SeedLightRiftways.sendUpdateRiftwayToPlayers(player.getServer());
                player.getWorld().playSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, SoundCategory.AMBIENT, 0.3f, 0.3f, true);
                player.getWorld().playSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.AMBIENT, 1f, 0.5f, true);
                player.getWorld().playSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.AMBIENT, 1f, 1.7f, true);
                SeedLightRiftways.addRiftwayLocation(false, pos);
            }//CLIENT ONLY BITS
            else{
                SeedLightRiftwaysClient.SERVER_IP = itemStack.getNbt().getString(NBT_SERVERIP_KEY);
                CheckValidAddress checkAddress = new CheckValidAddress();
                checkAddress.setAddress(SERVER_IP);
                checkAddress.setPlayer(player);
                checkAddress.start();
                SeedLightRiftwaysClient.IS_RIFTWAY_ACTIVE = true;
            }
            if(!player.getAbilities().creativeMode){
                itemStack.decrement(1);
            }

            return ActionResult.PASS;
        }else if(itemStack.getItem() instanceof FireChargeItem){
            if(!player.getWorld().isClient){
                //TODO lang translatable
                player.sendMessage(Text.literal(SeedLightRiftways.PREFIX+" §bThe riftway has been deactivated!"));
                SeedLightRiftways.updateConfig();
            }
            //Validate name as IP address
            if(!player.getAbilities().creativeMode){
                itemStack.decrement(1);
            }
            //SERVER BITS
            if(!player.getWorld().isClient){
                SeedLightRiftways.IS_RIFTWAY_ACTIVE = false;
                SeedLightRiftways.removeRiftwayLocation(false, pos);
                SeedLightRiftways.updateConfig();
                SeedLightRiftways.sendUpdateRiftwayToPlayers(Objects.requireNonNull(player.getServer()));
            }//CLIENT BITS
            else{
                //Probably redundant since the packets
                SeedLightRiftwaysClient.IS_RIFTWAY_ACTIVE = false;
            }

            return ActionResult.PASS;
        }else if(itemStack.getItem() instanceof BundleItem && player.hasPermissionLevel(2)){
            //BundleItemInvoker bundle = (BundleItemInvoker) itemStack.getItem();
            Stream<ItemStack> items = BundleItemInvoker.getBundledStacksInv(itemStack);
            SeedLightRiftways.SERVER_RIFTWAY_ITEMS_PASSWORD.clear();
            items.forEach(itemStack1 -> {
                SeedLightRiftways.SERVER_RIFTWAY_ITEMS_PASSWORD.add(itemStack1.getItem().getName().getString().toString());
            });
            player.sendMessage(Text.literal(SeedLightRiftways.PREFIX+" §bThe item combination to access this world has been changed!"));
            player.sendMessage(Text.literal(SeedLightRiftways.PREFIX+" §bIt now is: §a" + SeedLightRiftways.SERVER_RIFTWAY_ITEMS_PASSWORD.toString()));
            SeedLightRiftways.updateConfig();
        }
        return super.onUse(state, world, pos, player, hand, hit);

    }

}