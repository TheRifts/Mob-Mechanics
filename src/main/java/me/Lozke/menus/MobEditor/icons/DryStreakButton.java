package me.Lozke.menus.MobEditor.icons;

import me.Lozke.data.ModifiableEntity;
import me.Lozke.utils.ItemMenu.events.MenuClickEvent;
import me.Lozke.utils.ItemMenu.icons.MenuIcon;
import me.Lozke.utils.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DryStreakButton extends MenuIcon {

    private ItemStack defaultIcon;
    private ModifiableEntity mob;

    public DryStreakButton(ModifiableEntity mob) {
        this.defaultIcon = new ItemStack(Material.TRIPWIRE_HOOK);
        this.mob = mob;

        if (mob.isDryStreak()) {
            setIcon(Items.makeGlow(defaultIcon));
            setDisplayName("hellll yeah");
        }
        else {
            setIcon(defaultIcon);
            setDisplayName("hellll noo");
        }
    }

    @Override
    public void onItemClick(MenuClickEvent event) {
        mob.setDryStreak(!mob.isDryStreak());

        if (mob.isDryStreak()) {
            setIcon(Items.makeGlow(defaultIcon));
            setDisplayName("hellll yeah");
        }
        else {
            setIcon(defaultIcon);
            setDisplayName("hellll noo");
        }

        updateIcon();
    }
}
