package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.alliances.Alliance;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import dev.snowcave.guilds.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 11/11/2023.
 */
public class GuildAllianceInviteCommandHandler implements GuildCommandHandler {


    @Override
    public List<String> getKeywords() {
        return List.of("invite");
    }

    @Override
    public @NotNull String describe() {
        return GuildAllianceCommandHandler.allianceCommandDescription("invite",
                "Invite a Guild to your Alliance");
    }

    @Override
    public void handle(Player player, String[] arguments) {
        GuildCommand.asAllianceLeader(this::invite, player, arguments);
    }

    private void invite(Player player, User user, Alliance alliance, String[] arguments){
        Chatter chatter = new Chatter(player);
        Optional<Guild> inviteGuild = Guilds.getGuild(CommandUtils.squashArgs(2, arguments));
        if (inviteGuild.isPresent()){
            if (inviteGuild.get().getAlliance() != null){
                chatter.error( "That Guild is already part of an Alliance");
            } else {
                AllianceController.sendInvite(alliance, inviteGuild.get().getGuildName());
            }
        } else {
            chatter.error("Could not find a Guild by that name.");
        }
    }

    @Override
    public boolean canUse(CommandSender player) {
        return CommandSenderUtils.isAllianceLeader(player);
    }
}
