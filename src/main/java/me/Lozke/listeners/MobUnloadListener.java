package me.Lozke.listeners;

import me.Lozke.MobMechanics;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class MobUnloadListener implements Listener {

    private final MobMechanics plugin;

    public MobUnloadListener(MobMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChunkUnload(ChunkUnloadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (entity instanceof LivingEntity) {
                plugin.getMobManager().stopTracking((LivingEntity) entity);
            }
        }
    }
}
