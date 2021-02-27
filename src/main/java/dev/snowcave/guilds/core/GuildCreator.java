package dev.snowcave.guilds.core;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.config.DefaultRoles;
import org.bukkit.entity.Player;
import dev.snowcave.guilds.core.users.User;

/**
 * Created by WinterBear on 19/12/2020.
 */
public class GuildCreator {



    public static Guild create(String guildName, Player player){
        User grandMaster = new User(player.getName(), player.getUniqueId(), DefaultRoles.GRAND_MASTER);

        Guild guild = new Guild(guildName, grandMaster);
        grandMaster.setGuild(guild);

        guild.setSpawnLocation(player.getLocation());
        guild.claimChunk(player.getLocation().getChunk());
        guild.setSpawnLocation(player.getLocation());

        Guilds.GUILDS.add(guild);

        return guild;

    }


}
