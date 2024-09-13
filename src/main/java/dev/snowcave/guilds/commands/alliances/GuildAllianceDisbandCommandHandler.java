package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.core.alliances.Alliance;
import dev.snowcave.guilds.core.alliances.AllianceType;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by WinterBear on 11/11/2023.
 */
public class GuildAllianceDisbandCommandHandler implements GuildCommandHandler {
    @Override
    public List<String> getKeywords() {
        return List.of("disband");
    }

    @Override
    public @NotNull String describe() {
        return GuildAllianceCommandHandler.allianceCommandDescription("disband",
                "Disband your Alliance");
    }

    @Override
    public void handle(Player player, String[] arguments) {
        GuildCommand.asAllianceUser(this::disband, player, arguments);
    }

    public void disband(Player player, User user, Alliance alliance, String[] arguments){
        Chatter chatter = new Chatter(player);
        if(alliance.getLeaders().size() == 1){
            AllianceController.removeAlliance(alliance);
        } else if (alliance.getAllianceType().equals(AllianceType.GRAND_COUNCIL)) {
            chatter.error( "The disbanding of the Grand Council can only be done by vote. Use &e/g a vote &cto trigger a vote.");
        } else {
            chatter.error( "You must be the only Leader of your Alliance to use this.");
        }
    }

    @Override
    public boolean canUse(CommandSender player) {
        return CommandSenderUtils.isAllianceLeader(player);
    }


}
