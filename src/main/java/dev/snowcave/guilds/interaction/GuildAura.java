package dev.snowcave.guilds.interaction;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 22/11/2023.
 */
public class GuildAura {

    public void doAura() {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(this::eligable)
                .filter(player -> player.getWorld().getPlayers().stream()
                        .filter(p -> sameGuild(player, p))
                        .anyMatch(p -> p.getLocation().distance(player.getLocation()) < 10))
                .forEach(this::doAura);
    }

    private boolean eligable(Player player){
        return Guilds.getUser(player).isPresent() && Levels.getAllGuildBonuses(Guilds.getUser(player).get().getGuild().getLevel()).contains(GuildBonus.GUILD_AURA_1);
    }

    private boolean sameGuild(Player player1, Player player2){
        return Guilds.getUser(player1).isPresent() &&
                Guilds.getUser(player2).isPresent() &&
                Guilds.getUser(player1).get().getGuild().equals(Guilds.getUser(player2).get().getGuild());
    }

    private void doAura(Player player){
        User user = Guilds.getUser(player).get();



    }


}
