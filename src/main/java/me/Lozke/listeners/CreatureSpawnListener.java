package me.Lozke.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener implements Listener {

    @EventHandler
    public static void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.JOCKEY) {
            event.setCancelled(true);
        }
    }

}
