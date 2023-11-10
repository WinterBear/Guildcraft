package dev.snowcave.guilds.interaction;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.guildhalls.GuildHall;
import dev.snowcave.guilds.utils.RepeatingTask;
import dev.snowcave.guilds.utils.RepeatingTaskUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Created by WinterBear on 29/12/2020.
 */
public class GuildHallInteractionListener implements Listener {

    public static void start(JavaPlugin plugin) {
        RepeatingTaskUtils.everyTick(10, new RepeatingTask("Guild Hall Area of Effect Task", GuildHallInteractionListener::areaOfEffect), plugin);
    }

    public static boolean areaOfEffect() {
        Map<GuildHall, Guild> halls = Guilds.GUILDS.stream()
                .filter(g -> g.listAllBonuses().contains(GuildBonus.GUILD_HALL))
                .collect(Collectors.toMap(Guild::getGuildHall, g -> g));
        if (!halls.isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Optional<GuildHall> hall = halls.keySet().stream().filter(h -> h.isInHall(player.getLocation())).findFirst();
                if (hall.isPresent() && Guilds.getGuild(player).equals(Optional.of(halls.get(hall.get())))) {
                    heal(player, hall.get());
                    feed(player, hall.get());
                    rest(player, hall.get());
                }

            }
        }
        return true;
    }

    private static final Map<String, Integer> HEAL_COOLDOWN = new HashMap<>();

    private static void playEffect(Particle particle, Player player) {
        int count = 4;
        double radius = 0.5;
        double offsetYMod = 2;
        Location location = player.getLocation();
        for (int i = 0; i < count; i++) {
            double offsetX = getDoubleBetween(-radius, radius);
            double offsetY = getDoubleBetween(-radius * offsetYMod, radius * offsetYMod);
            double offsetZ = getDoubleBetween(-radius, radius);
            Location offsetLocation = new Location(location.getWorld(),
                    location.getX() + offsetX,
                    location.getY() + offsetY,
                    location.getZ() + offsetZ);
            particleDot(particle, offsetLocation, 1);
        }
    }


    private static void particleDot(Particle particle, Location location, int count) {
        location.getWorld().spawnParticle(particle, location, count, 0d, 0d, 0d, 0d);
    }

    private static double getDoubleBetween(double min, double max) {
        double randomDouble = Math.random();
        randomDouble = randomDouble * (max - min);
        return randomDouble + min;
    }

    private static void heal(Player player, GuildHall hall) {
        String uuid = player.getUniqueId().toString();
        if (HEAL_COOLDOWN.containsKey(uuid)) {
            Integer cooldown = HEAL_COOLDOWN.get(uuid);
            HEAL_COOLDOWN.put(uuid, cooldown - 1);
            if (cooldown < 1) {
                HEAL_COOLDOWN.remove(uuid);
            }
        } else {
            if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                double playerMaxHealth = Math.floor(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                double playerHealth = Math.floor(player.getHealth());
                try {
                    if (!player.isDead() && playerHealth <= (playerMaxHealth - 1.0)) {
                        player.setHealth(playerHealth + 1.0);
                        playEffect(Particle.HEART, player);
                    }

                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().log(Level.SEVERE, "An exception occured setting the players max health", e);
                    ChatUtils.warn("Player Health: " + playerHealth);
                    ChatUtils.warn("Player Max Health: " + playerMaxHealth);
                    ChatUtils.warn("Tried to set health to: " + (playerHealth + 1.0));
                }
            }
            HEAL_COOLDOWN.put(uuid, calculateHealthCooldown(hall.getGuild()));
        }
    }

    private static final Map<String, Integer> FEED_COOLDOWN = new HashMap<>();

    private static void feed(Player player, GuildHall hall) {
        if (hall.getFoodStore() > 0) {
            String uuid = player.getUniqueId().toString();
            if (FEED_COOLDOWN.containsKey(uuid)) {
                Integer cooldown = FEED_COOLDOWN.get(uuid);
                FEED_COOLDOWN.put(uuid, cooldown - 1);
                if (cooldown < 1) {
                    FEED_COOLDOWN.remove(uuid);
                }
            } else {
                if (player.getFoodLevel() < 20) {
                    playEffect(Particle.COMPOSTER, player);
                    player.setFoodLevel(player.getFoodLevel() + 1);
                    hall.setFoodStore(hall.getFoodStore() - 1);
                }
                if (player.getSaturation() < 20) {
                    player.setSaturation(player.getSaturation() + 1);
                }

                FEED_COOLDOWN.put(uuid, calculateHealthCooldown(hall.getGuild()));
            }
        }
    }

    private static final Map<String, Integer> REST_COOLDOWN = new HashMap<>();

    private static void rest(Player player, GuildHall hall) {
        String uuid = player.getUniqueId().toString();
        if (hall.getGuild().listAllBonuses().contains(GuildBonus.RESTING_GROUNDS)) {
            if (REST_COOLDOWN.containsKey(uuid)) {
                Integer cooldown = REST_COOLDOWN.get(uuid);
                REST_COOLDOWN.put(uuid, cooldown - 1);
                if (cooldown < 1) {
                    REST_COOLDOWN.remove(uuid);
                }
            } else {
                playEffect(Particle.FIREWORKS_SPARK, player);
                player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                REST_COOLDOWN.put(uuid, calculateHealthCooldown(hall.getGuild()));
            }
        }
    }

    private static Integer calculateHealthCooldown(Guild guild) {
        return 25 / guild.getRawLevel();
    }

    private static Optional<GuildHall> getHall(Guild guild, Location location) {
        if (guild.getLevel().getGuildBonuses().contains(GuildBonus.GUILD_HALL)
                && guild.getGuildHall().isInHall(location)) {
            return Optional.of(guild.getGuildHall());
        } else {
            return Optional.empty();
        }
    }

}
