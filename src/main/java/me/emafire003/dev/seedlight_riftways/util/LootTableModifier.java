package me.emafire003.dev.seedlight_riftways.util;

import me.emafire003.dev.seedlight_riftways.items.SeedlightRiftwaysItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;

public class LootTableModifier {

    //Generates between 1 and 2 leaves per chest with a chance of 30%
    //Generates between 1 and 2 seeds with a chance of 20% in end city treasure chests
    //Generates 1 seed with a chance of 25% in ancient cities
    //Generates 1 seed with a chance of 15% in bastion treasures
    //Then well it drops from new leaves, also TODO might be a drop of the sniffer in the future releases

    private static final Identifier ANCIENT_CITY
            = new Identifier("minecraft", "chests/ancient_city");

    private static final Identifier END_CITY_TREASURE
            = new Identifier("minecraft", "chests/end_city_treasure");

    private static final Identifier BASTION_TREASURE
            = new Identifier("minecraft", "chests/bastion_treasure");


    public static void modifyLootTables() {
        float base_chance = 0.20f;

        LootTableEvents.MODIFY.register(((resourceManager, manager, id, supplier, setter) -> {
            if (END_CITY_TREASURE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(base_chance))
                        .with(ItemEntry.builder(SeedlightRiftwaysItems.SEEDLIGHT_SEED))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                supplier.pool(poolBuilder.build());

                LootPool.Builder poolBuilder1 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(base_chance+0.10f))
                        .with(ItemEntry.builder(SeedlightRiftwaysItems.INTERRIFTWAYS_LEAF))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                supplier.pool(poolBuilder1.build());

            }
        }));

        LootTableEvents.MODIFY.register(((resourceManager, manager, id, supplier, setter) -> {
            if (ANCIENT_CITY.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(base_chance+0.05f))
                        .with(ItemEntry.builder(SeedlightRiftwaysItems.SEEDLIGHT_SEED))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                supplier.pool(poolBuilder.build());

                LootPool.Builder poolBuilder1 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(base_chance+0.10f))
                        .with(ItemEntry.builder(SeedlightRiftwaysItems.INTERRIFTWAYS_LEAF))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                supplier.pool(poolBuilder1.build());
            }
        }));

        LootTableEvents.MODIFY.register(((resourceManager, manager, id, supplier, setter) -> {
            if (BASTION_TREASURE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(base_chance-0.05f))
                        .with(ItemEntry.builder(SeedlightRiftwaysItems.SEEDLIGHT_SEED))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                supplier.pool(poolBuilder.build());

                LootPool.Builder poolBuilder1 = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(base_chance+0.05f))
                        .with(ItemEntry.builder(SeedlightRiftwaysItems.INTERRIFTWAYS_LEAF))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                supplier.pool(poolBuilder1.build());

            }
        }));

        LOGGER.info("Loot modified!");
    }
}
