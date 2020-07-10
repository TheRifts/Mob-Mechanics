package me.Lozke.data;

import me.Lozke.menus.SpawnerEditor.pages.SpawnerEditorMenu;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MobSpawner {
    private Map<String, Object> location;
    private Tier tier;
    private Rarity rarity;
    private String entityID;
    private boolean elite;
    private boolean spawnerActive;
    private int spawnTime;
    private int radius;
    private int amount;
    private int timeLeft;

    private transient Set<CustomMob> spawnedMobs;

    public MobSpawner(MobSpawner mobSpawner) {
        this.location = mobSpawner.getSerializedLocation();
        this.tier = mobSpawner.getTier();
        this.rarity = mobSpawner.getRarity();
        this.entityID = mobSpawner.getEntityID();
        this.elite = mobSpawner.isElite();
        this.spawnerActive = mobSpawner.isSpawnerActive();
        this.spawnTime = mobSpawner.getSpawnTime();
        this.timeLeft = spawnTime;
        this.radius = mobSpawner.getRadius();
        this.amount = mobSpawner.getAmount();
        this.spawnedMobs = new HashSet<>();
    }

    public MobSpawner(Location location, Tier tier, Rarity rarity, String entityID, boolean elite, boolean spawnerActive, int timer, int radius, int amount) {
        this.location = location.serialize();
        this.tier = tier;
        this.rarity = rarity;
        this.entityID = entityID;
        this.elite = elite;
        this.spawnerActive = spawnerActive;
        this.spawnTime = timer;
        this.timeLeft = spawnTime;
        this.radius = radius;
        this.amount = amount;
        this.spawnedMobs = new HashSet<>();
    }

    public Location getLocation() {
        return Location.deserialize(location);
    }
    public Map<String, Object> getSerializedLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location.serialize();
    }
    public void setLocation(Map<String, Object> serialiezedLocation) {
        this.location = serialiezedLocation;
    }

    public Tier getTier() {
        return tier;
    }
    public void setTier(Tier tier) {
        this.tier = tier;
        showSpawner();
    }

    public Rarity getRarity() {
        return rarity;
    }
    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public boolean isElite() {
        return elite;
    }
    public void toggleElite() {
        this.elite = !elite;
        showSpawner();
    }

    public boolean isSpawnerActive() {
        return spawnerActive;
    }
    public void toggleSpawnerActive() {
        this.spawnerActive = !spawnerActive;
    }

    public int getSpawnTime() {
        return spawnTime;
    }
    public void setSpawnTime(int time) {
        this.spawnTime = time;
        if(timeLeft>spawnTime) {
            timeLeft = time;
        }
    }

    public int getTimeLeft() {
        return timeLeft;
    }
    public void setTimeLeft(int time) {
        this.timeLeft = time;
    }

    public int getRadius() {
        return radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    //Does not copy associated spawned mobs
    public MobSpawner copy() {
        return new MobSpawner(this);
    }

    public Inventory editor() {
        return new SpawnerEditorMenu(this).getInventory();
    }

    public MobSpawner showSpawner() {
        if (elite) {
            Location.deserialize(location).getBlock().setType(Material.getMaterial(tier.getMaterialColor() + "_STAINED_GLASS"));
        } else {
            Location.deserialize(location).getBlock().setType(Material.getMaterial(tier.getMaterialColor() + "_CONCRETE"));
        }
        return this;
    }
    public MobSpawner hideSpawner() {
        Location.deserialize(location).getBlock().setType(Material.AIR);
        return this;
    }

    public boolean canSpawn() {
        return spawnedMobs.size() < amount;
    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    /*
    public void addMob(CustomMob mob) {
        spawnedMobs.add(mob);
    }
    public void removeMob(UUID uuid) {
        spawnedMobs.removeIf(mob -> uuid == mob.getUniqueId());
    }
    public CustomMob getCustomMob(UUID uuid) {
        for (CustomMob mob : spawnedMobs) {
            if (uuid == mob.getUniqueId()) {
                return mob;
            }
        }
        return null;
    }
     */
}
