package me.Lozke.menus.SpawnerEditor.icons;

import me.Lozke.data.MobSpawner;
import me.Lozke.data.Rarity;
import me.Lozke.utils.ItemMenu.events.MenuClickEvent;
import me.Lozke.utils.ItemMenu.icons.MenuIcon;
import org.bukkit.inventory.ItemStack;

public class RaritySelector extends MenuIcon {

    private MobSpawner spawner;
    private Rarity rarity;

    public RaritySelector(ItemStack icon, MobSpawner spawner, Rarity rarity) {
        super(icon);
        this.spawner = spawner;
        this.rarity = rarity;
        setDisplayName(rarity.getColorCode() + rarity.name());
    }

    @Override
    public void onItemClick(MenuClickEvent event) {
        spawner.setRarity(rarity);
        event.getMenu().getParent().updateMenu();
        event.getMenu().openParent(event.getPlayer());
    }
}
