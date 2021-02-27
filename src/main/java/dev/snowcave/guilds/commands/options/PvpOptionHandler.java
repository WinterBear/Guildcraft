package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class PvpOptionHandler extends OnOffGuildOptionHandler{

    @Override
    public void enable(Guild guild, Player player) {
        guild.getGuildOptions().setPvpEnabled(true);
        ChatUtils.send(player, "&7PvP is now &aenabled &7within the guild area. Only guild members may PvP.");
    }

    @Override
    public void disable(Guild guild, Player player) {
        guild.getGuildOptions().setPvpEnabled(false);
        ChatUtils.send(player, "&7PvP is now &cdisabled &7within the guild area.");
    }

    @Override
    public String getKeyword() {
        return "pvp";
    }

    @Override
    public void displayValue(Guild guild, Player player) {
        if(guild.getGuildOptions().isPvpEnabled()){
            ChatUtils.send(player, "&7PvP is &aenabled &7within the guild area. Only guild members may PvP.");
        } else {
            ChatUtils.send(player, "&7PvP is &cdisabled &7within the guild area.");
        }
    }
}
