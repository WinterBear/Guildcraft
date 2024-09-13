package dev.snowcave.guilds.commands.base;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by WinterBear on 21/01/2021.
 */
public abstract class GuildLeaderCommandHandler implements GuildCommandHandler {

    public void handle(Player player, String[] arguments) {
        Optional<User> user = Guilds.getUser(player);
        if (user.isPresent() && user.get().getGuild().getLeader().getUuid().equals(user.get().getUuid())) {
            handle(player, user.get(), arguments);
        } else {
            ChatUtils.send(player, "&7You are not the leader of a guild.");
        }
    }

    public abstract void handle(Player player, User user, String[] arguments);

    @Override
    public boolean canUse(CommandSender sender) {
        return CommandSenderUtils.isGuildLeader(sender);
    }


}
