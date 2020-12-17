package me.Lozke.tasks;

import me.Lozke.managers.AbilityManager;
import org.bukkit.scheduler.BukkitRunnable;

public class TickAbilitiesTask extends BukkitRunnable {

    private AbilityManager abilityManager;

    public TickAbilitiesTask(AbilityManager manager) {
        this.abilityManager = manager;
    }

    @Override
    public void run() {
        abilityManager.tickCooldowns();
    }
}
