package dev.snowcave.guilds.commands;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.banking.GuildDepositCommandHandler;
import dev.snowcave.guilds.commands.banking.GuildWithdrawCommandHandler;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.commands.claims.*;
import dev.snowcave.guilds.commands.general.*;
import dev.snowcave.guilds.commands.invites.*;
import dev.snowcave.guilds.commands.options.GuildOptionsCommandHandler;
import dev.snowcave.guilds.commands.pois.GuildHallCommandHandler;
import dev.snowcave.guilds.commands.pois.GuildMoveSpawnCommandHandler;
import dev.snowcave.guilds.commands.pois.GuildSpawnCommandHandler;
import dev.snowcave.guilds.commands.pois.GuildStoresCommandHandler;
import dev.snowcave.guilds.core.alliances.Alliance;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 28/09/2020.
 */
public class GuildCommand implements CommandExecutor, TabCompleter {

    private static final String MAIN_COMMAND_ALIAS = "guild";

    private static final String MAIN_COMMAND_ALIAS_SHORT = "g";

    private static final List<GuildCommandHandler> COMMANDS = Arrays.asList(
            //General
            new GuildClaimCommandHandler(),
            new GuildUnclaimCommandHandler(),
            new GuildCreationCommandHandler(),
            new GuildInfoCommandHandler(),
            new GuildOptionsCommandHandler(),
            new GuildLevelCommandHandler(),
            new GuildViewLevelCommandHandler(),
            new GuildRolesCommandHandler(),
            new GuildOutpostCommandHandler(),
            new GuildOutpostSetWarpCommandHandler(),
            new GuildOutpostWarpCommandHandler(),
            new GuildWardChunkCommandHandler(),
            //new GuildAllianceCommandHandler(),
            new GuildChatCommandHandler(),

            //Joining & Leaving
            new GuildJoinCommandHandler(),
            new GuildLeaveCommandHandler(),
            new GuildInviteCommandHandler(),
            new GuildKickCommandHandler(),
            new GuildDisbandCommandHandler(),
            new GuildAllyCommandHandler(),

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
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, String alias, String[] arguments) {
        if (alias.equalsIgnoreCase(MAIN_COMMAND_ALIAS) || alias.equalsIgnoreCase(MAIN_COMMAND_ALIAS_SHORT)) {
            if (arguments.length == 0) {
                Optional<Player> player = CommandSenderUtils.getPlayer(commandSender);
                if(player.isPresent()){
                    Chatter chatter = new Chatter(player.get());
                    displayUsage(commandSender, chatter);
                }
            } else if (COMMANDS.stream().anyMatch(c -> c.getKeywords().stream().anyMatch(k -> k.equalsIgnoreCase(arguments[0])))) {
                guild(commandSender, command, alias, arguments);
            } else {
                Chatter.error(commandSender, "Invalid argument &c" + arguments[0]);
            }
            return true;
        }
        return false;
    }


