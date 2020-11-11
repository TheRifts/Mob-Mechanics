package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import me.Lozke.MobMechanics;
import me.Lozke.data.MobSpawner;
import me.Lozke.managers.SpawnerManager;
import me.Lozke.utils.Text;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Set;

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
    @CommandCompletion("@mob-ids @mob-ids")
    public static void onSwap(Player player, String oldID, String newID) {
        if (!(MobMechanics.getInstance().getBaseEntityManager().isLoaded(newID))) {
            player.sendMessage(Text.colorize("&c" + newID + " is not a valid mob id"));
        }
        else {
            spawnerManager.swapID(oldID, newID);
        }
    }

    @Subcommand("debug")
    public static void onDebug(Player player) {
        Block block = player.getTargetBlockExact(50).getLocation().getBlock();
        MobSpawner spawner = spawnerManager.getSpawner(block.getLocation());
        if (spawner == null) {
            return;
        }
        player.sendMessage("Spawner Debug:");
        player.sendMessage("Entity ID: " + spawner.getEntityID());
        player.sendMessage("Spawned Mob Amount: " + spawner.getSpawnedMobsAmount() + "/" + spawner.getMaxMobAmount());
    }

    @Subcommand("forcespawn")
    @CommandAlias("forcespawnerspawn")
    public static void onForceSpawn() {
        Set<MobSpawner> spawners = spawnerManager.getSpawners();
        spawners.forEach(spawner -> spawner.setTimeLeft(0));
    }
}
