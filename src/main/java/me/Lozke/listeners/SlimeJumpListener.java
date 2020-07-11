package me.Lozke.listeners;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import me.Lozke.tasks.SlimeZaWarudoTask;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SlimeJumpListener implements Listener {

    @EventHandler
    public void onJump(EntityJumpEvent event) {
        if (!(event.getEntity() instanceof Slime)) {
            return;
        }
        //new SlimeZaWarudoTask(event.getEntity());
    }

}
