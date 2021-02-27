package dev.snowcave.guilds.commands.pois;

import dev.snowcave.guilds.commands.base.GuildMemberBonusCommandHandler;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class GuildSpawnCommandHandler extends GuildMemberBonusCommandHandler {

    @Override
    public List<String> getKeywords() {
        return Arrays.asList("spawn");
    }

    @Override
    public String describe() {
        return "&b/guild spawn &8- &7Teleport to your guilds spawn.";
    }

    @Override
    public void handleWithBonus(Player player, User user, String[] arguments) {
        player.teleport(user.getGuild().getSpawnLocation());
        ChatUtils.send(player, ChatUtils.format("&7Teleporting to spawn."));
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.GUILD_SPAWN;
    }


}
