package me.Lozke.menus.SpawnerEditor.pages;

import me.Lozke.MobMechanics;
import me.Lozke.data.MobSpawner;
import me.Lozke.data.BaseEntity;
import me.Lozke.menus.SpawnerEditor.icons.MobSelectionButton;
import me.Lozke.utils.ItemMenu.menus.ItemMenu;

public class MobTypeSelector extends ItemMenu {

    public MobTypeSelector(MobSpawner spawner) {
        super(Math.max((int) Math.ceil(MobMechanics.getInstance().getMobManager().getLoadedMobs().size() / 9.0), 1), "Mob Type Selector");
        for (BaseEntity selectableMob : MobMechanics.getInstance().getMobManager().getLoadedMobs()) {
            addDisplayItem(new MobSelectionButton(spawner, selectableMob));
        }
    }
}
