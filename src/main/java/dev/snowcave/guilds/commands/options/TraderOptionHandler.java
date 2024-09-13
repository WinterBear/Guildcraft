package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 11/02/2024.
 */
public class TraderOptionHandler extends OnOffGuildOptionHandler {


    @Override
    public String getKeyword() {
        return "traders";
    }

    @Override
    public void handleNoArgs(Guild guild, Player player) {
        if (guild.getGuildOptions().isTradersBanned()) {
            Chatter.sendP(player, "&7Wandering Traders are currently unwelcome in the Guild.");
        } else {
            Chatter.sendP(player, "&7Wandering Traders are currently welcome in the Guild.");
        }
    }

    @Override
    public void enable(Guild guild, Player player) {
        guild.getGuildOptions().setTradersBanned(false);
        Chatter.sendP(player, "&7Wandering Traders are now welcome in your guild.");
    }

    @Override
    public void disable(Guild guild, Player player) {
        guild.getGuildOptions().setTradersBanned(true);
        Chatter.sendP(player, "&7Wandering Traders are no longer welcome in your guild.");
    }
}
