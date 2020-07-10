package me.Lozke.menus.SpawnerEditor.icons;

import me.Lozke.data.MobSpawner;
import me.Lozke.utils.ItemMenu.events.MenuClickEvent;
import me.Lozke.utils.ItemMenu.icons.MenuIcon;
import me.Lozke.utils.Items;
import me.Lozke.utils.Text;
import org.bukkit.Material;

public class SpawnerTimerButton extends MenuIcon {

    private MobSpawner spawner;

    public SpawnerTimerButton(MobSpawner spawner) {
        this.spawner = spawner;
        String[] lore = new String[]{
                Text.colorize("&8Left click to increase spawn time by 10"),
                Text.colorize("&8Right click to decrease spawn time by 10"),
                Text.colorize("&8Shift click to change by 100"),
                Text.colorize("&8Press drop key to force spawn (spawner must be on)"),
                Text.colorize("&8Double click on this item to reset timer to default")};
        setIcon(Items.formatItem(Material.CLOCK, Text.colorize("&f" + spawner.getTimeLeft() + "/" + spawner.getSpawnTime()), lore));
        updateTimer();
    }

    @Override
    public void onItemClick(MenuClickEvent event) {
        switch (event.getClick()) {
            case LEFT:
                addTime(10);
                break;
            case RIGHT:
                subtractTime(10);
                break;
            case SHIFT_LEFT:
                addTime(100);
                break;
            case SHIFT_RIGHT:
                subtractTime(100);
                break;
            case DROP:
                spawner.setTimeLeft(0);
                break;
            case DOUBLE_CLICK:
                spawner.setSpawnTime(10);
                break;
        }
        updateTimer();
        updateIcon();
    }

    public void updateTimer() {
        setDisplayName(Text.colorize("&f" + spawner.getTimeLeft() + "/" + spawner.getSpawnTime()));
    }

    private void addTime(int time) {
        spawner.setSpawnTime(spawner.getSpawnTime() + time);
    }

    private void subtractTime(int time) {
        spawner.setSpawnTime(Math.max(spawner.getSpawnTime() - time, 10));
    }
}
