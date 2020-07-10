package me.Lozke.menus.SpawnerEditor.pages;

import me.Lozke.data.MobSpawner;
import me.Lozke.data.Rarity;
import me.Lozke.utils.ItemMenu.menus.ItemMenu;
import me.Lozke.utils.Items;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class RaritySelector extends ItemMenu {

    public RaritySelector(MobSpawner spawner) {
        super(InventoryType.CHEST, 1, "Rarity Editor");
        for (Rarity rarity : Rarity.types) {
            if (spawner.getRarity() == rarity) {
                addDisplayItem(new me.Lozke.menus.SpawnerEditor.icons.RaritySelector(Items.makeGlow(new ItemStack(rarity.getIcon())), spawner, rarity));
            }
            else {
                addDisplayItem(new me.Lozke.menus.SpawnerEditor.icons.RaritySelector(rarity.getIcon(), spawner, rarity));
            }
        }
    }
}
