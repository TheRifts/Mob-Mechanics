package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.Lozke.data.ARNamespacedKey;
import me.Lozke.utils.ItemWrapper;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@CommandAlias("gibwand")
public class SpawnerWand extends BaseCommand {

    @Default
    public boolean onGibWand(Player player) {
        ItemWrapper wrapper = new ItemWrapper(Material.SHEARS)
                .setName("&eSpawner Wand")
                .setLore(
                        Text.colorize("&8Press swap key to switch between placement and edit modes"),
                        Text.colorize("&8Placement mode: Right click to place, Left click to destroy"),
                        Text.colorize("&8Edit mode: Left click to access spawner GUI, Right click to place fixed spawn locations"))
                .addKey(ARNamespacedKey.SPAWNER_WAND_TOGGLE);
        player.getInventory().addItem(wrapper.getItem());
        return true;
    }
}
