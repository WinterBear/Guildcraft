package dev.snowcave.guilds.interaction;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.guildhalls.GuildHall;
import dev.snowcave.guilds.utils.ChunkUtils;
import dev.snowcave.guilds.utils.RepeatingTaskUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by WinterBear on 29/12/2020.
 */
public class GuildHallInteractionListener implements Listener {

    //Explore
    @EventHandler
    public void PlayerMove(PlayerMoveEvent event){
        if(event.getTo() != null) {
            Player player = event.getPlayer();
            Optional<Guild> fromGuild = ChunkUtils.getGuild(event.getFrom().getChunk());
            Optional<Guild> toGuild = ChunkUtils.getGuild(event.getTo().getChunk());
            Optional<GuildHall> fromHall = getGuildHall(fromGuild, event.getFrom());
            Optional<GuildHall> toHall = getGuildHall(toGuild, event.getTo());
            if (toHall.isPresent() && !toHall.equals(fromHall)) {
                player.sendTitle(ChatUtils.format("&fﾟ･&8-&f･ &3" + ChatColor.of(toGuild.get().getGuildOptions().getColor()) + toGuild.get().getGuildOptions().getGuildSymbol().getSymbol() + " &f･&8-&f･ﾟ"), ChatUtils.format("&6" + toGuild.get().getGuildHall().getName()), 12, 18, 12);
            }
        }
    }

    public static void start(JavaPlugin plugin){
        RepeatingTaskUtils.everyTick(10, GuildHallInteractionListener::areaOfEffect, plugin);
    }

    public static boolean areaOfEffect(){
        List<GuildHall> halls = Guilds.GUILDS.stream().filter(g -> g.listAllBonuses().contains(GuildBonus.GUILD_HALL))
                .map(Guild::getGuildHall).collect(Collectors.toList());
        if(!halls.isEmpty()){
            for(Player player : Bukkit.getOnlinePlayers()){
                Optional<GuildHall> hall = halls.stream().filter(h -> h.isInHall(player.getLocation())).findFirst();
                hall.ifPresent(guildHall -> heal(player, guildHall));
                hall.ifPresent(guildHall -> feed(player, guildHall));
                hall.ifPresent(guildHall -> rest(player, guildHall));
            }
        }
        return true;
    }

    private static final Map<String, Integer> HEAL_COOLDOWN = new HashMap<>();

    private static void heal(Player player, GuildHall hall){
        String uuid = player.getUniqueId().toString();
        if(HEAL_COOLDOWN.containsKey(uuid)){
            Integer cooldown = HEAL_COOLDOWN.get(uuid);
            HEAL_COOLDOWN.put(uuid, cooldown - 1);
            if(cooldown < 1){
                HEAL_COOLDOWN.remove(uuid);
            }
        } else {
            if(player.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null && player.getHealth() < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()){
                player.setHealth(player.getHealth() + 1);
            }
            HEAL_COOLDOWN.put(uuid, calculateHealthCooldown(hall.getGuild()));
        }
    }

    private static final Map<String, Integer> FEED_COOLDOWN = new HashMap<>();

    private static void feed(Player player, GuildHall hall){
        if(hall.getFoodStore() > 0){
            String uuid = player.getUniqueId().toString();
            if(FEED_COOLDOWN.containsKey(uuid)){
                Integer cooldown = FEED_COOLDOWN.get(uuid);
                FEED_COOLDOWN.put(uuid, cooldown - 1);
                if(cooldown < 1){
                    FEED_COOLDOWN.remove(uuid);
                }
            } else {
                if(player.getFoodLevel() < 20){
                    player.setFoodLevel(player.getFoodLevel() + 1);
                    hall.setFoodStore(hall.getFoodStore() - 1);
                }
                if(player.getSaturation() < 20){
                    player.setSaturation(player.getSaturation() + 1);
                }
                FEED_COOLDOWN.put(uuid, calculateHealthCooldown(hall.getGuild()));
            }
        }
    }

    private static final Map<String, Integer> REST_COOLDOWN = new HashMap<>();

    private static void rest(Player player, GuildHall hall){
        String uuid = player.getUniqueId().toString();
        if(hall.getGuild().listAllBonuses().contains(GuildBonus.RESTING_GROUNDS)) {
            if (REST_COOLDOWN.containsKey(uuid)) {
                Integer cooldown = REST_COOLDOWN.get(uuid);
                REST_COOLDOWN.put(uuid, cooldown - 1);
                if (cooldown < 1) {
                    REST_COOLDOWN.remove(uuid);
                }
            } else {
                player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                REST_COOLDOWN.put(uuid, calculateHealthCooldown(hall.getGuild()));
            }
        }
    }

    private static Integer calculateHealthCooldown(Guild guild){
        return 25/guild.getRawLevel();
    }

    private static Optional<GuildHall> getGuildHall(Optional<Guild> fromGuild, Location from) {
        Optional<GuildHall> fromHall;
        if (fromGuild.isPresent()) {
            fromHall = getHall(fromGuild.get(), from);
        } else {
            fromHall = Optional.empty();
        }
        return fromHall;
    }

    private static Optional<GuildHall> getHall(Guild guild, Location location){
        if(guild.getLevel().getGuildBonuses().contains(GuildBonus.GUILD_HALL)
            && guild.getGuildHall().isInHall(location)){
            return Optional.of(guild.getGuildHall());
        } else {
            return Optional.empty();
        }
    }

}
