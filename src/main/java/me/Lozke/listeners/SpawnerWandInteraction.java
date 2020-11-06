package me.Lozke.listeners;

import me.Lozke.AgorianRifts;
import me.Lozke.MobMechanics;
import me.Lozke.data.ARNamespacedKey;
import me.Lozke.data.ActionBarType.ActionBarMessage;
import me.Lozke.data.MobSpawner;
import me.Lozke.managers.SpawnerManager;
import me.Lozke.tasks.ActionBarMessenger;
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
    private SpawnerManager spawnerMananger;
    private ActionBarMessenger messenger;
    private final String MESSAGE_ID = "spawner-wand";

    private List<UUID> ignoredPlayers;


    public SpawnerWandInteraction(MobMechanics plugin) {
        this.plugin = plugin;
        this.spawnerMananger = plugin.getSpawnerManager();
        this.messenger = AgorianRifts.getPluginInstance().getActionBarMessenger();
        ignoredPlayers = new ArrayList<>();
        //messages = new HashMap<>();
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent event) {
        //TODO: Check player's Rank and if too low return here.
        //Let's not run ANY code if the player is too low of a rank.

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
            ActionBarMessage message = new ActionBarMessage(MESSAGE_ID,"&cOut Of Range");
            messenger.addMessage(player, message);
            return;
        }

        ActionBarMessage message;
        NamespacedKeyWrapper wrapper = new NamespacedKeyWrapper(handItem);
        if (wrapper.hasKey(ARNamespacedKey.SPAWNER_WAND_TOGGLE)) {
            boolean val = wrapper.getBoolean(ARNamespacedKey.SPAWNER_WAND_TOGGLE);
            if (val) { //Placement Mode
                switch(event.getAction()) {
                    case LEFT_CLICK_BLOCK:
                    case LEFT_CLICK_AIR:
                        if(plugin.getSpawnerManager().isSpawner(location)) {
                            plugin.getSpawnerManager().removeSpawner(location);
                            message = new ActionBarMessage(MESSAGE_ID,"&aSpawner Removed");
                            messenger.addMessage(player, message);
                        }
                        else {
                            message = new ActionBarMessage(MESSAGE_ID,"&cNo Spawner Found");
                            messenger.addMessage(player, message);
                        }
                        break;
                    case RIGHT_CLICK_BLOCK:
                        placeSpawner(location, event.getBlockFace());
                        message = new ActionBarMessage(MESSAGE_ID,"&aSpawner Placed");
                        messenger.addMessage(player, message);
                        break;
                    case RIGHT_CLICK_AIR:
                        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 50);
                        if(lastTwoTargetBlocks.size()!=2) {
                            message = new ActionBarMessage(MESSAGE_ID,"&cPlacement Failure");
                            messenger.addMessage(player, message);
                            return;
                        }
                        BlockFace blockFace = lastTwoTargetBlocks.get(1).getFace(lastTwoTargetBlocks.get(0));
                        if(blockFace==null) {
                            message = new ActionBarMessage(MESSAGE_ID,"&cPlacement Failure");
                            messenger.addMessage(player, message);
                            return;
                        }
                        placeSpawner(location, blockFace);
                        message = new ActionBarMessage(MESSAGE_ID,"&aSpawner Placed");
                        messenger.addMessage(player, message);
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
                            message = new ActionBarMessage(MESSAGE_ID,"&cNo Spawner Found");
                            messenger.addMessage(player, message);
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
}