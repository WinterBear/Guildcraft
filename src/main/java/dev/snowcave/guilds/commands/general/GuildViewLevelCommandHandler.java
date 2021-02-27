package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.commands.base.GuildMemberCommandHandler;
import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * Created by WinterBear on 20/01/2021.
 */
public class GuildViewLevelCommandHandler extends GuildMemberCommandHandler {

    @Override
    public void handle(Player player, User user, String[] arguments) {
        int maxLevel = Levels.LEVELS.size();
        try {
            int level = Integer.parseInt(arguments[1]);
            if(level > maxLevel){
                ChatUtils.send(player, "&cError - The maximum level is " + maxLevel);
                return;
            } else {
                ChatUtils.send(player,"&bGuild Bonuses&8: " + Levels.get(level).getGuildBonuses());
                ChatUtils.send(player,"&bMax Chunks&8: &3" + Levels.get(level).getMaxChunks() + " &bMax Members&8: &3" + Levels.get(level).getMaxMembers());
            }
        } catch (NumberFormatException nfe){
            ChatUtils.send(player, "&cError - " + arguments[1] + " is not a number.");
        }

    }

    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("levelinfo");
    }

    @Override
    public String describe() {
        return "&b/guild levelinfo &e<&6level&e> &8- &7View level costs and bonuses";
    }
}
