package me.emafire003.dev.seedlight_riftways.blocks.riftwayblock;

import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RiftWayBlock extends BlockWithEntity {
    public RiftWayBlock(Settings settings) {
        super(settings);
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof RiftWayBlockEntity) {
            int i = ((RiftWayBlockEntity)blockEntity).getDrawnSidesCount();

            for(int j = 0; j < i; ++j) {
                double d = (double)pos.getX() + random.nextDouble();
                double e = (double)pos.getY() + random.nextDouble();
                double f = (double)pos.getZ() + random.nextDouble();
                double g = (random.nextDouble() - 0.5D) * 0.5D;
                double h = (random.nextDouble() - 0.5D) * 0.5D;
                double k = (random.nextDouble() - 0.5D) * 0.5D;
                int l = random.nextInt(2) * 2 - 1;
                if (random.nextBoolean()) {
                    f = (double)pos.getZ() + 0.5D + 0.25D * (double)l;
                    k = (random.nextFloat() * 2.0F * (float)l);
                } else {
                    d = (double)pos.getX() + 0.5D + 0.25D * (double)l;
                    g = (random.nextFloat() * 2.0F * (float)l);
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
        return checkType(type, SLRBlocks.SQUARE_PORTAL_BLOCKENTITY, world.isClient ? RiftWayBlockEntity::clientTick : RiftWayBlockEntity::serverTick);
    }

   /* public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isOf(Items.PAPER) && !itemStack.isOf(Items.FIRE_CHARGE)) {
            return super.onUse(state, world, pos, player, hand, hit);
        } else {
            primeTnt(world, pos, player);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            Item item = itemStack.getItem();
            if (!player.isCreative()) {
                if (itemStack.isOf(Items.FLINT_AND_STEEL)) {
                    itemStack.damage(1, player, (playerx) -> {
                        playerx.sendToolBreakStatus(hand);
                    });
                } else {
                    itemStack.decrement(1);
                }
            }

            player.incrementStat(Stats.USED.getOrCreateStat(item));
            return ActionResult.success(world.isClient);
        }
    }*/
}