package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.commands.general.GuildSubcommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by WinterBear on 26/12/2020.
 */
public abstract class OnOffGuildOptionHandler implements GuildSubcommandHandler {

    @Override
    public void handle(Guild guild, Player player, String argument) {
        if (argument.equalsIgnoreCase("on")) {
            enable(guild, player);
        } else if (argument.equalsIgnoreCase("off")) {
            disable(guild, player);
        } else {
            Chatter.error(player, argument + " is not valid. Valid arguments are &aon &cor &4off&c.");
        }
    }

    public abstract void enable(Guild guild, Player player);

    public abstract void disable(Guild guild, Player player);

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return List.of("on", "off");
    }

}
