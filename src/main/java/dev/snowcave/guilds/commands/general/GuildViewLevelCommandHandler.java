package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.commands.base.GuildMemberCommandHandler;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Created by WinterBear on 20/01/2021.
 */
public class GuildViewLevelCommandHandler extends GuildMemberCommandHandler {

    @Override
    public void handle(Player player, User user, String[] arguments) {
        Chatter chatter = new Chatter(player);
        if (arguments.length < 2) {
            chatter.send(describe());
            return;
        }
        int maxLevel = Levels.LEVELS.size();
        try {
            int level = Integer.parseInt(arguments[1]);
            if (level > maxLevel) {
                chatter.error("The maximum level is " + maxLevel);
            } else {
                List<GuildBonus> bonuses = Levels.get(level).getGuildBonuses();
                if(!bonuses.isEmpty()){
                    chatter.send("&bGuild Bonuses&8: " + Levels.get(level).getGuildBonuses());
                }
                chatter.send("&bMax Chunks&8: &3" + Levels.get(level).getMaxChunks() + " &bMax Members&8: &3" + Levels.get(level).getMaxMembers());
            }
        } catch (NumberFormatException nfe) {
            chatter.error( arguments[1] + " is not a number.");
        }

    }

    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("levelinfo");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild levelinfo &e<&6Level&e> &8- &7View level costs and bonuses";
    }
}
