package me.Lozke.events;

import me.Lozke.FallingAutism;
import me.Lozke.MobMechanics;
import me.Lozke.data.ActionBarMessage;
import me.Lozke.data.items.NamespacedKeys;
import me.Lozke.handlers.ItemHandler;
import me.Lozke.tasks.actionbar.ActionBarMessageTickTask;
import me.Lozke.utils.Text;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ItemInteraction implements Listener {

    private static int weight = 10;

    private MobMechanics plugin;

    private List<UUID> ignoredPlayers;
    private Map<UUID, ActionBarMessageTickTask> messages;


    public ItemInteraction(MobMechanics plugin) {
        this.plugin = plugin;
        ignoredPlayers = new ArrayList<>();
        messages = new HashMap<>();
    }


    @EventHandler
    public void onInteraction(PlayerInteractEvent event) {
        //Prevents event from firing twice, we only care if player is using main hand!
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        //Prevent event from firing twice if spawner is at edge of melee range
        if(ignoredPlayers.contains(uuid)) {
            return;
        }
        ignoredPlayers.add(uuid);
        new BukkitRunnable() {
            @Override
            public void run() {
                ignoredPlayers.remove(uuid);
            }
        }.runTaskLaterAsynchronously(FallingAutism.getPluginInstance(), 1);


        //TODO: Check player's Rank and if too low return here.

        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = handItem.getItemMeta();
        if (handItem.getType() != Material.SHEARS || itemMeta == null || !itemMeta.getDisplayName().equals(Text.colorize("&eSpawner Wand"))) {
            return;
        }

        Action action = event.getAction();
        if(action == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true); //Prevent destroying blocks with spawner wand
        }

        Location location;
        try {
            if (action == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                location = event.getClickedBlock().getLocation();
            } else {
                location = player.getTargetBlockExact(50).getLocation(); //We should consider calculating the actual maximum range instead of capping it at 50
            }
        } catch (NullPointerException ignore) {
            handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&cOut Of Range", weight), uuid));
            return;
        }

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        NamespacedKey key = NamespacedKeys.spawnerWandToggle;
        if (dataContainer.has(key, PersistentDataType.INTEGER)) {
            int value = dataContainer.get(key, PersistentDataType.INTEGER); //Let's convert this to a boolean DataType!
            if (value == 0) { //Placement Mode
                switch(event.getAction()) {
                    case LEFT_CLICK_BLOCK:
                    case LEFT_CLICK_AIR:
                        if(plugin.getSpawnerManager().isSpawner(location)) {
                            plugin.getSpawnerManager().removeSpawner(location);
                            handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&aSpawner Removed", weight), uuid));
                        }
                        else {
                            handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&cNo Spawner Found", weight), uuid));
                        }
                        break;
                    case RIGHT_CLICK_BLOCK:
                        placeSpawner(location, event.getBlockFace());
                        handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&aSpawner Placed", weight), uuid));
                        break;
                    case RIGHT_CLICK_AIR:
                        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 50);
                        if(lastTwoTargetBlocks.size()!=2) {
                            handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&cPlacement Failure", weight), uuid));
                            return;
                        }
                        BlockFace blockFace = lastTwoTargetBlocks.get(1).getFace(lastTwoTargetBlocks.get(0));
                        if(blockFace==null) {
                            handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&cPlacement Failure", weight), uuid));
                            return;
                        }
                        placeSpawner(location, blockFace);
                        handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&aSpawner Placed", weight), uuid));
                        break;
                }
            }
            if (value == 1) { //Edit Mode
                switch(event.getAction()) {
                    case LEFT_CLICK_BLOCK:
                    case LEFT_CLICK_AIR:
                        // Temporarily added right click cases
                    case RIGHT_CLICK_BLOCK:
                    case RIGHT_CLICK_AIR:
                        if (plugin.getSpawnerManager().isSpawner(location)) {
                            player.openInventory(plugin.getSpawnerManager().openGUI(location));
                        }
                        else {
                            handleNewMessage(new ActionBarMessageTickTask(new ActionBarMessage("&cNo Spawner Found", weight), uuid));
                        }
                        break;
                        /*
                    case RIGHT_CLICK_BLOCK:
                    case RIGHT_CLICK_AIR:
                        handleNewMessage(uuid, new ActionBarMessageTickTask(new ActionBarMessage("&cFixed Spawns Not Implemented", weight), uuid));
                        //TODO placing fixed spawn locations
                        break;
                         */
                }
            }
        }
    }

    private void placeSpawner(Location location, BlockFace blockFace) {
        //TODO: Change these to only add to the affected coordinate. Less maths!
        switch (blockFace) {
            case NORTH:
                location.add(0, 0, -1);
                break;
            case EAST:
                location.add(1, 0, 0);
                break;
            case SOUTH:
                location.add(0, 0, 1);
                break;
            case WEST:
                location.add(-1, 0, 0);
                break;
            case UP:
                location.add(0, 1, 0);
                break;
            case DOWN:
                location.add(0, -1, 0);
                break;
        }
        plugin.getSpawnerManager().createSpawner(location);
    }

    private void handleNewMessage(ActionBarMessageTickTask newMessageTickTask) {
        UUID recipient = newMessageTickTask.getRecipient();
        if (messages.containsKey(recipient)) {
            ActionBarMessageTickTask existingMessageTickTask = messages.get(recipient);
            if(!existingMessageTickTask.isCancelled()) {
                messages.get(recipient).cancel();
            }
        }
        messages.put(recipient, newMessageTickTask);
    }
}