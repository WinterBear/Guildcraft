package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.commands.general.GuildSubcommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.Chatter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

/**
 * Created by WinterBear on 06/01/2021.
 */
public class ColorOptionHandler implements GuildSubcommandHandler {

    @Override
    public String getKeyword() {
        return "color";
    }

    @Override
    public void handle(Guild guild, Player player, String argument) {
        Chatter chatter = new Chatter(player);
        try {
            if (Pattern.matches("^#(([0-9a-fA-F]{2}){3}|([0-9a-fA-F]){3})$", argument)) {
                guild.getGuildOptions().setColor(argument);
                ChatColor color = ChatColor.of(argument);
                chatter.sendP("Your guild color was set to " + color + argument);
            } else {
                chatter.error(argument + " is not a valid option.");
                chatter.send("&7Color must be in the format #FFFFFF");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            chatter.error(argument + " is not a valid option.");
            chatter.send("&7Color must be in the format #FFFFFF");
        }
    }

    @Override
    public void handleNoArgs(Guild guild, Player player) {
        Chatter chatter = new Chatter(player);
        ChatColor color = ChatColor.of(guild.getGuildOptions().getColor());
        chatter.sendP("Current Color&8: " + color + guild.getGuildOptions().getColor());
    }
}
