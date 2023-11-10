package dev.snowcave.guilds.commands.invites;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.conversations.Invite;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildJoinCommandHandler implements GuildCommandHandler {

    @Override
    public void handle(Player player, String[] arguments) {
        if (arguments.length < 2) {
            ChatUtils.send(player, ChatUtils.format("&b/guild invite &e<&6Guild&e> &8- &7Invite a player to join your Guild."));
        } else {
            String guildName = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
            Optional<Guild> guild = Guilds.getGuild(guildName);
            if (guild.isPresent()) {
                Optional<Invite> invite = GuildInviteCommandHandler.INVITES.stream()
                        .filter(i -> i.getUser().getUuid().equals(player.getUniqueId()))
                        .filter(i -> i.getGuild().equals(guild.get()))
                        .findFirst();
                if (invite.isPresent() || guild.get().isPublic()) {
                    if (guild.get().getLevel().getMaxMembers() > guild.get().getMembers().size()) {
                        User user = invite.get().getUser();
                        user.setGuild(guild.get());
                        guild.get().getMembers().add(user);
                        ChatUtils.send(player, "&bYou joined &6" + guild.get().getGuildName());
                        guild.get().broadcast("&b" + player.getName() + " &3joined the guild!");
                        invite.ifPresent(value -> GuildInviteCommandHandler.INVITES.remove(value));
                    } else {
                        ChatUtils.send(player, "&7That guild is full.");
                    }
                } else {
                    ChatUtils.send(player, ChatUtils.format("&7You have not been invited to &6" + guild.get().getGuildName()));
                }
            } else {
                ChatUtils.send(player, "&7Could not find a guild by that name.");
            }
        }
    }

    @Override
    public List<String> getKeywords() {
        return List.of("join");
    }

    @Override
    public String describe() {
        return "&b/guild join &e<&6Guild&e> &8- &7Join a public Guild, or one you have been invited into.";
    }
}
