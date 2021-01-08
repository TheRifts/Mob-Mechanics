package me.Lozke.tasks;

import me.Lozke.MobMechanics;
import me.Lozke.RiftsCore;
import me.Lozke.data.*;
import me.Lozke.managers.SpawnerManager;
import me.Lozke.utils.NumGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class TickSpawnersTask extends BukkitRunnable {

    private SpawnerManager spawnerManager;

    public TickSpawnersTask(SpawnerManager spawnerManager) {
        this.spawnerManager = spawnerManager;
        runTaskTimerAsynchronously(MobMechanics.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
        if (spawnerManager.getSpawners() == null || spawnerManager.getSpawners().isEmpty()) {
            return;
        }
        for (MobSpawner spawner : spawnerManager.getSpawners()) {
            if (!spawner.isSpawnerActive()) {
                continue;
            }
            int timeLeft = spawner.getTimeLeft();
            boolean isChunkLoaded = RiftsCore.getPluginInstance().getChunkManager().isChuckLoaded(spawner.getLocation(), spawner.getChunkKey());
            if (timeLeft == 0 && spawner.getSpawnedMobsAmount() < spawner.getMaxMobAmount() && isChunkLoaded && arePlayersNearby(spawner.getLocation(), spawner.getActiveRange())) {
                for (int spawnedMobAmount = 0; spawnedMobAmount < spawner.getAmount() && spawner.getSpawnedMobsAmount() < spawner.getMaxMobAmount(); spawnedMobAmount++) {
                    double a = NumGenerator.fraction() * 2 * Math.PI;
                    double dist = NumGenerator.fraction() * spawner.getRadius();
                    Location loc = null;
                    while (loc == null) {
                        loc = assureValidLocation(spawner.getLocation().clone().add(dist * Math.sin(a), 0, dist * Math.cos(a)).add(0.5, 0, 0.5));
                    }
                    syncSpawnMob(spawner, loc);
                    spawner.setSpawnedMobsAmount(spawner.getSpawnedMobsAmount() + 1);
                }
                timeLeft = spawner.getSpawnTime()-1;
            }
            else if (timeLeft > 0){ //Using else to guarantee spawner continues ticking if not at 0.
                timeLeft = timeLeft-1;
            }
            spawner.setTimeLeft(timeLeft);
        }
    }

    private boolean arePlayersNearby(Location location, double range) {
        if (range == 0) { //If default then ignore checking for nearby players
            return true;
        }
        Set<Player> players = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() != location.getWorld()) {
                continue;
            }
            if (player.getLocation().distanceSquared(location) <= Math.pow(range, 2)) {
                players.add(player);
            }
        }
        return !players.isEmpty();
    }

    private Location assureValidLocation(Location location) {
       for (int i = 0; i < 3; i++) {
           Location clonedLoc = location.clone();
           clonedLoc.add(0, i, 0);
           boolean isValid = true;
           for (int j = 0; j < 2; j++) {
               clonedLoc.add(0, j, 0);
               if (clonedLoc.getBlock().getType().isSolid()) {
                   isValid = false;
               }
           }
           if (isValid) {
               location.add(0, i, 0);
               return location;
           }
       }
       return null;
    }

    private void syncSpawnMob(MobSpawner spawner, Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                RiftsMob mob = MobMechanics.getInstance().getBaseEntityManager().spawnBaseEntity(spawner, location);
                if (mob == null) return;
                MobMechanics.getInstance().getMobManager().trackEntity(mob);
                mob.setSpawner(spawner);
            }
        }.runTask(MobMechanics.getInstance());
    }
}
