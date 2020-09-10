package me.Lozke.menus.MobEditor.icons;

import me.Lozke.data.BaseEntity;
import me.Lozke.utils.ItemMenu.events.MenuClickEvent;
import me.Lozke.utils.ItemMenu.icons.MenuIcon;
import org.bukkit.inventory.ItemStack;

public class EliteToggleButton extends MenuIcon {

    private BaseEntity mob;

    public EliteToggleButton(ItemStack icon, BaseEntity mob) {
        super(icon);
        this.mob = mob;
    }

    @Override
    public void onItemClick(MenuClickEvent event) {

    }
}
