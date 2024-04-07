package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.general.GuildSubcommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.Chatter;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 20/01/2021.
 */
public class RenameGuildOptionHandler implements GuildSubcommandHandler {

    @Override
    public String getKeyword() {
        return "rename";
    }

    @Override
    public void handle(Guild guild, Player player, String argument) {
        if (Guilds.getGuild(argument).isPresent()) {
            Chatter.error(player, "&3There is already a Guild called " + argument);
        } else {
            String oldName = guild.getGuildName();
            guild.setGuildName(argument);
            Bukkit.broadcastMessage(ChatUtils.format("&3The Guild &6" + oldName + "&3 was reformed, we are now known as &6" + argument));
        }
    }

    @Override
    public void handleNoArgs(Guild guild, Player player) {
        Chatter.send(player, "&b/g options public &e<&aon&8/&coff&e> &8- &7Allow anyone to join your guild. &8(&6" + guild.getGuildName() + "&8)");
    }
}
