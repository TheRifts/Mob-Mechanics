package me.Lozke.tasks;

import me.Lozke.MobMechanics;
import me.Lozke.data.MobSpawner;
import me.Lozke.managers.SpawnerManager;
import me.Lozke.utils.NumGenerator;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

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
            if (timeLeft == 0 && spawner.getSpawnedMobsAmount() < spawner.getMaxMobAmount()) {
                for (int i = 0; i < spawner.getAmount() && spawner.getSpawnedMobsAmount() < spawner.getMaxMobAmount(); i++) {
                    double a = NumGenerator.fraction() * 2 * Math.PI;
                    double dist = NumGenerator.fraction() * spawner.getRadius();
                    Location loc = spawner.getLocation().clone().add(dist * Math.sin(a), 0, dist * Math.cos(a)).add(0.5, 0, 0.5);
                    syncSpawnMob(spawner, loc);
                }
                timeLeft = spawner.getSpawnTime()-1;
            }
            else if (timeLeft > 0){ //Using else to guarantee spawner continues ticking if not at 0.
                timeLeft = timeLeft-1;
            }
            spawner.setTimeLeft(timeLeft);
        }
    }

    private void syncSpawnMob(MobSpawner spawner, Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                MobMechanics.getInstance().getMobManager().spawnMob(spawner, location);
            }
        }.runTask(MobMechanics.getInstance());
    }
}
