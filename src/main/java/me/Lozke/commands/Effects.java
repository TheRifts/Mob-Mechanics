package me.Lozke.commands;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.Lozke.MobMechanics;
import me.Lozke.managers.EffectManager;

@CommandAlias("effects|effect")
public class Effects extends BaseCommand {

    private static EffectManager manager;

    public Effects() {
        manager = MobMechanics.getInstance().getEffectManager();
    }

    @Default
    @CommandAlias("reload")
    public static void onReload() {
        onSave();
        onLoad();
    }

    @CommandAlias("save")
    public static void onSave() {
        manager.saveEffects();
    }

    @CommandAlias("load")
    public static void onLoad() {
        manager.loadEffects();
    }

}
