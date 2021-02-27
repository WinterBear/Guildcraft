package dev.snowcave.guilds.core.guildhalls;

import dev.snowcave.guilds.core.Guild;

/**
 * Created by WinterBear on 29/12/2020.
 */
public class GuildHallUtils {

    public static void setupGuildHall(Guild guild){
        GuildHall guildHall = new GuildHall();
        guildHall.setCenter(guild.getSpawnLocation());
        guildHall.setShape(GuildHallShape.CIRCLE);
        guildHall.setSize(GuildHallSize.SMALL);
        guildHall.setGuild(guild);
        guild.setGuildHall(guildHall);
    }
}
