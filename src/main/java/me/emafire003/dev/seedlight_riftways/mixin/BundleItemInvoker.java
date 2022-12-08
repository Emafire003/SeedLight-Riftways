package me.emafire003.dev.seedlight_riftways.mixin;

import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(BundleItem.class)
public interface BundleItemInvoker {

    @Invoker("getBundledStacks")
    public static Stream<ItemStack> getBundledStacksInv(ItemStack stack) {
        throw new AssertionError();
    }
}
