package me.Lozke.tasks;

import me.Lozke.MobMechanics;
import me.Lozke.data.*;
import me.Lozke.managers.ItemFactory;
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
                for (int spawnedMobAmount = 0; spawnedMobAmount < spawner.getAmount() && spawner.getSpawnedMobsAmount() < spawner.getMaxMobAmount(); spawnedMobAmount++) {
                    double a = NumGenerator.fraction() * 2 * Math.PI;
                    double dist = NumGenerator.fraction() * spawner.getRadius();
                    Location loc = spawner.getLocation().clone().add(dist * Math.sin(a), 0, dist * Math.cos(a)).add(0.5, 0, 0.5);
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

    private void syncSpawnMob(MobSpawner spawner, Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                RiftsMob mob = MobMechanics.getInstance().getBaseEntityManager().spawnBaseEntity(spawner, location);
                MobMechanics.getInstance().getMobManager().trackEntity(mob);

                Tier tier = mob.getTier();
                Rarity rarity = mob.getRarity();

                int minHp = ItemFactory.getArmourHP(tier, rarity, ItemFactory.RangeType.LOW);
                int maxHP = ItemFactory.getArmourHP(tier, rarity, ItemFactory.RangeType.HIGH);
                mob.setBaseStat(RiftsStat.HP, NumGenerator.rollInclusive(minHp, maxHP));

                mob.setBaseStat(RiftsStat.DMG_LO, ItemFactory.getDamage(tier, rarity, ItemFactory.RangeType.LOW));
                mob.setBaseStat(RiftsStat.DMG_HI, ItemFactory.getDamage(tier, rarity, ItemFactory.RangeType.HIGH));

                mob.updateStats();
            }
        }.runTask(MobMechanics.getInstance());
    }
}
