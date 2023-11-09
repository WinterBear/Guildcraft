package dev.snowcave.guilds.commands.invites;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.config.DefaultRoles;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.conversations.Invite;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildInviteCommandHandler extends GuildMemberPermissionCommandHandler {

    public static List<Invite> INVITES = new ArrayList<>();

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        if (arguments.length < 2) {
            ChatUtils.send(player, ChatUtils.format("&b/guild invite &e<&6Player&e> &8- &7Invite a player to join your Guild."));
        } else {
            OfflinePlayer invitePlayer = Bukkit.getOfflinePlayer(arguments[1]);
            Guild guild = user.getGuild();
            if (guild.getLevel().getMaxMembers() > guild.getMembers().size()) {
                if (invitePlayer.isOnline() || invitePlayer.hasPlayedBefore()) {
                    if (Guilds.getGuild(invitePlayer.getPlayer()).isPresent()) {
                        ChatUtils.send(player, ChatUtils.format("&3That player is already part of a Guild."));
                        return;
                    }
                    INVITES.add(new Invite(guild, new User(invitePlayer.getName(), invitePlayer.getUniqueId(), DefaultRoles.MEMBER)));
                    ChatUtils.send(player, ChatUtils.format("&3Sent invite to &6" + invitePlayer.getName()));
                    if (invitePlayer.isOnline()) {
                        ChatUtils.send(invitePlayer.getPlayer(), ChatUtils.format("&b" + player.getName() + " &3has invited you to &6" + guild.getGuildName()));
                        ChatUtils.send(invitePlayer.getPlayer(), ChatUtils.format("&3Use &b/guild join &e<&6Guild&e> &3to accept the invitation."));
                    }
                } else {
                    ChatUtils.send(player, ChatUtils.format("&3No player exists by that name."));
                }
            } else {
                ChatUtils.send(player, ChatUtils.format("&7Your guild does not have any more player slots. Level up to increase the number of available slots."));
            }
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.INVITE;
    }

    @Override
    public List<String> getKeywords() {
        return Arrays.asList("invite");
    }

    @Override
    public String describe() {
        return "&b/guild invite &e<&6Player&e> &8- &7Invite a player to join your Guild.";
    }
}
