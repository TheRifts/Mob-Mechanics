package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("spawn")
public class SpawnMob extends BaseCommand {

    @Default
    public boolean onSpawnMob(Player player, String[] args) {
        //new CustomMob(EntityType.valueOf(args[0]), args[1], Tier.values()[Integer.parseInt(args[2])], Rarity.values()[Integer.parseInt(args[3])], null, player.getLocation());
        return true;
    }
}
