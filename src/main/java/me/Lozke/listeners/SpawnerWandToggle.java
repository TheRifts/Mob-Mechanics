package me.Lozke.listeners;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.ActionBarMessage;
import me.Lozke.tasks.ActionBarMessageTickTask;
import me.Lozke.utils.NamespacedKeyWrapper;
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

    private Map<UUID, ActionBarMessageTickTask> messages = new HashMap<>();

    public SpawnerWandToggle() {
        //messages = new HashMap<>();
    }

    @EventHandler
    public void onHandSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (handItem.getType() != Material.SHEARS) {
            return;
        }

        NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(handItem);
        ARNamespacedKey toggle = ARNamespacedKey.SPAWNER_WAND_TOGGLE;
        if (wrapper.hasKey(toggle)) {
            boolean keyVal = wrapper.getBoolean(toggle);
            wrapper.addKey(toggle, !keyVal);
            UUID uuid = player.getUniqueId();
            if (keyVal) {
                handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&eEdit Mode Activated", weight, time, showTime), uuid));
            }
            else {
                handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&ePlacement Mode Activated", weight, time, showTime), uuid));
            }
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