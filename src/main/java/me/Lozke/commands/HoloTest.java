package me.Lozke.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.Lozke.utils.Text;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class HoloTest extends Command {

    public HoloTest() {
        super("holo");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Player player = (Player) commandSender;

        Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.CHICKEN);
        entity.setCustomName("Normal ol Chicken");
        entity.setCustomNameVisible(true);

        int line = addLine(entity.getLocation(), entity.getEntityId(), "pants");
        int line2 = addLine(entity.getLocation(), line, "poopy pants");
        int line3 = addLine(entity.getLocation(), line2, "shitty pants");
        int line4 = addLine(entity.getLocation(), line3, "crappy pants");

        return true;
    }

    private void broadcastPacket(PacketContainer packet) {
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }

    private int addLine(Location location, int parentID, String... display) {
        int newEntityID = (int) (Math.random() * Integer.MAX_VALUE);

        PacketContainer spawnEntityLiving = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        spawnEntityLiving.getIntegers().write(0, newEntityID);
        spawnEntityLiving.getUUIDs().write(0, UUID.randomUUID());
        spawnEntityLiving.getEntityTypeModifier().write(0, EntityType.SHULKER_BULLET);
        spawnEntityLiving.getDoubles().write(0, location.getX());
        spawnEntityLiving.getDoubles().write(1, location.getY());
        spawnEntityLiving.getDoubles().write(2, location.getZ());
        broadcastPacket(spawnEntityLiving);

        createMeta(newEntityID, display);
        mountPacket(parentID, newEntityID);
        return newEntityID;
    }

    private void createMeta(int entityID, String... displayName) {
        PacketContainer metaContainer = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        metaContainer.getModifier().writeDefaults();
        metaContainer.getModifier().write(0, entityID);
        WrappedDataWatcher wrapper = new WrappedDataWatcher();
        wrapper.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x30);
        if (displayName.length > 0) {
            Optional<?> opt = Optional.of(WrappedChatComponent.fromText(Text.colorize(displayName[0])).getHandle());
            wrapper.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);
            wrapper.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        }
        metaContainer.getWatchableCollectionModifier().write(0, wrapper.getWatchableObjects());
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(metaContainer);
    }

    private void mountPacket(int vehicleID, int passengerID) {
        PacketContainer mountPacket = new PacketContainer(PacketType.Play.Server.MOUNT);
        mountPacket.getIntegers().write(0, vehicleID);
        mountPacket.getIntegerArrays().write(0, new int[]{passengerID});
        broadcastPacket(mountPacket);
    }
}
