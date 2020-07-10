package me.Lozke.menus.MobEditor.icons;

import me.Lozke.data.ModifiableEntity;
import me.Lozke.utils.ItemMenu.events.MenuClickEvent;
import me.Lozke.utils.ItemMenu.icons.MenuIcon;
import org.bukkit.inventory.ItemStack;

public class EliteToggleButton extends MenuIcon {

    private ModifiableEntity mob;

    public EliteToggleButton(ItemStack icon, ModifiableEntity mob) {
        super(icon);
        this.mob = mob;
    }

    @Override
    public void onItemClick(MenuClickEvent event) {

    }
}
