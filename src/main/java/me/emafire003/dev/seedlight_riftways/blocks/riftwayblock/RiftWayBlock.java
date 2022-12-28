package me.emafire003.dev.seedlight_riftways.blocks.riftwayblock;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.SeedLightRiftwaysClient;
import me.emafire003.dev.seedlight_riftways.items.InterRiftwaysLeafItem;
import me.emafire003.dev.seedlight_riftways.mixin.BundleItemInvoker;
import me.emafire003.dev.seedlight_riftways.util.CheckValidAddress;
import me.emafire003.dev.seedlight_riftways.util.INamedSeverWorld;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BundleItem;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
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
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftwaysClient.SERVER_IP;
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

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        /*Thread thread = new Thread(() -> addNewRiftLoc(6, pos, world)); // It needs to check 200+ blocks after all
        thread.setName("riftloc_check");
        thread.start();*/
        addNewRiftLoc(10, pos, world);
        super.onPlaced(world, pos, state, placer, itemStack);
    }


    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        removeRiftwayLocation(10, pos, (World) world);
        super.onBroken(world, pos, state);
    }

    /**Checks of other blocks of the same type as this one close by
     * If there are returns false, meaning it can be considered as a new Riftway in on itself
     * And it also adds its position to the list
     *
     * */
    public boolean addNewRiftLoc(int rad, BlockPos origin, World world){
        SeedLightRiftways.LOGGER.debug("Checking if adding a new riftloc is possible");

        for(int y = -rad; y <= rad; y++)
        {
            for(int x = -rad; x <= rad; x++)
            {
                for(int z = -rad; z <= rad; z++)
                {
                    BlockPos pos = origin.add(x, y, z);
                    if(!pos.equals(origin) && world.getBlockState(pos).getBlock() instanceof RiftWayBlock){
                        SeedLightRiftways.LOGGER.debug("Nope, another riftblock found at: " + pos.toString());
                        return false;
                    }

                }
            }
        }
        //If no match has been found, return true.
        SeedLightRiftways.addRiftwayLocation(false, origin, world);
        return true;
    }



    /**This method is called when the block breaks, it checks for other Riftway Blocks
     * nearby and if there are moves the RiftwayLoc on the first it finds. Otherwise, it simply removes the Riftway Location*/
    private void removeRiftwayLocation(int rad, BlockPos origin, World world){
        for(int y = -rad; y <= rad; y++)
        {
            for(int x = -rad; x <= rad; x++)
            {
                for(int z = -rad; z <= rad; z++)
                {
                    BlockPos pos = origin.add(x, y, z);
                    if(!pos.equals(origin) && world.getBlockState(pos).getBlock() instanceof RiftWayBlock){
                        SeedLightRiftways.addRiftwayLocation(false, pos, world); //Adds the new one
                        SeedLightRiftways.removeRiftwayLocation(false, origin, world); //Removes the old one
                        return;
                    }

                }
            }
        }
        SeedLightRiftways.removeRiftwayLocation(false, origin, world);
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

                ServerWorld sworld = ((ServerWorld) player.getWorld());
                RiftWayBlockEntity.playSetRiftwayDestination(sworld, pos);
                //If the server is NOT dedicated, it means it's integrated which means it's in singleplayer
                if(!sworld.getServer().isDedicated()){
                    SeedLightRiftways.setIsRiftwayActiveInWorld(true, ((INamedSeverWorld)sworld).getLevelName());
                }
            }//CLIENT ONLY BITS
            else{
                SeedLightRiftwaysClient.SERVER_IP = itemStack.getNbt().getString(NBT_SERVERIP_KEY);
                CheckValidAddress checkAddress = new CheckValidAddress();
                checkAddress.setAddress(SERVER_IP);
                checkAddress.setPlayer(player);
                checkAddress.start();
                RiftWayBlockEntity.playSetRiftwayDestination(player);
                SeedLightRiftwaysClient.IS_RIFTWAY_ACTIVE = true;
            }
            if(!player.getAbilities().creativeMode){
                itemStack.decrement(1);
            }

            return ActionResult.PASS;
        }else if(itemStack.getItem() instanceof FireChargeItem){
            //Validate name as IP address
            if(!player.getAbilities().creativeMode){
                itemStack.decrement(1);
            }
            //SERVER BITS
            if(!player.getWorld().isClient){
                SeedLightRiftways.IS_RIFTWAY_ACTIVE = false;
                ServerWorld sworld = ((ServerWorld) player.getWorld());
                RiftWayBlockEntity.playTurnOffRiftway(sworld, pos);
                //If the server is NOT dedicated, it means it's integrated which means it's in singleplayer
                if(!sworld.getServer().isDedicated()){
                    SeedLightRiftways.setIsRiftwayActiveInWorld(false, ((INamedSeverWorld)sworld).getLevelName());
                }
                SeedLightRiftways.removeRiftwayLocation(false, pos, world);
                //TODO lang translatable
                player.sendMessage(Text.literal(SeedLightRiftways.PREFIX+" §bThe riftway has been deactivated!"));
                SeedLightRiftways.updateConfig();
                SeedLightRiftways.sendUpdateRiftwayToPlayers(Objects.requireNonNull(player.getServer()));
            }//CLIENT BITS
            else{
                //Probably redundant since the packets
                RiftWayBlockEntity.playTurnOffRiftway(player);
                SeedLightRiftwaysClient.IS_RIFTWAY_ACTIVE = false;
            }

            return ActionResult.PASS;
        }else if(itemStack.getItem() instanceof BundleItem && player.hasPermissionLevel(2) && !world.isClient){
            //BundleItemInvoker bundle = (BundleItemInvoker) itemStack.getItem();
            RiftWayBlockEntity.playSetRiftwayDestination(player);
            List<ItemStack> items = BundleItemInvoker.getBundledStacksInv(itemStack).toList();
            SeedLightRiftways.SERVER_RIFTWAY_ITEMS_PASSWORD.clear();
            if(items.isEmpty()){
                SeedLightRiftways.REQUIRES_PASSWORD = false;
                player.sendMessage(Text.literal(SeedLightRiftways.PREFIX+" §cRemoved the item combination to access this world!"));
            }else{
                try{
                    for(ItemStack item : items){
                        SeedLightRiftways.SERVER_RIFTWAY_ITEMS_PASSWORD.add(item.getItem().getName().getString().toString());
                    }
                    SeedLightRiftways.REQUIRES_PASSWORD = true;
                    player.sendMessage(Text.literal(SeedLightRiftways.PREFIX+" §bThe item combination to access this world has been changed!"));
                    player.sendMessage(Text.literal(SeedLightRiftways.PREFIX+" §bIt now is: §a" + SeedLightRiftways.SERVER_RIFTWAY_ITEMS_PASSWORD.toString()));
                }catch (Exception e){
                    player.sendMessage(Text.literal(SeedLightRiftways.PREFIX+" §4An error occurred! Check the logs!"));
                    e.printStackTrace();
                }

            }
            SeedLightRiftways.updateConfig();
        }
        return super.onUse(state, world, pos, player, hand, hit);

    }

}