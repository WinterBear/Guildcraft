package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.core.alliances.Alliance;
import dev.snowcave.guilds.core.alliances.AllianceType;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 18/11/2023.
 */
public class GuildAllianceDemoteCommandHandler  implements GuildCommandHandler {
    @Override
    public List<String> getKeywords() {
        return List.of("demote");
    }

    @Override
    public @NotNull String describe() {
        return GuildAllianceCommandHandler.allianceCommandDescription("demote",
                "Demote a member of your Alliance");
    }

    @Override
    public void handle(Player player, String[] arguments) {
        GuildCommand.asAllianceLeader(this::demote, player, arguments);
    }

    //demote method
    private void demote(Player player, User user, Alliance alliance, String[] arguments) {
        Chatter chatter = new Chatter(player);
        if (arguments.length > 2) {
            String demotePlayer = arguments[2];
            Optional<User> demoteUser = Guilds.getUser(Bukkit.getOfflinePlayer(demotePlayer));
            if (demoteUser.isPresent()) {
                if (alliance.getLeaders().contains(demoteUser.get().getUuid())) {
                    if (!alliance.getLeaders().contains(user.getUuid())) {
                        chatter.error("You must be a leader to demote another leader.");
                    } else if (alliance.getAllianceType().equals(AllianceType.DEMOCRACY)) {
                        chatter.error("You cannot demote a leader in a democracy.");
                    } else if (alliance.getAllianceType().equals(AllianceType.GRAND_COUNCIL)) {
                        AllianceController.demote(demoteUser.get(), alliance);
                    } else if (alliance.getAllianceType().equals(AllianceType.IMPERIAL)) {
                        chatter.error("You cannot demote yourself.");
                    }
                } else if (alliance.getAssistants().contains(demoteUser.get().getUuid())) {
                    if (!alliance.getLeaders().contains(user.getUuid())) {
                        chatter.error("You must be a leader to demote an assistant.");
                    } else {
                        AllianceController.demote(demoteUser.get(), alliance);
                    }
                } else {
                    chatter.error("That player is not a leader or assistant.");
                }
            } else {
                chatter.error("Could not find a player by that name.");
            }
        }
    }

    @Override
    public boolean canUse(CommandSender player) {
        return CommandSenderUtils.isAllianceLeader(player);
    }

}
