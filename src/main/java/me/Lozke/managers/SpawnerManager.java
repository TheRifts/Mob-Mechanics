package me.Lozke.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.Lozke.MobMechanics;
import me.Lozke.data.MobSpawner;
import me.Lozke.data.SpawnerWandPersistentDataType;
import me.Lozke.tasks.TickSpawnersTask;
import me.Lozke.utils.Logger;
import me.Lozke.utils.NamespacedKeyWrapper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;

public class SpawnerManager {

    private MobMechanics plugin;
    private TickSpawnersTask tickSpawnersTask;

    private HashSet<MobSpawner> mobSpawners = new HashSet<>();
    private boolean visible;

    public SpawnerManager(MobMechanics plugin) {
        this.plugin = plugin;
        loadSpawners();
        startSpawnerTask();
    }

    public void startSpawnerTask() {
        this.tickSpawnersTask = new TickSpawnersTask(this);
    }

    public void stopSpawnerTask() {
        if (tickSpawnersTask == null || tickSpawnersTask.isCancelled()) {
            return;
        }
        tickSpawnersTask.cancel();
    }

    public HashSet<MobSpawner> getSpawners() {
        return mobSpawners;
    }

    public void loadSpawners() {
        //TODO handle MobManager folder/file creation (probably in main onEnable)
        if (!new File(plugin.getDataFolder().getPath() + File.separator + "Spawners.json").exists()) {
            Logger.warn("No spawner file (Spawners.json) detected");
            mobSpawners = new HashSet<>();
            return;
        }
        try {
            mobSpawners = new GsonBuilder().setPrettyPrinting().create().fromJson(new FileReader(plugin.getDataFolder().getPath() + File.separator + "Spawners.json"), new TypeToken<HashSet<MobSpawner>>(){}.getType());
        } catch (FileNotFoundException exception) {
            //todo: handle this exception
            exception.printStackTrace();
        }
        if (mobSpawners == null) {
            mobSpawners = new HashSet<>();
        }
    }

    public void saveSpawners() {
        if (mobSpawners.isEmpty()) {
            Logger.log("No mobs to save to Mobs.json");
            return;
        }
        try (FileWriter writer = new FileWriter(new File(plugin.getDataFolder() + File.separator + "Spawners.json"))) {
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

    public void addSpawner(MobSpawner spawner) {
        mobSpawners.add(spawner.showSpawner());
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
        return getSpawner(location) != null;
    }

    public Inventory openGUI(Location location) {
        return getSpawner(location).editor();
    }

    public void swapID(String oldID, String newID) {
        if (!MobMechanics.getInstance().getBaseEntityManager().isLoaded(newID)) {
            Logger.log("Attempted to change spawners with ID '" + oldID + "' to '" + newID + "', but '" + newID + "' is invalid!");
            return;
        }
        for (MobSpawner spawner : mobSpawners) {
            if (spawner.getEntityID().equalsIgnoreCase(oldID)) {
                spawner.setEntityID(newID);
            }
        }
    }

    public MobSpawner parseSpawnerWandData(ItemStack stack) {
        NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(stack);
        return (MobSpawner) wrapper.get(SpawnerWandPersistentDataType.DATA_TAG, new SpawnerWandPersistentDataType());
    }
}
