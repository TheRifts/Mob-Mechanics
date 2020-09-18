package me.Lozke.menus.SpawnerEditor.pages;

import me.Lozke.data.MobSpawner;
import me.Lozke.menus.SpawnerEditor.icons.ActiveRangeButton;
import me.Lozke.menus.SpawnerEditor.icons.MobAmountButton;
import me.Lozke.menus.SpawnerEditor.icons.MobMaxAmountButton;
import me.Lozke.menus.SpawnerEditor.icons.RadiusButton;
import me.Lozke.utils.ItemMenu.icons.ReturnIcon;
import me.Lozke.utils.ItemMenu.menus.ItemMenu;
import me.Lozke.utils.Items;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class MechanicEditor extends ItemMenu {

    public MechanicEditor(MobSpawner spawner) {
        super(InventoryType.HOPPER, "Spawner Mechanics");
        addDisplayItem(new ActiveRangeButton(spawner));
        addDisplayItem(new RadiusButton(spawner));
        addDisplayItem(new MobAmountButton(spawner));
        addDisplayItem(new MobMaxAmountButton(spawner));
        setDisplayItem(getInventory().getSize()-1, new ReturnIcon(Items.formatItem(Material.RED_CONCRETE, "&cReturn")));
    }

}
