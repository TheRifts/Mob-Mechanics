package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import me.Lozke.MobMechanics;
import me.Lozke.managers.SpawnerManager;
import me.Lozke.utils.Text;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("spawner|spawners")
public class Spawners extends BaseCommand {

    private static SpawnerManager spawnerManager;

    public Spawners() {
        spawnerManager = MobMechanics.getInstance().getSpawnerManager();
    }

    @Subcommand("save")
    public static void onSave() {
        spawnerManager.saveSpawners();
    }

    @Subcommand("load")
    public static void onLoad() {
        spawnerManager.loadSpawners();
    }

    @Subcommand("show")
    public static void onShow() {
        spawnerManager.showSpawners();
    }

    @Subcommand("hide")
    public static void onHide() {
        spawnerManager.hideSpawners();
    }

    @Subcommand("edit")
    public static void onEdit(Player player) {
        Location location;
        try {
            location = player.getTargetBlockExact(50).getLocation();
        } catch (NullPointerException ignore) {
            return;
        }
        if (spawnerManager.isSpawner(location)) {
            player.openInventory(spawnerManager.openGUI(location));
        }
    }

    @Subcommand("swap")
    public static void onSwap(Player player, String oldID, String newID) {
        if (!(MobMechanics.getInstance().getMobManager().isLoaded(newID))) {
            player.sendMessage(Text.colorize("&c" + newID + " is not a valid mob id"));
        }
        else {
            spawnerManager.swapID(oldID, newID);
        }
    }
}
