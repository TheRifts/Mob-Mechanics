package me.Lozke.commands;

import me.Lozke.MobMechanics;
import me.Lozke.managers.MobManager;
import me.Lozke.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Mobs extends Command {

    private MobManager mobManager;

    public Mobs() {
        super("mob");
        this.mobManager = MobMechanics.getInstance().getMobManager();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        switch (args[0]) {
            case "save":
                mobManager.saveMobs();
                break;
            case "load":
                mobManager.loadMobs();
                break;
            case "selector":
                mobManager.openEditor((Player) sender);
                break;
            default:
                sender.sendMessage(Text.colorize("&cInvalid Args"));
        }
        return true;
    }
}
