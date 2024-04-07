package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import dev.snowcave.guilds.utils.CommandUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 11/11/2023.
 */
public class GuildAllianceJoinCommandHandler implements GuildCommandHandler {


    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("join");
    }

    @Override
    public @NotNull String describe() {
        return GuildAllianceCommandHandler.allianceCommandDescription(
                "join", "Join an Alliance you have been invited to"
        );
    }

    @Override
    public void handle(Player player, String[] arguments) {
        GuildCommand.asGuildUser(this::join, player, arguments);
    }

    private void join(Player player, User user, String[] args){
        Chatter chatter = new Chatter(player);
        if(args.length > 2){
            String joinAlliance = CommandUtils.squashArgs(2, args);
            Optional<AllianceInvite> invite = AllianceController.getAllianceInvite(user, joinAlliance);
            if(invite.isPresent() && Guilds.ALLIANCES.containsKey(joinAlliance)){
                AllianceController.joinAlliance(user.getGuild(), joinAlliance);
            } else {
                chatter.error("You do not have an invite to that Alliance.");
            }
        } else {
            for(AllianceInvite invite : AllianceController.getAllianceInvites(user.getGuild())){
                showInvite(invite, chatter);
            }
        }
    }



    private void showInvite(AllianceInvite invite, Chatter chatter){
        Component parsed = MiniMessage.miniMessage()
                .deserialize("<dark_gray> - <click:run_command:/g a join " + invite.getAlliance() + ">" +
                        "<#d13824>" + invite.getAlliance() + " <#51f07c>Click to accept." +
                        "</click>");
        chatter.send(parsed);
    }

    @Override
    public boolean canUse(CommandSender player) {
        return CommandSenderUtils.isGuildLeader(player);
    }

}
