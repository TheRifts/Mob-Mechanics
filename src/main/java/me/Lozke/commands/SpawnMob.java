package me.Lozke.commands;

import me.Lozke.data.CustomMob;
import me.Lozke.data.Rarity;
import me.Lozke.data.Tier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMob extends Command {

    public SpawnMob() {
        super("spawn");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        new CustomMob(EntityType.valueOf(args[0]), args[1], Tier.values()[Integer.parseInt(args[2])], Rarity.values()[Integer.parseInt(args[3])], null, ((Player)sender).getLocation());
        return true;
    }
}
