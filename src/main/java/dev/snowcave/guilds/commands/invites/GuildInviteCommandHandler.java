package dev.snowcave.guilds.commands.invites;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.conversations.Invite;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildInviteCommandHandler extends GuildMemberPermissionCommandHandler {

    public static final HashMap<UUID, Set<Invite>> INVITES = new HashMap<>();

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        Chatter chatter = new Chatter(player);
        if (arguments.length < 2) {
            chatter.sendP("&b/guild invite &e<&6Player&e> &8- &7Invite a player to join your Guild.");
        } else {
            OfflinePlayer invitePlayer = Bukkit.getOfflinePlayer(arguments[1]);
            Guild guild = user.getGuild();
            if (guild.getLevel().getMaxMembers() > guild.getMembers().size()) {
                if (invitePlayer.isOnline() || invitePlayer.hasPlayedBefore()) {
                    if (Guilds.getGuild(invitePlayer.getPlayer()).isPresent()) {
                        chatter.error("That player is already part of a Guild.");
                        return;
                    }
                    INVITES.putIfAbsent(invitePlayer.getUniqueId(), new HashSet<>());
                    INVITES.get(invitePlayer.getUniqueId()).add(new Invite(guild, invitePlayer.getUniqueId()));
                    chatter.sendP("&3Sent invite to &6" + invitePlayer.getName());
                    if (invitePlayer.isOnline() && invitePlayer.getPlayer() != null) {
                        Chatter.sendP(invitePlayer.getPlayer(),"&b" + player.getName() + " &3has invited you to &6" + guild.getGuildName());
                        Chatter.send(invitePlayer.getPlayer(), "&3Use &b/guild join &3to accept the invitation.");
                    }
                } else {
                    chatter.error("&3No player exists by that name.");
                }
            } else {
                chatter.error("&7Your guild does not have any more player slots. Level up to increase the number of available slots.");
            }
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.INVITE;
    }

    @Override
    public List<String> getKeywords() {
        return List.of("invite");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild invite &e<&6Player&e> &8- &7Invite a player to join your Guild.";
    }
}
