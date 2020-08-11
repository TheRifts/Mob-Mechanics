package me.Lozke.commands;

import me.Lozke.data.ARNamespacedKey;
import me.Lozke.managers.ItemWrapper;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnerWand extends Command {

    public SpawnerWand() {
        super("gibwand");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        ItemWrapper wrapper = new ItemWrapper()
                .setMaterial(Material.SHEARS)
                .setName("&eSpawner Wand")
                .setLore(
                        Text.colorize("&8Press swap key to switch between placement and edit modes"),
                        Text.colorize("&8Placement mode: Right click to place, Left click to destroy"),
                        Text.colorize("&8Edit mode: Left click to access spawner GUI, Right click to place fixed spawn locations"))
                .addKey(ARNamespacedKey.SPAWNER_WAND_TOGGLE);
        ((Player) sender).getInventory().addItem(wrapper.getItem());
        return true;
    }
}
