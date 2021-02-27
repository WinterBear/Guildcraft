package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class NoticeboardOptionHandler implements GuildOptionHandler{

    @Override
    public String getKeyword() {
        return "noticeboard";
    }

    @Override
    public void setValue(Guild guild, Player player, String argument) {
        guild.getGuildOptions().setNoticeboard(argument);
        guild.broadcast("&eA new message has been written on the guild noticeboard&8:");
        guild.broadcast(argument);
    }

    @Override
    public void displayValue(Guild guild, Player player) {
        ChatUtils.send(player, guild.getGuildOptions().getNoticeboard());
    }
}
