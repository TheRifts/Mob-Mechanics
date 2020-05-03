package me.Lozke.commands;

import me.Lozke.data.Mob;
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
        EntityType entityType = EntityType.valueOf(args[0]);
        new Mob(entityType, args[1], args[2], Tier.valueOf(args[3])).spawnMob(((Player) sender).getLocation());
        return true;
    }
}
