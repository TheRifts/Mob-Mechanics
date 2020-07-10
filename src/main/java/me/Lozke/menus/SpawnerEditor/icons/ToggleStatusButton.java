package me.Lozke.menus.SpawnerEditor.icons;

import me.Lozke.data.MobSpawner;
import me.Lozke.utils.ItemMenu.events.MenuClickEvent;
import me.Lozke.utils.ItemMenu.icons.MenuIcon;
import me.Lozke.utils.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ToggleStatusButton extends MenuIcon {

    private MobSpawner spawner;
    private ItemStack on = Items.formatItem(Material.LIME_DYE, "&fSpawner Status: &a&lON");
    private ItemStack off = Items.formatItem(Material.GRAY_DYE, "&fSpawner Status: &c&lOFF");

    public ToggleStatusButton(MobSpawner spawner) {
        this.spawner = spawner;
        updateButton();
    }

    @Override
    public void onItemClick(MenuClickEvent event) {
        spawner.toggleSpawnerActive();
        updateButton();
        event.getMenu().updateIcon(this);
    }

    private void updateButton() {
        if (spawner.isSpawnerActive()) {
            setIcon(on);
        }
        else {
            setIcon(off);
        }
    }
}
