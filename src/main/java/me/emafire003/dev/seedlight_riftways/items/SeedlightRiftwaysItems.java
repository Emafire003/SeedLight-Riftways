package me.emafire003.dev.seedlight_riftways.items;

import me.emafire003.dev.seedlight_riftways.SeedlightRiftways;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;



public class SeedlightRiftwaysItems {

    public static final Item LUXINTUS_BERRY = registerItem("luxintus_berry",
            new InterRiftwaysLeafItem(new FabricItemSettings().rarity(Rarity.EPIC)
                    .food(new FoodComponent.Builder().alwaysEdible().hunger(5).build()).maxCount(16).group(ItemGroup.FOOD)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(SeedlightRiftways.MOD_ID, name), item);
    }

    public static void registerItems(){
        SeedlightRiftways.LOGGER.info("Registering items...");
    }

}
