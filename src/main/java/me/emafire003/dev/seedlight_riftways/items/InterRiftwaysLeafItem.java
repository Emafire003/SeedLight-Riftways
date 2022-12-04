package me.emafire003.dev.seedlight_riftways.items;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//It is going to be an item which whill have some data assigned to it. The data is composed of
// server ip. And maybe server name? The data gets assigned in the anvil maybe.

//it is a leaf which sprouted from another seed, so it can point back to that seed. So, it is
//kind of a coordiante system.
//maybe like a lodestone compass?
//Problem is how to assign the value form in-game. Maybe a new block like riftwaylocator? But i'd need a screen for that


//The seed item may be either found in the end cities or crafted with 2 beacons a nether star and seeds
public class InterRiftwaysLeafItem extends Item {

    public static final String NBT_SERVERIP_KEY = SeedLightRiftways.MOD_ID + ".serverip";

    public InterRiftwaysLeafItem(Settings settings) {
        super(settings);
    }


    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.hasNbt();
    }

    public static void setServerIpAddressNBT(ItemStack stack, PlayerEntity player){
        if(stack.getNbt().get(NBT_SERVERIP_KEY) != null || !stack.getNbt().getString(NBT_SERVERIP_KEY).equalsIgnoreCase("")){
            return;
        }
        NbtCompound nbt = new NbtCompound();
        nbt.putString(NBT_SERVERIP_KEY, stack.getName().getString());
        stack.setNbt(nbt);
        stack.removeCustomName();
        stack.setCustomName(Text.literal("§5§kO§l ").append(SeedlightRiftwaysItems.INTERRIFTWAYS_LEAF.getName()).append(" §5§kO"));

    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        try{
            //Spawns the first bit of particles
            /*for(Vec3d pos : RiftwayParticlesUtil.getRectanglePerimeter(1, 1, user.getPos().add(0, 1 ,0), 0.4, 0.4, 30)){
                world.addParticle(RiftParticles.RIFTWAY_BEGIN_TELEPORT_PARTICLE, pos.x, pos.y+(user.getRandom().nextInt(10) / 10), pos.z, 0,0,0);
            }

            //The lowest point of the partucles
            for(Vec3d pos : RiftwayParticlesUtil.getRectanglePerimeter(1, 1, user.getPos().add(0, 0.25 ,0), 0.6, 0.6, 30)){
                world.addParticle(RiftParticles.RIFTWAY_BEGIN_TELEPORT_PARTICLE, pos.x, pos.y+(user.getRandom().nextInt(10) / 10), pos.z, 0,0,0);
            }

            for(Vec3d pos : RiftwayParticlesUtil.getRectanglePerimeter(1, 1, user.getPos().add(0, 0.45 ,0), 0.55, 0.55, 30)){
                world.addParticle(RiftParticles.RIFTWAY_BEGIN_TELEPORT_PARTICLE, pos.x, pos.y+(user.getRandom().nextInt(10) / 10), pos.z, 0,0,0);
            }

            for(Vec3d pos : RiftwayParticlesUtil.getRectanglePerimeter(1, 1, user.getPos().add(0, 0.65 ,0), 0.48, 0.48, 30)){
                world.addParticle(RiftParticles.RIFTWAY_BEGIN_TELEPORT_PARTICLE, pos.x, pos.y+(user.getRandom().nextInt(10) / 10), pos.z, 0,0,0);
            }

            for(Vec3d pos : RiftwayParticlesUtil.getRectanglePerimeter(1, 1, user.getPos().add(0,  1.25,0), 0.3, 0.3, 30)){
                world.addParticle(RiftParticles.RIFTWAY_BEGIN_TELEPORT_PARTICLE, pos.x, pos.y+(user.getRandom().nextInt(10) / 10), pos.z, 0,0,0);
            }*/
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(!Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.seedlight_riftways.inter_riftways_leaf.tooltip1"));
        } else if(stack.hasNbt()) {
            tooltip.add(Text.translatable("item.seedlight_riftways.inter_riftways_leaf.tooltip1")
                    .append(stack.getNbt().getString(NBT_SERVERIP_KEY)));
        }
    }
}
