package dev.snowcave.guilds.commands;

import dev.snowcave.guilds.commands.banking.GuildDepositCommandHandler;
import dev.snowcave.guilds.commands.banking.GuildWithdrawCommandHandler;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.commands.claims.GuildClaimCommandHandler;
import dev.snowcave.guilds.commands.claims.GuildUnclaimCommandHandler;
import dev.snowcave.guilds.commands.general.*;
import dev.snowcave.guilds.commands.invites.GuildInviteCommandHandler;
import dev.snowcave.guilds.commands.invites.GuildJoinCommandHandler;
import dev.snowcave.guilds.commands.invites.GuildKickCommandHandler;
import dev.snowcave.guilds.commands.invites.GuildLeaveCommandHandler;
import dev.snowcave.guilds.commands.options.GuildOptionsCommandHandler;
import dev.snowcave.guilds.commands.pois.GuildHallCommandHandler;
import dev.snowcave.guilds.commands.pois.GuildMoveSpawnCommandHandler;
import dev.snowcave.guilds.commands.pois.GuildSpawnCommandHandler;
import dev.snowcave.guilds.commands.pois.GuildStoresCommandHandler;
import dev.snowcave.guilds.core.guildhalls.GuildHall;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import io.github.winterbear.WinterCoreUtils.CommandSenderUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

/**
 * Created by WinterBear on 28/09/2020.
 */
public class GuildCommand implements CommandExecutor {

    private static final String MAIN_COMMAND_ALIAS = "guild";

    private static final String MAIN_COMMAND_ALIAS_SHORT = "g";

    List<GuildCommandHandler> COMMANDS = Arrays.asList(
            //General
            new GuildClaimCommandHandler(),
            new GuildUnclaimCommandHandler(),
            new GuildCreationCommandHandler(),
            new GuildInfoCommandHandler(),
            new GuildOptionsCommandHandler(),
            new GuildLevelCommandHandler(),
            new GuildViewLevelCommandHandler(),
            new GuildRolesCommandHandler(),

            //Joining & Leaving
            new GuildJoinCommandHandler(),
            new GuildLeaveCommandHandler(),
            new GuildInviteCommandHandler(),
            new GuildKickCommandHandler(),

            //Banking
            new GuildDepositCommandHandler(),
            new GuildWithdrawCommandHandler(),

            //Points of Interest
            new GuildSpawnCommandHandler(),
            new GuildMoveSpawnCommandHandler(),
            new GuildHallCommandHandler(),
            new GuildStoresCommandHandler(),

            //Misc
            new GuildTopCommandHandler(),

            //Leader
            new TransferGuildControlCommandHandler());


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments) {
        if(alias.equalsIgnoreCase(MAIN_COMMAND_ALIAS) || alias.equalsIgnoreCase(MAIN_COMMAND_ALIAS_SHORT)){
            if(arguments.length == 0){
                ChatUtils.send(commandSender, ChatUtils.format("&7Usage&8:"));
                displayUsage(commandSender);
            } else if (COMMANDS.stream().anyMatch(c -> c.getKeywords().stream().anyMatch(k -> k.equalsIgnoreCase(arguments[0])))) {
                guild(commandSender, command, alias, arguments);
            } else {
                ChatUtils.send(commandSender, "&7Invalid argument &c" + arguments[0]);
            }
            return true;
        }
        return false;
    }



    public void displayUsage(CommandSender sender){
        for(GuildCommandHandler commandHandler : COMMANDS){
            if(commandHandler.canUse(sender)){
                ChatUtils.send(sender, commandHandler.describe());
            }
        }
    }

    @io.github.winterbear.wintercore.Annotations.Command(aliases = "g")
    public void guild(CommandSender commandSender, Command command, String alias, String[] arguments){
        if(CommandSenderUtils.getPlayer(commandSender).isPresent()){
            COMMANDS.stream()
                    .filter(c -> c.getKeywords().stream().anyMatch(k -> k.equalsIgnoreCase(arguments[0])))
                    .forEach(c -> c.handle(CommandSenderUtils.getPlayer(commandSender).get(), arguments));
        } else {
            ChatUtils.send(commandSender, "&7Only players may use this command.");
        }

    }
}
