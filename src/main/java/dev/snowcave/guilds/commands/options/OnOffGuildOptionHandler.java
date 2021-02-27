package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public abstract class OnOffGuildOptionHandler implements GuildOptionHandler{

    @Override
    public void setValue(Guild guild, Player player, String argument) {
        if(argument.equalsIgnoreCase("on")){
            enable(guild, player);
        } else if (argument.equalsIgnoreCase("off")){
            disable(guild, player);
        } else {
            ChatUtils.send(player, "&cError &8- &7" + argument + " is not valid. Valid arguments are &aon &7or &coff&7.");
        }
    }

    public abstract void enable(Guild guild, Player player);

    public abstract void disable(Guild guild, Player player);

}
