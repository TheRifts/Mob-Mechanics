package me.Lozke.events;

import me.Lozke.data.ActionBarMessage;
import me.Lozke.data.items.NamespacedKeys;
import me.Lozke.tasks.actionbar.ActionBarMessageTickTask;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnerWandToggle implements Listener {

    private static final int weight = 100;
    private static final int time = 3;
    private static final boolean showTime = false;

    private Map<UUID, ActionBarMessageTickTask> messages;

    public SpawnerWandToggle() {
        messages = new HashMap<>();
    }

    @EventHandler
    public void onHandSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (handItem.getType() != Material.SHEARS || !handItem.getItemMeta().getDisplayName().equals(Text.colorize("&eSpawner Wand"))) {
            return;
        }
        ItemMeta itemMeta = handItem.getItemMeta();
        if (itemMeta != null) {
            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            NamespacedKey key = NamespacedKeys.spawnerWandToggle;
            if (dataContainer.has(key, PersistentDataType.INTEGER)) {
                int value = dataContainer.get(key, PersistentDataType.INTEGER);
                UUID uuid = player.getUniqueId();
                if (value == 0) {
                    dataContainer.set(key, PersistentDataType.INTEGER, 1);
                    handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&eEdit Mode Activated", weight, time, showTime), uuid));
                }
                if (value == 1) {
                    dataContainer.set(key, PersistentDataType.INTEGER, 0);
                    handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&ePlacement Mode Activated", weight, time, showTime), uuid));
                }
            }
            handItem.setItemMeta(itemMeta);
            event.setCancelled(true);
        }
    }

    private void handleNewMessage(ActionBarMessageTickTask actionBarMessageTickTask) {
        UUID recipient = actionBarMessageTickTask.getRecipient();
        if (messages.containsKey(recipient)) {
            ActionBarMessageTickTask messageTickTask = messages.get(recipient);
            if(!messageTickTask.isCancelled()) {
                messages.get(recipient).cancel();
            }
        }
        messages.put(recipient, actionBarMessageTickTask);
    }
}