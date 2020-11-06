package me.Lozke.listeners;

import me.Lozke.data.MobSpawner;
import me.Lozke.data.SpawnerWandPersistentDataType;
import me.Lozke.managers.SpawnerManager;
import me.Lozke.utils.NamespacedKeyWrapper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class SpawnerSneakListener implements Listener {

    private SpawnerManager manager;

    public SpawnerSneakListener(SpawnerManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) {
            return;
        }
        Player player = event.getPlayer();

        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (handItem.getType() != Material.SHEARS) {
            return;
        }

        Block locBlock = player.getLocation().add(0, -1, 0).getBlock();
        if (!locBlock.getType().name().contains("CONCRETE")) {
            return;
        }

        MobSpawner spawner = manager.getSpawner(locBlock.getLocation());
        if (spawner == null) {
            return;
        }

        NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(handItem);
        wrapper.addKey(SpawnerWandPersistentDataType.DATA_TAG, new SpawnerWandPersistentDataType(), spawner);
    }

}
