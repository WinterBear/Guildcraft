package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.core.alliances.Alliance;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 12/11/2023.
 */
public class GuildAlliancePromoteCommandHandler implements GuildCommandHandler {

    @Override
    public List<String> getKeywords() {
        return List.of("promote");
    }

    @Override
    public @NotNull String describe() {
        return GuildAllianceCommandHandler.allianceCommandDescription("promote",
                "Promote a member of your Alliance");
    }

    @Override
    public void handle(Player player, String[] arguments) {
        if(arguments.length > 1){
            GuildCommand.asAllianceLeader(this::promote, player, arguments);
        }
    }

    private void promote(Player player, User user, Alliance alliance, String[] arguments){
        Chatter chatter = new Chatter(player);
        if(arguments.length > 2){
            OfflinePlayer promotePlayer = Bukkit.getOfflinePlayer(arguments[2]);
            if(user.getUuid().equals(promotePlayer.getUniqueId())){
                chatter.error("You cannot promote yourself.");
                return;
            }
            Optional<User> promoteUser = Guilds.getUser(promotePlayer);
            if (promoteUser.isPresent()){
                AllianceController.promote(promoteUser.get(), alliance).ifPresent(chatter::send);
            } else {
                chatter.error("Could not find an alliance member by that name.");
            }
        } else {
            chatter.send(describe());
        }

    }

    @Override
    public boolean canUse(CommandSender player) {
        return CommandSenderUtils.isAllianceLeader(player);
    }
}
