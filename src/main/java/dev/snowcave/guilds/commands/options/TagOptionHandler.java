package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.commands.general.GuildSubcommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class TagOptionHandler implements GuildSubcommandHandler {

    @Override
    public String getKeyword() {
        return "tag";
    }

    @Override
    public void handle(Guild guild, Player player, String argument) {
        if (argument.length() < 8) {
            guild.getGuildOptions().setGuildTag(argument.toUpperCase());
            guild.broadcast("&eThe Guild Tag has been changed to &8: &6" + argument.toUpperCase());
        } else {
            Chatter.error(player, "Maximum length of guild tag is 8 characters.");
        }

    }

    @Override
    public void handleNoArgs(Guild guild, Player player) {
        Chatter.sendP(player, "Your guild tag is " + guild.getGuildOptions().getGuildTag());
    }
}
