package me.emafire003.dev.seedlight_riftways.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

//Based on Factions' code https://github.com/ickerio/factions
public interface SLRCommand {
    public LiteralCommandNode<FabricClientCommandSource> getNode();

}
