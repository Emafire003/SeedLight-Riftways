package me.emafire003.dev.seedlight_riftways.items;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.blocks.SLRBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;



public class SeedlightRiftwaysItems {

    public static final Item INTERRIFTWAYS_LEAF = registerItem("inter_riftways_leaf",
            new InterRiftwaysLeafItem(new FabricItemSettings().rarity(Rarity.RARE)));

    public static final Item SEEDLIGHT_SEED = registerItem("seedlight_seed",
            new SeedLight_Seed(SLRBlocks.SEEDLIGHT_PLANT, new FabricItemSettings().rarity(Rarity.EPIC).maxCount(2)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(SeedLightRiftways.MOD_ID, name), item);
    }

    public static void registerItems(){
        SeedLightRiftways.LOGGER.info("Registering items...");
    }

}
