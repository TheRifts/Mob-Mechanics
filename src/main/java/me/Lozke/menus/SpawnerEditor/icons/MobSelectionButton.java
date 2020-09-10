package me.Lozke.menus.SpawnerEditor.icons;

import me.Lozke.data.MobSpawner;
import me.Lozke.data.BaseEntity;
import me.Lozke.utils.ItemMenu.events.MenuClickEvent;
import me.Lozke.utils.ItemMenu.icons.MenuIcon;
import me.Lozke.utils.Items;
import me.Lozke.utils.Text;
import org.bukkit.Material;


public class MobSelectionButton extends MenuIcon {

    private MobSpawner spawner;
    private BaseEntity baseEntity;

    public MobSelectionButton(MobSpawner spawner, BaseEntity baseEntity) {
        super(Items.formatItem(Material.ZOMBIE_VILLAGER_SPAWN_EGG, baseEntity.getName()));
        Items.setLore(getIcon(), Text.colorize("&fEntity Type: " + baseEntity.getType()), "TODO: Display other stats");
        this.spawner = spawner;
        this.baseEntity = baseEntity;
    }

    @Override
    public void onItemClick(MenuClickEvent event) {
        spawner.setEntityID(baseEntity.getId());
        event.getMenu().getParent().updateMenu();
        event.getMenu().openParent(event.getPlayer());
    }
}