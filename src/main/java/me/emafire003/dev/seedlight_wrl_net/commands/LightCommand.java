package me.emafire003.dev.seedlight_wrl_net.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientCommandSource;

//Based on Factions' code https://github.com/ickerio/factions
public interface LightCommand {
    public LiteralCommandNode<FabricClientCommandSource> getNode();

}
