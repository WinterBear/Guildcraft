package dev.snowcave.guilds.commands.invites;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by WinterBear on 29/02/2024.
 */
public class GuildAllyCommandHandler  extends GuildMemberPermissionCommandHandler {


    @Override
    public List<String> getKeywords() {
        return List.of("ally");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild ally &e<&aadd&8/&cremove&e> &e<&bplayer&e> &8- &7Allow non guild members to build in your guild space.";
    }

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        Chatter chatter = new Chatter(player);
        if (arguments.length == 3){
            if("add".equalsIgnoreCase(arguments[1])){
                OfflinePlayer ally = Bukkit.getOfflinePlayer(arguments[2]);
                if(user.getGuild().hasMember(ally.getUniqueId())){
                    chatter.error("That player is a Guild Member!");
                } else {
                    user.getGuild().addAlly(ally.getName(), ally.getUniqueId());
                    user.getGuild().broadcast("&b" + ally.getName() + " was made a Guild Ally by " + player.getDisplayName());
                }

                return;
            } else if("remove".equalsIgnoreCase(arguments[1])){
                OfflinePlayer ally = Bukkit.getOfflinePlayer(arguments[2]);
                if (user.getGuild().playerIsAlly(ally)){
                    user.getGuild().removeAlly(ally.getUniqueId());
                    user.getGuild().broadcast("&b" + arguments[2] + " is no longer a Guild Ally");
                } else {
                    chatter.error("That player is not an Ally");
                }

                return;
            }
        }
        Chatter.sendP(player, describe());
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args){
        if (args.length == 2) {
            return List.of("add", "remove");
        }
        return List.of();
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.ALLIES;
    }
}
