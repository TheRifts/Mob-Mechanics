package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import me.Lozke.managers.MobManager;

@CommandAlias("slaughter")
public class Slaughter extends BaseCommand {

    private MobManager mobManager;

    public Slaughter(MobManager manager) {
        this.mobManager = manager;
    }

    @Default
    public void onSlaughter() {
        mobManager.stopTrackingAllMobs();
    }
}
