package me.Lozke.managers;

import me.Lozke.MobMechanics;
import me.Lozke.data.CustomMob;
import me.Lozke.data.MobSpawner;

import java.util.*;

public class MobManager {

    private MobMechanics plugin;

    private Map<UUID, MobSpawner> mobHomes;
    private Map<UUID, CustomMob> homelessMobs;

    public MobManager(MobMechanics plugin) {
        this.plugin = plugin;

        mobHomes = new HashMap<>();
        homelessMobs = new HashMap<>();
    }

    public MobSpawner getSpawner(CustomMob customMob) {
        return customMob.getSpawner();
    }

    public MobSpawner getSpawner(UUID uuid) {
        return mobHomes.get(uuid);
    }

    public void addMob(CustomMob mob) {
        MobSpawner mobSpawner = mob.getSpawner();
        mobHomes.put(mob.getUniqueId(), mobSpawner);
        if (mobSpawner != null) {
            mobSpawner.addMob(mob);
        }
        else {
            homelessMobs.put(mob.getUniqueId(), mob);
        }
    }

    public void removeMob(UUID uuid) {
        MobSpawner mobSpawner = mobHomes.remove(uuid);
        if(mobSpawner != null) {
            mobSpawner.removeMob(uuid);
        }
        else {
            homelessMobs.remove(uuid);
        }
    }

    public Set<UUID> getAllUniqueIds() {
        Set<UUID> allUniqueIds = new HashSet<>();
        allUniqueIds.addAll(mobHomes.keySet());
        allUniqueIds.addAll(homelessMobs.keySet());
        return allUniqueIds;
    }

    public Set<CustomMob> getAllCustomMobs() {
        HashSet<CustomMob> allCustomMobs = new HashSet<>();
        for (UUID uuid : getAllUniqueIds()) {
            CustomMob customMob = getCustomMob(uuid);
            if (customMob != null) {
                allCustomMobs.add(customMob);
            }
        }
        return allCustomMobs;
    }

    public CustomMob getCustomMob(UUID uuid) {
        MobSpawner mobSpawner = mobHomes.get(uuid);
        if (mobSpawner != null) {
            return mobSpawner.getCustomMob(uuid);
        }
        else {
            return homelessMobs.get(uuid);
        }
    }
}
