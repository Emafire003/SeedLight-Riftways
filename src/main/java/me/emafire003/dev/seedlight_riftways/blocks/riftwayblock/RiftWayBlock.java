package me.emafire003.dev.seedlight_riftways.blocks.riftwayblock;

import me.emafire003.dev.seedlight_riftways.SeedlightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import me.emafire003.dev.seedlight_riftways.items.InterRiftwaysLeafItem;
import me.emafire003.dev.seedlight_riftways.items.SeedlightRiftwaysItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
        if(!(itemStack.getItem() instanceof InterRiftwaysLeafItem)){
            return super.onUse(state, world, pos, player, hand, hit);
        }
        if(itemStack.getNbt().get(NBT_SERVERIP_KEY) != null || !itemStack.getNbt().getString(NBT_SERVERIP_KEY).equalsIgnoreCase("")){
            return super.onUse(state, world, pos, player, hand, hit);
        }
        SERVER_IP = itemStack.getNbt().getString(NBT_SERVERIP_KEY);
        player.sendMessage(Text.literal(SeedlightRiftways.PREFIX+" §bA new destination has been set for the portal, §d").append(SERVER_IP));
        //Validate name as IP address
        if(!player.getAbilities().creativeMode){
            itemStack.decrement(1);
        }
        SeedLightRiftwaysClient.IS_RIFTWAY_ACTIVE = true;
        return ActionResult.PASS;
    }

}