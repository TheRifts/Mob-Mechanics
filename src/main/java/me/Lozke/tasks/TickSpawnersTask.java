package me.Lozke.tasks;

import me.Lozke.FallingAutism;
import me.Lozke.data.MobSpawner;
import me.Lozke.managers.SpawnerManager;
import me.Lozke.utils.NumGenerator;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class TickSpawnersTask extends BukkitRunnable {

    private SpawnerManager spawnerManager;

    public TickSpawnersTask(SpawnerManager spawnerManager) {
        this.spawnerManager = spawnerManager;
        runTaskTimerAsynchronously(FallingAutism.getPluginInstance(), 0L, 20L);
    }

    @Override
    public void run() {
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
                        for (Location loc : locations) {
                            LivingEntity entity = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
                            entity.setHealth(1);
                        }
                    }
                }.runTask(FallingAutism.getPluginInstance());
            }
            timeLeft = timeLeft > 0 ? timeLeft-1 : spawner.getSpawnTime()-1;
            spawner.setTimeLeft(timeLeft);
        }
    }
}
