package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class PublicOptionHandler extends OnOffGuildOptionHandler {

    @Override
    public void enable(Guild guild, Player player) {
        guild.getGuildOptions().setPublic(true);
        Chatter.sendP(player, "&7Your guild is now &bpublic&7. Anyone may join your guild.");
    }

    @Override
    public void disable(Guild guild, Player player) {
        guild.getGuildOptions().setPublic(false);
        Chatter.sendP(player, "&7Your guild is now &cprivate&7. Only players who are invited may join your guild.");
    }

    @Override
    public String getKeyword() {
        return "public";
    }

    @Override
    public void handleNoArgs(Guild guild, Player player) {
        if (guild.getGuildOptions().isPublic()) {
            Chatter.sendP(player, "&7Your guild is &bpublic&7. Anyone may join your guild.");
        } else {
            Chatter.sendP(player, "&7Your guild is &cprivate&7. Only players who are invited may join your guild.");
        }
    }
}
