package dev.snowcave.guilds.interaction;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.guildhalls.GuildHall;
import dev.snowcave.guilds.utils.ChunkUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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

    public boolean areaOfEffect(){
        List<GuildHall> halls = Guilds.GUILDS.stream().filter(g -> g.getLevel().getGuildBonuses().contains(GuildBonus.GUILD_HALL))
                .map(Guild::getGuildHall).collect(Collectors.toList());
        if(!halls.isEmpty()){
            for(Player player : Bukkit.getOnlinePlayers()){
                Optional<GuildHall> hall = halls.stream().filter(h -> h.isInHall(player.getLocation())).findFirst();
                hall.ifPresent(guildHall -> heal(player, guildHall));
            }
        }
        return true;
    }

    private final Map<String, Integer> HEAL_COOLDOWN = new HashMap<>();

    private void heal(Player player, GuildHall hall){
        String uuid = player.getUniqueId().toString();
        if(HEAL_COOLDOWN.containsKey(uuid)){
            Integer cooldown = HEAL_COOLDOWN.get(uuid);
            HEAL_COOLDOWN.put(uuid, cooldown - 1);
            if(cooldown < 1){
                HEAL_COOLDOWN.remove(uuid);
            }
        } else {
            player.setHealth(player.getHealth() + 1);
            HEAL_COOLDOWN.put(uuid, calculateHealthCooldown(hall.getGuild()));
        }
    }

    private Integer calculateHealthCooldown(Guild guild){
        return 10 * (guild.getRawLevel()/25);
    }

    private Optional<GuildHall> getGuildHall(Optional<Guild> fromGuild, Location from) {
        Optional<GuildHall> fromHall;
        if (fromGuild.isPresent()) {
            fromHall = getHall(fromGuild.get(), from);
        } else {
            fromHall = Optional.empty();
        }
        return fromHall;
    }

    private Optional<GuildHall> getHall(Guild guild, Location location){
        if(guild.getLevel().getGuildBonuses().contains(GuildBonus.GUILD_HALL)
            && guild.getGuildHall().isInHall(location)){
            return Optional.of(guild.getGuildHall());
        } else {
            return Optional.empty();
        }
    }

}
