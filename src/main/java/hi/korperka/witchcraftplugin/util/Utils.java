package hi.korperka.witchcraftplugin.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Utils {
    public static void spawnParticleBeam(Particle particle, Location start, Location end){
        //УРААААААААААА
        double distance = start.distance(end);
        Vector direction = end.toVector().subtract(start.toVector()).normalize();

        for(double i = 0; i < distance; i += 0.3){
            Location currentParticleLocation = start.clone().add(direction.clone().multiply(i));
            start.getWorld().spawnParticle(particle, currentParticleLocation, 2);
        }
    }

    public static void teleportEffect(Player player){
        Location playerLocation = player.getLocation();
        World world = player.getWorld();

        final int PARTICLES_COUNT = 150;

        for(int i = 0; i < PARTICLES_COUNT; i++)
        {
            double offsetX = Math.random() * 2 - 1;
            double offsetY = Math.random() * 2 - 1;
            double offsetZ = Math.random() * 2 - 1;

            world.spawnParticle(Particle.PORTAL,
                    playerLocation.add(offsetX, offsetY, offsetZ), 1, 0, 0, 0, 0);
            playerLocation.subtract(offsetX, offsetY, offsetZ);
        }
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> argClass){
        int rand = (int) (Math.random() * argClass.getEnumConstants().length);
        return argClass.getEnumConstants()[rand];
    }

    //я задолбался от этих векторов
    public static void pushEntityToPlayer(Entity entity, Player player) {
        Vector direction = player.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
        Vector velocity = direction.multiply(1.5);
        entity.setVelocity(velocity);
    }

    public static void pushPlayerUp(Player player, int distance) {
        Vector velocity = new Vector(0, distance, 0).normalize().multiply(1.5);
        player.setVelocity(velocity);
    }

    public static void breakBlockIfAbsent(Block block) {
        if(block != null)
            block.breakNaturally();
    }

    public static void sendChatMessageFromPlayer(Player player, Component message) {
        player.getServer().sendMessage(Component.text(String.format("<%s> ", player.getName()))
                .append(message));
    }
}
