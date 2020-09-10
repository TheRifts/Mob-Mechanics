package me.Lozke.menus.MobSelector;

import me.Lozke.data.BaseEntity;
import me.Lozke.menus.MobEditor.MobEditorMenu;
import me.Lozke.utils.ItemMenu.icons.OpenMenuIcon;
import me.Lozke.utils.ItemMenu.menus.ItemMenu;
import me.Lozke.utils.Items;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class MobSelectorMenu extends ItemMenu {

    public MobSelectorMenu() {
        super(InventoryType.CHEST, 6, "Mob Selector (pg. 1)");
        applyOutline();
        populateGUI();
    }

    private void populateGUI() {

    }

    private void applyOutline() {
        ItemStack item = Items.formatItem(Material.BLACK_STAINED_GLASS_PANE, " ");
        for (int i = 45; i < 54; i++) {
            if (i == 49) {
                ItemStack buttonIcon = Items.formatItem(Material.YELLOW_STAINED_GLASS_PANE, "&e&lAdd New Mob");
                setDisplayItem(i, new OpenMenuIcon(buttonIcon, new MobEditorMenu(new BaseEntity()).setParent(this)));
            }
            else {
                setDisplayItem(i, item);
            }
        }
    }
}
