package me.Lozke.listeners;

import me.Lozke.MobMechanics;
import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.ActionBarMessage;
import me.Lozke.tasks.ActionBarMessageTickTask;
import me.Lozke.utils.NamespacedKeyWrapper;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SpawnerWandInteraction implements Listener {

    private static int weight = 10;

    private MobMechanics plugin;

    private List<UUID> ignoredPlayers;
    private Map<UUID, ActionBarMessageTickTask> messages = new HashMap<>();


    public SpawnerWandInteraction(MobMechanics plugin) {
        this.plugin = plugin;
        ignoredPlayers = new ArrayList<>();
        //messages = new HashMap<>();
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
        }.runTaskLaterAsynchronously(MobMechanics.getInstance(), 1);


        //TODO: Check player's Rank and if too low return here.

        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = handItem.getItemMeta();
        if (handItem.getType() != Material.SHEARS) {
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

        NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(handItem);
        if (wrapper.hasKey(ARNamespacedKey.SPAWNER_WAND_TOGGLE)) {
            boolean val = wrapper.getBoolean(ARNamespacedKey.SPAWNER_WAND_TOGGLE);
            if (val) { //Placement Mode
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
            else { //Edit Mode
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