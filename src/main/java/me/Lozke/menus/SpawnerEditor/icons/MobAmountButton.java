package me.Lozke.menus.SpawnerEditor.icons;

import me.Lozke.data.MobSpawner;
import me.Lozke.utils.ItemMenu.events.MenuClickEvent;
import me.Lozke.utils.ItemMenu.icons.MenuIcon;
import me.Lozke.utils.Items;
import me.Lozke.utils.Text;
import org.bukkit.Material;

public class MobAmountButton extends MenuIcon {

    private MobSpawner spawner;

    public MobAmountButton(MobSpawner spawner) {
        this.spawner = spawner;
        setIcon(Items.formatItem(
                Material.BARRIER,
                Text.colorize("&fEdit Amount (" + spawner.getAmount() + ")"),
                /*
                 * &8Left click to increase by 1
                 * &8Right click to decrease by 1
                 * &8Shift click to change by 10
                 * &8Press drop key on this item to set to minimum value
                 */
                new String[]{Text.colorize("&8Left click to increase by 1"), Text.colorize("&8Right click to decrease by 1"), Text.colorize("&8Shift click to change by 10"), Text.colorize("&8Press drop key on this item to set to minimum value")}));
    }

    @Override
    public void updateIcon() {
        setDisplayName("&fEdit Amount (" + spawner.getAmount() + ")");
        getParent().updateIcon(this);
    }

    @Override
    public void onItemClick(MenuClickEvent event) {
        switch (event.getClick()) {
            case LEFT:
                add(1);
                break;
            case RIGHT:
                subtract(1);
                break;
            case SHIFT_LEFT:
                add(10);
                break;
            case SHIFT_RIGHT:
                subtract(10);
                break;
            case DROP:
                subtract(spawner.getAmount());
                break;
        }
    }

    private void add(int amount) {
        spawner.setAmount(spawner.getAmount() + amount);
        updateIcon();
    }

    private void subtract(int amount) {
        spawner.setAmount(Math.max(spawner.getAmount() - amount, 1));
        updateIcon();
    }
}
