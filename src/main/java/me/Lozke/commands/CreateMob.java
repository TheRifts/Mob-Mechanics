package me.Lozke.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Lozke.data.BaseEntity;
import me.Lozke.managers.BaseEntityManager;
import org.bukkit.entity.EntityType;

@CommandAlias("addmob")
public class CreateMob extends BaseCommand {

    private BaseEntityManager baseEntityManager;

    public CreateMob(BaseEntityManager baseEntityManager) {
        this.baseEntityManager = baseEntityManager;
    }

    @Default
    @CommandCompletion("@entity")
    @Syntax("<entity> <mob-ID>")
    public void onCreateMob(EntityType type, @Single String mobID) {
        BaseEntity baseEntity = new BaseEntity();
        baseEntity.setId(mobID);
        baseEntity.setName(mobID);
        baseEntity.setType(type);

        baseEntityManager.addBaseEntity(baseEntity);
    }

}
