package dev.snowcave.guilds.commands.invites;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildKickCommandHandler extends GuildMemberPermissionCommandHandler {

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        if (arguments.length < 2) {
            ChatUtils.send(player, ChatUtils.format("&b/guild kick &e<&6Player&e> &8- &7Kick a player from your Guild"));
        } else {
            Player kickPlayer = Bukkit.getPlayer(arguments[1]);
            Guild guild = user.getGuild();
            Optional<User> kickUser = Guilds.getUser(kickPlayer);
            if (kickPlayer != null && kickUser.isPresent() && guild.getMembers().contains(kickUser.get())) {
                if (guild.getLeader().getUuid().equals(kickUser.get().getUuid())) {
                    ChatUtils.send(player, "&cYou cannot kick the Guild Leader.");
                    return;
                }
                guild.broadcast("&b" + player.getName() + " &3kicked &c" + kickPlayer.getName() + " &3from the guild.");
                if (kickPlayer.isOnline()) {
                    ChatUtils.send(kickPlayer, ChatUtils.format("&cYou were kicked from " + guild.getGuildName()));
                }
                guild.getMembers().remove(kickUser.get());
            }

        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.KICK;
    }

    @Override
    public List<String> getKeywords() {
        return Arrays.asList("kick");
    }

    @Override
    public String describe() {
        return "&b/guild kick &e<&6Player&e> &8- &7Kick a player from your Guild";
    }
}
