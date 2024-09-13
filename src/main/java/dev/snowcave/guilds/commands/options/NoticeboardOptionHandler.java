package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.commands.general.GuildSubcommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class NoticeboardOptionHandler implements GuildSubcommandHandler {

    @Override
    public String getKeyword() {
        return "noticeboard";
    }

    @Override
    public void handle(Guild guild, Player player, String argument) {
        guild.getGuildOptions().setNoticeboard(argument);
        guild.broadcast("&eA new message has been written on the guild noticeboard&8:");
        guild.broadcast(argument);
    }

    @Override
    public void handleNoArgs(Guild guild, Player player) {
        Chatter.send(player, guild.getGuildOptions().getNoticeboard());
    }
}
