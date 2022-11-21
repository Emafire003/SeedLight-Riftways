package me.emafire003.dev.seedlight_riftways.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.emafire003.dev.seedlight_riftways.items.InterRiftwaysLeafItem.NBT_SERVERIP_KEY;

public class SeedLight_Seed extends Item {
    public SeedLight_Seed(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        int light_level = context.getWorld().getLightLevel(context.getBlockPos());
        context.getPlayer().sendMessage(Text.literal("LightLevel: " + light_level));
        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.seedlight_riftways.seedlight_seed.tooltip1"));
    }
}
