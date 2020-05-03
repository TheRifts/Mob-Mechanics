package me.Lozke.data;

import me.Lozke.guis.SpawnerEditor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class MobSpawner {

    private Map<String, Object> location;
    private Tier tier;
    private Rarity rarity;
    private String mobType; //Convert this to an Object
    private boolean elite;
    private boolean spawnerActive;
    private int spawnTime;
    private int radius;
    private int amount;
    private transient int timeLeft;
    private transient BukkitTask task;


    public MobSpawner(MobSpawner mobSpawner) {
        this.location = mobSpawner.getSerializedLocation();
        this.tier = mobSpawner.getTier();
        this.rarity = mobSpawner.getRarity();
        this.mobType = mobSpawner.getMobType();
        this.elite = mobSpawner.isElite();
        this.spawnerActive = mobSpawner.isSpawnerActive();
        this.spawnTime = mobSpawner.getSpawnTime();
        this.timeLeft = spawnTime;
        this.radius = mobSpawner.getRadius();
        this.amount = mobSpawner.getAmount();
    }
    public MobSpawner(Location location, Tier tier, Rarity rarity, String mobType, boolean elite, boolean spawnerActive, int timer, int radius, int amount) {
        this.location = location.serialize();
        this.tier = tier;
        this.rarity = rarity;
        this.mobType = mobType;
        this.elite = elite;
        this.spawnerActive = spawnerActive;
        this.spawnTime = timer;
        this.timeLeft = spawnTime;
        this.radius = radius;
        this.amount = amount;
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

    public String getMobType() {
        return mobType;
    }
    public void setMobType(String mobType) {
        this.mobType = mobType;
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


    public MobSpawner copy() {
        return new MobSpawner(this);
    }

    public Inventory editor() {
        return new SpawnerEditor(this).getGui();
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
}
