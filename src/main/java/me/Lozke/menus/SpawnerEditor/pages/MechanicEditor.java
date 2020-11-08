package me.Lozke.menus.SpawnerEditor.pages;

import me.Lozke.data.MobSpawner;
import me.Lozke.menus.SpawnerEditor.icons.*;
import me.Lozke.utils.ItemMenu.icons.ReturnIcon;
import me.Lozke.utils.ItemMenu.menus.ItemMenu;
import me.Lozke.utils.Items;
import org.bukkit.Material;

public class MechanicEditor extends ItemMenu {

    public MechanicEditor(MobSpawner spawner) {
        super(1, "Spawner Mechanics");
        addDisplayItem(new ActiveRangeButton(spawner));
        addDisplayItem(new LeashButton(spawner));
        addDisplayItem(new RadiusButton(spawner));
        addDisplayItem(new MobAmountButton(spawner));
        addDisplayItem(new MobMaxAmountButton(spawner));
        setDisplayItem(getInventory().getSize()-1, new ReturnIcon(Items.formatItem(Material.RED_CONCRETE, "&cReturn")));
    }

}
