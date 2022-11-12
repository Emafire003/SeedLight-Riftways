package me.emafire003.dev.seedlight_riftways.items;

import me.emafire003.dev.seedlight_riftways.SeedlightRiftways;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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

    public static final String NBT_SERVERIP_KEY = SeedlightRiftways.MOD_ID + ".serverip";

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
        stack.setCustomName(Text.literal("§5§kO").append("§l"+SeedlightRiftwaysItems.INTERRIFTWAYS_LEAF.getName()).append(" §5§kO"));

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
