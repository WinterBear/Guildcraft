package dev.snowcave.guilds.utils;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.alliances.Alliance;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by WinterBear on 11/11/2023.
 */
public class CommandSenderUtils {

    public static Optional<Player> getPlayer(CommandSender sender) {
        return Optional.ofNullable(sender.getServer().getPlayer(sender.getName()));
    }

    public static Optional<User> getUser(CommandSender sender){
        Optional<Player> player = getPlayer(sender);
        if(player.isPresent()) {
            return Guilds.getUser(player.get());
        }
        return Optional.empty();
    }

    public static boolean isGuildLeader(CommandSender sender){
        Optional<User> user = getUser(sender);
        return user.map(u -> u.getGuild().getLeader().getUuid()
                .equals(u.getUuid())).orElse(false);
    }

    public static Optional<Alliance> getAlliance(CommandSender sender){
        Optional<User> user = getUser(sender);
        if(user.isPresent() && user.get().getGuild().getAlliance() != null){
            return Optional.of(Guilds.ALLIANCES.get(user.get().getGuild().getAlliance()));
        }

        return Optional.empty();
    }

    public static boolean isAllianceMember(CommandSender sender){
        return getAlliance(sender).isPresent();
    }

    public static boolean isAllianceLeaderOrAssistant(CommandSender sender){
        Optional<Player> player = getPlayer(sender);
        Optional<Alliance> alliance = getAlliance(sender);
        return player.isPresent()
                && alliance.isPresent()
                && (alliance.get().getLeaders()
                .contains(player.get().getUniqueId())
                || alliance.get().getAssistants()
                .contains(player.get().getUniqueId()));
    }

    public static boolean isAllianceLeader(CommandSender sender){
        Optional<Player> player = getPlayer(sender);
        Optional<Alliance> alliance = getAlliance(sender);
        return player.isPresent()
                && alliance.isPresent()
                && alliance.get().getLeaders()
                .contains(player.get().getUniqueId());
    }

    public static boolean isOnlyAllianceLeader(CommandSender sender){
        Optional<Player> player = getPlayer(sender);
        Optional<Alliance> alliance = getAlliance(sender);
        return player.isPresent()
                && alliance.isPresent()
                && alliance.get().getLeaders()
                .contains(player.get().getUniqueId())
                && alliance.get().getLeaders().size() == 1;
    }

    public static boolean guildHasBonus(CommandSender player, GuildBonus guildBonus) {
        Optional<User> optionalUser =  getUser(player);
        return optionalUser.filter(user -> Levels.getAllGuildBonuses(user.getGuild().getLevel())
                .contains(guildBonus)).isPresent();


    }

    public static  boolean userHasGuildPermission(CommandSender player, GuildPermission permission){
        Optional<User> optionalUser =  getUser(player);
        return optionalUser.map(user -> user.hasPermission(permission)).orElse(false);
    }
}
