package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

/**
 * Created by WinterBear on 06/01/2021.
 */
public class ColorOptionHandler implements GuildOptionHandler {

    @Override
    public String getKeyword() {
        return "color";
    }

    @Override
    public void setValue(Guild guild, Player player, String argument) {
        try {
            if (Pattern.matches("^#(([0-9a-fA-F]{2}){3}|([0-9a-fA-F]){3})$", argument)) {
                guild.getGuildOptions().setColor(argument);
                ChatColor color = ChatColor.of(argument);
                ChatUtils.send(player, "&7Your guild color was set to " + color + argument);
            } else {
                ChatUtils.send(player, "&cError &8- &c" + argument + " &7is not a valid option.");
                ChatUtils.send(player, "&7Color must be in the format #FFFFFF");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            ChatUtils.send(player, "&cError &8- &c" + argument + " &7is not a valid option.");
            ChatUtils.send(player, "&7Color must be in the format #FFFFFF");
        }
    }

    @Override
    public void displayValue(Guild guild, Player player) {
        ChatColor color = ChatColor.of(guild.getGuildOptions().getColor());
        ChatUtils.send(player, "&3Current Color&8: " + color + guild.getGuildOptions().getColor());
    }
}
