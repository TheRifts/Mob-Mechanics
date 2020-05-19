/*
 * Created by Noah Pritchard on 5/9/2020
 */
package me.Lozke.tasks;

import me.Lozke.MobMechanics;
import me.Lozke.data.CustomMob;
import me.Lozke.managers.MobManager;
import me.Lozke.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.scheduler.BukkitRunnable;

public class HealthNameFlashTask extends BukkitRunnable {

    private MobManager mobManager;

    private int counter;

    public HealthNameFlashTask(MobManager mobManager) {
        this.mobManager = mobManager;

        this.counter = 0;

        runTaskTimerAsynchronously(MobMechanics.getInstance(), 0L, 40L);
    }

    @Override
    public void run() {
        final int alternationOffset = 4;
        for (CustomMob customMob : mobManager.getAllCustomMobs()) {
            if (Bukkit.getEntity(customMob.getUniqueId()) != null) {
                if (customMob.getLivingEntity().getHealth() != customMob.getAttributable().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() &&
                        counter%alternationOffset !=0) {
                    customMob.updateHealthDisplay();
                }
                else {
                    Bukkit.getEntity(customMob.getUniqueId()).setCustomName(customMob.getName());
                }
            }
        }

        if (counter%alternationOffset ==0) {
            counter = 0;
        }
        counter++;
    }
}
