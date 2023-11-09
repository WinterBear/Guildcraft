package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class TagOptionHandler implements GuildOptionHandler {

    @Override
    public String getKeyword() {
        return "tag";
    }

    @Override
    public void setValue(Guild guild, Player player, String argument) {
        if (argument.length() < 5) {
            guild.getGuildOptions().setGuildTag(argument.toUpperCase());
            guild.broadcast("&eThe Guild Tag has been changed to &8: &6" + argument.toUpperCase());
        } else {
            ChatUtils.send(player, "&7Maximum length of guild tag is 4 characters.");
        }

    }

    @Override
    public void displayValue(Guild guild, Player player) {
        ChatUtils.send(player, guild.getGuildOptions().getGuildTag());
    }
}
