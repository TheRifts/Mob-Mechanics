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
            if (!spawner.isSpawnerActive()) { //return method, if spawner is off.
                continue;
            }
            int timeLeft = spawner.getTimeLeft();
            if (timeLeft == 0) {
                Location[] locations = new Location[spawner.getAmount()];
                for (int i = 0; i < spawner.getAmount(); i++) {
                    double a = NumGenerator.fraction() * 2 * Math.PI;
                    double dist = NumGenerator.fraction() * spawner.getRadius();
                    locations[i] = spawner.getLocation().clone().add(dist * Math.sin(a), 0, dist * Math.cos(a)).add(0.5, 0, 0.5);
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Location location : locations) {
                            MobMechanics.getInstance().getMobManager().spawnMob(spawner.getTier(), spawner.getRarity(), spawner.getEntityID(), location);
                        }
                    }
                }.runTask(MobMechanics.getInstance());
            }
            timeLeft = timeLeft > 0 ? timeLeft-1 : spawner.getSpawnTime()-1;
            spawner.setTimeLeft(timeLeft);
        }
    }
}
