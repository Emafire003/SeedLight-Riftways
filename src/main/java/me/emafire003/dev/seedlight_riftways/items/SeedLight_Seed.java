package me.emafire003.dev.seedlight_riftways.items;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeedLight_Seed extends AliasedBlockItem {


    public SeedLight_Seed(Block block, Settings settings) {
        super(block, settings);
    }

    /*@Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(!context.getWorld().isSkyVisible(context.getBlockPos())){
            return ActionResult.FAIL;
        }
        return super.useOnBlock(context);
    }*/

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.seedlight_riftways.seedlight_seed.tooltip1"));
        tooltip.add(Text.translatable("item.seedlight_riftways.seedlight_seed.tooltip2"));
    }
}
