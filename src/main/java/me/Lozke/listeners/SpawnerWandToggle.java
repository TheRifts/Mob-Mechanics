package me.Lozke.listeners;

import me.Lozke.AgorianRifts;
import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.ActionBarType.ActionBarMessage;
import me.Lozke.tasks.ActionBarMessenger;
import me.Lozke.utils.NamespacedKeyWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class SpawnerWandToggle implements Listener {

    private static final int weight = 100;
    private static final int time = 3;

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
            event.setCancelled(true);

            boolean keyVal = wrapper.getBoolean(toggle);
            wrapper.addKey(toggle, !keyVal);

            ActionBarMessenger messenger = AgorianRifts.getPluginInstance().getActionBarMessenger();
            ActionBarMessage message = new ActionBarMessage("spawner-wand-toggle", "", time);
            if (keyVal) message.setMessage("&eEdit Mode Activated");
            else message.setMessage("&ePlacement Mode Activated");
            messenger.addMessage(player, message);
        }
    }
}