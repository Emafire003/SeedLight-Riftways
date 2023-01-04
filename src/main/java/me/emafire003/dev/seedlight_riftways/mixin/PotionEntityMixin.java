package me.emafire003.dev.seedlight_riftways.mixin;

import me.emafire003.dev.seedlight_riftways.blocks.seedlight_plant.SeedLightPlantBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;


@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity implements FlyingItemEntity {


    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }



    @Inject(method = "onBlockHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/PotionEntity;extinguishFire(Lnet/minecraft/util/math/BlockPos;)V"))
    protected void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        Direction direction = blockHitResult.getSide();
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(direction);
        this.waterSeedLights(blockPos2);
        this.waterSeedLights(blockPos2.offset(direction.getOpposite()));
        Iterator var9 = Direction.Type.HORIZONTAL.iterator();

        while(var9.hasNext()) {
            Direction direction2 = (Direction)var9.next();
            this.waterSeedLights(blockPos2.offset(direction2));
        }
    }

    private void waterSeedLights(BlockPos pos) {
        BlockState blockState = this.world.getBlockState(pos);
        if (blockState.getBlock() instanceof SeedLightPlantBlock) {
            SeedLightPlantBlock seedlight_plant = (SeedLightPlantBlock) blockState.getBlock();
            if(seedlight_plant.getWaterLevel() < seedlight_plant.getMaxWaterLevel()){
                seedlight_plant.setWaterLevel(seedlight_plant.getWaterLevel()+random.nextBetween(0, 3));
            }
        }

    }

}
