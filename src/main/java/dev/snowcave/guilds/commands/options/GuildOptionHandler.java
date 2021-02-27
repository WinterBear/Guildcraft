package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public interface GuildOptionHandler {

    String getKeyword();

    void setValue(Guild guild, Player player, String argument);

    void displayValue(Guild guild, Player player);

}
