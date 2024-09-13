package dev.snowcave.guilds.commands.invites;

import com.google.common.collect.Streams;
import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.config.DefaultRoles;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import dev.snowcave.guilds.utils.conversations.Invite;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildJoinCommandHandler implements GuildCommandHandler {

    private void showInvite(Invite invite, Chatter chatter){
        Component parsed = MiniMessage.miniMessage()
                .deserialize("<dark_gray> - <click:run_command:/g join " + invite.getGuild().getGuildName() + ">" +
                        "<#d13824>" + invite.getGuild().getGuildName() + " <#51f07c>Click to accept." +
                        "</click>");
        chatter.send(parsed);
    }

    @Override
    public void handle(Player player, String[] arguments) {
        Chatter chatter = new Chatter(player);
        if (arguments.length < 2) {
            if (GuildInviteCommandHandler.INVITES.get(player.getUniqueId()).isEmpty()) {
                ChatUtils.send(player, ChatUtils.format("&b/guild join &e<&6Guild&e> &8- &7Join a Guild."));
            } else {
                chatter.sendP("&7You have been invited to the following Guilds:");
                for(Invite invite : GuildInviteCommandHandler.INVITES.get(player.getUniqueId())){
                    showInvite(invite, chatter);
                }
            }


        } else {
            String guildName = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
            Optional<Guild> guild = Guilds.getGuild(guildName);
            if(Guilds.getUser(player).isPresent()){
                chatter.error("&bYou are already in a Guild.");
            } else if (guild.isPresent()) {
                Optional<Invite> invite = GuildInviteCommandHandler.INVITES.get(player.getUniqueId()).stream()
                        .filter(i -> i.getGuild().equals(guild.get()))
                        .findFirst();
                if (invite.isPresent() || guild.get().isPublic()) {
                    if (guild.get().getLevel().getMaxMembers() > guild.get().getMembers().size()) {
                        User user = new User(player.getName(), player.getUniqueId(), DefaultRoles.MEMBER);
                        user.setGuild(guild.get());
                        Chatter.broadcastP(guild.get(), "&b" + player.getName() + " &3joined the guild!");
                        guild.get().getMembers().add(user);
                        chatter.sendP("&bYou joined &6" + guild.get().getGuildName());
                        invite.ifPresent(GuildInviteCommandHandler.INVITES.get(player.getUniqueId())::remove);
                    } else {
                        chatter.error("That guild is full.");
                    }
                } else {
                    chatter.error("You have not been invited to " + guild.get().getGuildName());
                }
            } else {
                chatter.error("Could not find a guild by that name.");
            }
        }
    }

    @Override
    public List<String> getKeywords() {
        return List.of("join");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild join &e<&6Guild&e> &8- &7Join a public Guild, or one you have been invited into.";
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        Optional<Player> player = CommandSenderUtils.getPlayer(sender);
        if (player.isPresent() && args.length > 1) {
            Stream<String> invites = GuildInviteCommandHandler.INVITES
                    .getOrDefault(player.get().getUniqueId(), new HashSet<>()).stream()
                    .map(i -> i.getGuild().getGuildName());
            Stream<String> publicGuilds = Guilds.GUILDS.stream()
                    .filter(Guild::isPublic)
                    .map(Guild::getGuildName);
            return Streams.concat(invites, publicGuilds)
                    .collect(Collectors.toSet()) //Remove duplicates
                    .stream().toList();
        }

        return List.of();
    }

}
