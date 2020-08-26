package me.Lozke.data;

import me.Lozke.menus.SpawnerEditor.pages.SpawnerEditorMenu;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class MobSpawner {
    private Map<String, Object> location;
    private Tier tier;
    private Rarity rarity;
    private String entityID;
    private boolean elite;
    private boolean spawnerActive;
    private int spawnTime;
    private int radius;
    private int spawnAmount;
    private int timeLeft;
    private int maxMobAmount;

    private transient int spawnedMobsAmount;

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
        this.spawnAmount = mobSpawner.getAmount();
        this.maxMobAmount = mobSpawner.getMaxMobAmount();
    }

    public MobSpawner(Location location, Tier tier, Rarity rarity, String entityID, boolean elite, boolean spawnerActive, int timer, int radius, int spawnAmount, int maxMobAmount) {
        this.location = location.serialize();
        this.tier = tier;
        this.rarity = rarity;
        this.entityID = entityID;
        this.elite = elite;
        this.spawnerActive = spawnerActive;
        this.spawnTime = timer;
        this.timeLeft = spawnTime;
        this.radius = radius;
        this.spawnAmount = spawnAmount;
        this.maxMobAmount = maxMobAmount;
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
        return spawnAmount;
    }
    public void setAmount(int spawnAmount) {
        this.spawnAmount = spawnAmount;
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
        return spawnedMobsAmount < spawnAmount;
    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public void setSpawnedMobsAmount(int amount) {
        this.spawnedMobsAmount = amount;
    }

    public int getSpawnedMobsAmount() {
        return spawnedMobsAmount;
    }

    public void setMaxMobAmount(int maxMobAmount) {
        this.maxMobAmount = maxMobAmount;
    }
    public int getMaxMobAmount() {
        return maxMobAmount;
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
