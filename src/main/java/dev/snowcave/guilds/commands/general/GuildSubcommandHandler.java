package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.core.Guild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by WinterBear on 26/12/2020.
 */
public interface GuildSubcommandHandler {

    String getKeyword();

    void handle(Guild guild, Player player, String argument);

    void handleNoArgs(Guild guild, Player player);

    default List<String> getTabCompletions(CommandSender sender, String[] args) {
        return List.of();
    }

}
