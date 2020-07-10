package me.Lozke.menus.MobEditor;

import me.Lozke.data.ModifiableEntity;
import me.Lozke.data.MobSpawner;
import me.Lozke.menus.MobEditor.icons.DryStreakButton;
import me.Lozke.menus.MobEditor.icons.EliteToggleButton;
import me.Lozke.utils.ItemMenu.icons.ReturnIcon;
import me.Lozke.utils.ItemMenu.menus.ItemMenu;
import me.Lozke.utils.Items;
import me.Lozke.utils.Text;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class MobEditorMenu extends ItemMenu {
    public MobEditorMenu(ModifiableEntity mob, MobSpawner spawner) {
        super(InventoryType.CHEST, 1, "Mob Editor");
        addDisplayItem(
                Items.formatItem(
                        Material.ZOMBIE_HEAD, //Change this to display mobType
                        Text.colorize("&fType: " + mob.getName()),
                        new String[]{Text.colorize("&8Click to change type")}));
        addDisplayItem(
                Items.formatItem(
                        Material.NAME_TAG,
                        Text.colorize("&fRename Mob"),
                        new String[]{Text.colorize("&fCurrent Name: " + mob.getName()), Text.colorize("&7Click to change name")}));
        addDisplayItem(Items.formatItem(Material.CHEST, Text.colorize("&fEdit/Add Drops")));
        addDisplayItem(Items.formatItem(Material.ITEM_FRAME, Text.colorize("&fEdit Stats")));
        addDisplayItem(Items.formatItem(Material.WRITABLE_BOOK, Text.colorize("&fEdit/Add Dialogue")));
        addDisplayItem(new EliteToggleButton(Items.formatItem(Material.ENDER_PEARL, Text.colorize("&fToggle Elite Status")), mob));
        addDisplayItem(Items.formatItem(Material.GOLDEN_SWORD, Text.colorize("&fWeapon Type")));
        addDisplayItem(new DryStreakButton(mob).setParent(this));
        addDisplayItem(new ReturnIcon(Items.formatItem(Material.RED_CONCRETE, Text.colorize("&cReturn"))));
    }

    public MobEditorMenu(ModifiableEntity mob) {
        this(mob, null);
    }
}
