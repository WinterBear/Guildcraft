package dev.snowcave.guilds.commands.pois;

import dev.snowcave.guilds.commands.base.GuildMemberBonusCommandHandler;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class GuildSpawnCommandHandler extends GuildMemberBonusCommandHandler {

    @Override
    public List<String> getKeywords() {
        return List.of("spawn");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild spawn &8- &7Teleport to your guilds spawn.";
    }

    @Override
    public void handleWithBonus(Player player, User user, String[] arguments) {
        player.teleport(user.getGuild().getSpawnLocation());
        Chatter.sendP(player, "Teleporting to spawn.");
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.GUILD_SPAWN;
    }


}
