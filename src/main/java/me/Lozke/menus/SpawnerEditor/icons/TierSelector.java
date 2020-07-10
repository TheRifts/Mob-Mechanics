package me.Lozke.menus.SpawnerEditor.icons;

import me.Lozke.data.MobSpawner;
import me.Lozke.data.Tier;
import me.Lozke.utils.ItemMenu.events.MenuClickEvent;
import me.Lozke.utils.ItemMenu.icons.MenuIcon;
import org.bukkit.inventory.ItemStack;

public class TierSelector extends MenuIcon {

    private MobSpawner spawner;
    private Tier tier;

    public TierSelector(ItemStack icon, MobSpawner spawner, Tier tier) {
        super(icon);
        this.spawner = spawner;
        this.tier = tier;
        setDisplayName(tier.getColorCode() + tier.name());
    }

    @Override
    public void onItemClick(MenuClickEvent event) {
        spawner.setTier(tier);
        event.getMenu().getParent().updateMenu();
        event.getMenu().openParent(event.getPlayer());
    }
}
