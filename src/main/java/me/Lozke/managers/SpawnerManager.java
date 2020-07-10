package me.Lozke.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.Lozke.MobMechanics;
import me.Lozke.data.MobSpawner;
import me.Lozke.data.Rarity;
import me.Lozke.data.Tier;
import me.Lozke.tasks.TickSpawnersTask;
import me.Lozke.utils.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.io.*;
import java.util.*;

public class SpawnerManager {

    private MobMechanics plugin;

    private HashSet<MobSpawner> mobSpawners = new HashSet<>();
    private boolean visible;

    public SpawnerManager(MobMechanics plugin) {
        this.plugin = plugin;
        loadSpawners();
        new TickSpawnersTask(this);
    }

    public HashSet<MobSpawner> getSpawners() {
        return mobSpawners;
    }

    public void loadSpawners() {
        if (!new File(plugin.getDataFolder().getPath() + "/Spawners.json").exists()) {
            Logger.log("No spawners to load from Spawners.json");
            return;
        }
        try {
            mobSpawners = new GsonBuilder().setPrettyPrinting().create().fromJson(new FileReader(plugin.getDataFolder().getPath() + "/Spawners.json"), new TypeToken<HashSet<MobSpawner>>(){}.getType());
        } catch (FileNotFoundException exception) {
            //todo: handle this exception
            exception.printStackTrace();
        }
    }

    public void saveSpawners() {
        if (mobSpawners.isEmpty()) {
            Logger.log("No mobs to save to Mobs.json");
            return;
        }
        try (FileWriter writer = new FileWriter(new File(plugin.getDataFolder() + "/Spawners.json"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(mobSpawners, writer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    //Make this Async
    //Make this show only to players with permission
    public void showSpawners() {
        visible = true;
        for (MobSpawner spawner : mobSpawners) {
            spawner.showSpawner();
        }
    }

    //Make this Async
    public void hideSpawners() {
        visible = false;
        for (MobSpawner spawner : mobSpawners) {
            spawner.hideSpawner();
        }
    }

    public boolean visible() {
        return visible;
    }

    public void createSpawner(Location location) {
        mobSpawners.add(new MobSpawner(location, Tier.T1, Rarity.ANCIENT, "HOGLIN", false, true, 10, 4, 3).showSpawner());
    }

    //Make this async?
    public MobSpawner getSpawner(Location location) {
        for (MobSpawner spawner : mobSpawners) {
            if (spawner.getLocation().equals(location)) {
                return spawner;
            }
        }
        return null;
    }

    public void removeSpawner(Location location) {
        if (isSpawner(location)) {
            location.getBlock().setType(Material.AIR);
            mobSpawners.remove(getSpawner(location));
        }
    }

    //This method needs a better naming convention (or does it?)
    public boolean isSpawner(Location location) {
        return getSpawners() != null;
    }

    public Inventory openGUI(Location location) {
        return getSpawner(location).editor();
    }
}
