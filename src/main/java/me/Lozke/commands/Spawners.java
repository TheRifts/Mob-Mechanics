package me.Lozke.commands;

import me.Lozke.MobMechanics;
import me.Lozke.managers.SpawnerManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawners extends Command {

    private SpawnerManager spawnerManager;

    public Spawners() {
        super("spawners");
        this.spawnerManager = MobMechanics.getInstance().getSpawnerManager();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        switch (args[0]) {
            case "save":
                spawnerManager.saveSpawners();
                break;
            case "load":
                spawnerManager.loadSpawners();
                break;
            case "show":
                spawnerManager.showSpawners();
                break;
            case "hide":
                spawnerManager.hideSpawners();
                break;
            case "edit":
                Player player = (Player) sender;
                Location location;
                try {
                    location = player.getTargetBlockExact(50).getLocation();
                } catch (NullPointerException ignore) {
                    break;
                }
                if (spawnerManager.isSpawner(location)) {
                    player.openInventory(spawnerManager.openGUI(location));
                }
                break;
        }
        return true;
    }
}
