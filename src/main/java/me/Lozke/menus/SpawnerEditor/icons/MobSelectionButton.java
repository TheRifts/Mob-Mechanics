package me.Lozke.menus.SpawnerEditor.icons;

import me.Lozke.data.MobSpawner;
import me.Lozke.data.ModifiableEntity;
import me.Lozke.utils.ItemMenu.events.MenuClickEvent;
import me.Lozke.utils.ItemMenu.icons.MenuIcon;
import me.Lozke.utils.Items;
import me.Lozke.utils.Text;
import org.bukkit.Material;


public class MobSelectionButton extends MenuIcon {

    private MobSpawner spawner;
    private ModifiableEntity modifiableEntity;

    public MobSelectionButton(MobSpawner spawner, ModifiableEntity modifiableEntity) {
        super(Items.formatItem(Material.ZOMBIE_VILLAGER_SPAWN_EGG, modifiableEntity.getName()));
        Items.setLore(getIcon(), Text.colorize("&fEntity Type: " + modifiableEntity.getType()), "TODO: Display other stats");
        this.spawner = spawner;
        this.modifiableEntity = modifiableEntity;
    }

    @Override
    public void onItemClick(MenuClickEvent event) {
        spawner.setEntityID(modifiableEntity.getId());
        event.getMenu().getParent().updateMenu();
        event.getMenu().openParent(event.getPlayer());
    }
}