    public void displayUsage(CommandSender sender, Chatter chatter) {
        chatter.sendP("Help & Commands &8>>");
        for (GuildCommandHandler commandHandler : COMMANDS) {
            if (commandHandler.canUse(sender)) {
                chatter.send(commandHandler.describe());
            }
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, String alias, String[] args){
        if (alias.equalsIgnoreCase(MAIN_COMMAND_ALIAS) || alias.equalsIgnoreCase(MAIN_COMMAND_ALIAS_SHORT)) {
            if (args.length < 2){
                return COMMANDS.stream()
                        .filter(c -> c.getKeywords().stream().anyMatch(k -> k.toLowerCase().startsWith(args[0].toLowerCase())))
                        .map(c -> c.getKeywords().stream().filter(k -> k.toLowerCase().startsWith(args[0].toLowerCase())).findFirst())
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
            } else {
                Optional<GuildCommandHandler> commandHandler = COMMANDS.stream()
                        .filter(c -> c.getKeywords().stream().anyMatch(k -> k.equalsIgnoreCase(args[0])))
                        .findFirst();
                if(commandHandler.isPresent()){
                    return commandHandler.get().getTabCompletions(sender, args);
                }
            }
        }
        return List.of();
    }


    @io.github.winterbear.wintercore.Annotations.Command(aliases = "g")
    public void guild(CommandSender commandSender, Command command, String alias, String[] arguments) {
        if (CommandSenderUtils.getPlayer(commandSender).isPresent()) {
            COMMANDS.stream()
                    .filter(c -> c.getKeywords().stream().anyMatch(k -> k.equalsIgnoreCase(arguments[0])))
                    .forEach(c -> c.handle(CommandSenderUtils.getPlayer(commandSender).get(), arguments));
        } else {
            ChatUtils.send(commandSender, "&7Only players may use this command.");
        }

    }

     public static void asAllianceUser(AllianceUserCommandHandle f, Player player, String[] arguments, Condition... conditions){
         Optional<User> user = Guilds.getUser(player);
         if (user.isPresent()) {
             if (user.get().getGuild().getAlliance() == null){
                 ChatUtils.send(player, "&cYou do not have an Alliance.");
             } else {
                 Alliance alliance = Guilds.ALLIANCES.get(user.get().getGuild().getAlliance());
                 Optional<Condition> failure = Arrays.stream(conditions).filter(condition -> condition.check(player, user.get(), alliance, arguments))
                         .findFirst();

                 if(failure.isPresent()){
                     ChatUtils.send(player, failure.get().getMessage(player, user.get(), alliance, arguments));
                 } else {
                     f.handle(player, user.get(), alliance, arguments);
                 }

             }
         } else {
             ChatUtils.send(player, "&cYou do not have a guild.");
         }
     }

     public static void asAllianceLeader(AllianceUserCommandHandle f, Player player, String[] arguments){
         asAllianceUser(f, player, arguments, new Condition(GuildCommand::isAllianceLeader, GuildCommand::notAllianceLeader));
     }

    private static boolean isAllianceLeader(Player player, User user, Alliance alliance, String[] arguments){
        return alliance.getLeaders().stream().anyMatch(l -> l.equals(user.getUuid()));
    }

    private static String notAllianceLeader(Player player, User user, Alliance alliance, String[] arguments){
        return "&cOnly " + alliance.getAllianceType().getLeaderGroupName() + " can use this command.";
    }


    public static void asAllianceLeaderOrAssistant(AllianceUserCommandHandle f, Player player, String[] arguments){
        asAllianceUser(f, player, arguments, new Condition(GuildCommand::isAllianceLeaderOrAssistant, GuildCommand::notAllianceLeaderOrAssistant));
    }

    private static boolean isAllianceLeaderOrAssistant(Player player, User user, Alliance alliance, String[] arguments){
        return alliance.getAssistantsAndLeaders().stream().anyMatch(l -> l.equals(user.getUuid()));
    }

    private static String notAllianceLeaderOrAssistant(Player player, User user, Alliance alliance, String[] arguments){
        return "&cOnly " + alliance.getAllianceType().getLeaderGroupName() + " and "
                + alliance.getAllianceType().getAssistantTitle() + "s can use this command.";
    }

    public static void asGuildUser(GuildUserCommandHandle f, Player player, String[] arguments){
        Optional<User> user = Guilds.getUser(player);
        if (user.isPresent()) {
            f.handle(player, user.get(), arguments);
        } else {
            Chatter.error(player, "You do not have a guild.");
        }
    }

    public interface GuildUserCommandHandle{
        void handle(Player player, User user, String[] strings);
    }

    public interface AllianceUserCommandHandle{
        void handle(Player player, User user, Alliance alliance, String[] arguments);
    }

    public interface AllianceCommandCondition{
        boolean check(Player player, User user, Alliance alliance, String[] arguments);
    }

    public interface AllianceCommandMessage{
        String getMessage(Player player, User user, Alliance alliance, String[] arguments);
    }

    public static class Condition{
        private final AllianceCommandCondition check;
        private final AllianceCommandMessage message;

        public Condition(AllianceCommandCondition check, AllianceCommandMessage message) {
            this.check = check;
            this.message = message;
        }

        public boolean check(Player player, User user, Alliance alliance, String[] arguments) {
            return check.check(player, user, alliance, arguments);
        }

        public String getMessage(Player player, User user, Alliance alliance, String[] arguments) {
            return message.getMessage(player, user, alliance, arguments);
        }
    }
}
