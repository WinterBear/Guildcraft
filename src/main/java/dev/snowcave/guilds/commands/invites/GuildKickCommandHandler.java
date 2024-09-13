package dev.snowcave.guilds.commands.invites;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildKickCommandHandler extends GuildMemberPermissionCommandHandler {

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        Chatter chatter = new Chatter(player);
        if (arguments.length < 2) {
            chatter.send("&b/guild kick &e<&6Player&e> &8- &7Kick a player from your Guild");
        } else {
            OfflinePlayer kickPlayer = Bukkit.getOfflinePlayer(arguments[1]);
            Guild guild = user.getGuild();
            Optional<User> kickUser = Guilds.getUser(kickPlayer);
            if (kickPlayer != null && kickUser.isPresent() && guild.getMembers().contains(kickUser.get())) {
                if (guild.getLeader().getUuid().equals(kickUser.get().getUuid())) {
                    chatter.error("You cannot kick the Guild Leader.");
                    return;
                }
                guild.broadcast("&b" + player.getName() + " &3kicked &c" + kickPlayer.getName() + " &3from the guild.");
                if (kickPlayer.isOnline()) {
                    Chatter.send(Bukkit.getPlayer(arguments[1]), "&cYou were kicked from " + guild.getGuildName());
                }
                guild.getMembers().remove(kickUser.get());
            } else {
                chatter.sendP("Could not find the player " + arguments[1]);
            }

        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.KICK;
    }

    @Override
    public List<String> getKeywords() {
        return List.of("kick");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild kick &e<&6Player&e> &8- &7Kick a player from your Guild";
    }
}
