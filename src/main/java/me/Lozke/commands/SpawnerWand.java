package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import me.Lozke.data.*;
import me.Lozke.utils.ItemWrapper;
import me.Lozke.utils.Logger;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@CommandAlias("gibwand")
public class SpawnerWand extends BaseCommand {

    @Default
    @CommandCompletion("@tier @rarity @mob-ids")
    @Syntax("<tier> <rarity> <mob id> <timer> <radius> <range> <leash distance> <spawn amount> <max amount>")
    public boolean onGibWand(Player player, Tier tier, Rarity rarity, String mobID, int timer, int radius, int activeRange, int leashRange, int spawnAmount, int maxAmount) {
        MobSpawner spawner = new MobSpawner(tier, rarity, mobID, false, true, timer, radius, activeRange, spawnAmount, maxAmount, leashRange);
        ItemWrapper wrapper = new ItemWrapper(Material.SHEARS)
                .setName("&eSpawner Wand")
                .setLore(
                        Text.colorize("&8Press swap key to switch between placement and edit modes"),
                        Text.colorize("&8Placement mode: Right click to place, Left click to destroy"),
                        Text.colorize("&8Edit mode: Left click to access spawner GUI, Right click to place fixed spawn locations"))
                .addKey(ARNamespacedKey.SPAWNER_WAND_TOGGLE)
                .addKey(SpawnerWandPersistentDataType.DATA_TAG, new SpawnerWandPersistentDataType(), spawner);
        player.getInventory().addItem(wrapper.getItem());
        return true;
    }

    @CommandAlias("review")
    public void onReview(Player player) {
        ItemWrapper wrapper = new ItemWrapper(player.getInventory().getItemInMainHand());
        MobSpawner spawner = (MobSpawner) wrapper.get(SpawnerWandPersistentDataType.DATA_TAG, new SpawnerWandPersistentDataType());
        Logger.broadcast("Parsing spawner wand data: ");
        Logger.broadcast(spawner.getEntityID());
    }
}
