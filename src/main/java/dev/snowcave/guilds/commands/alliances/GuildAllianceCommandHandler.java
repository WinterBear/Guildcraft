package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.utils.Chatter;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class GuildAllianceCommandHandler implements GuildCommandHandler {

    private static final HashMap<String, GuildCommandHandler> HANDLERS = handlers();

    private static HashMap<String, GuildCommandHandler> handlers(){
        HashMap<String, GuildCommandHandler> handlers = new HashMap<>();

        handlers.put("create", new GuildAllianceCreateCommandHandler());
        handlers.put("deposit", new GuildAllianceDepositCommandHandler());
        handlers.put("disband", new GuildAllianceDisbandCommandHandler());
        handlers.put("invite", new GuildAllianceInviteCommandHandler());
        handlers.put("join", new GuildAllianceJoinCommandHandler());
        handlers.put("promote", new GuildAlliancePromoteCommandHandler());
        handlers.put("demote", new GuildAllianceDemoteCommandHandler());
        //handlers.put("kick", new GuildAllianceKickCommandHandler());
        //handlers.put("leave", new GuildAllianceLeaveCommandHandler());
        //handlers.put("withdraw", new GuildAllianceWithdrawCommandHandler());
        handlers.put("info", new GuildAllianceInfoCommandHandler());
        //handlers.put("Top", new GuildAllianceTopCommandHandler());


        return handlers;
    }

    @Override
    public void handle(Player player, String[] arguments) {
        Chatter chatter = new Chatter(player);
        if(arguments.length > 1){
            String keyword = arguments[1];
            if(HANDLERS.containsKey(keyword)){
                if (HANDLERS.get(keyword).canUse(player)){
                    HANDLERS.get(arguments[1]).handle(player, arguments);
                } else {
                    chatter.error( "You do not have the authority to use this command.");
                }
            }
        } else {
            chatter.sendP("&eAlliance Commands:");
            for (GuildCommandHandler handler : HANDLERS.values()) {
                chatter.send(handler.describe());
            }
        }
    }

    @Override
    public List<String> getKeywords() {
        return List.of("alliance", "a");
    }


    private static final String ALLIANCE_BASE_COMMAND = "&b/guild " + ChatColor.of("#d13824") + "alliance ";

    @Override
    public @NotNull String describe() {
        return ALLIANCE_BASE_COMMAND + " &8- &7Alliance commands";
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if(args.length > 2) {
            if (HANDLERS.containsKey(args[1])) {
                return HANDLERS.get(args[1]).getTabCompletions(sender, args);
            }
        } else {
            return new ArrayList<>(HANDLERS.keySet());
        }
        return List.of();
    }

    public static String allianceCommandDescription(String keyword, String description, String... parameters){
        StringBuilder sb = new StringBuilder()
                .append(ALLIANCE_BASE_COMMAND)
                .append(ChatColor.of("#f55945"))
                .append(keyword);

        for (String parameter : parameters){
            sb.append(" ")
                    .append(ChatColor.of("#f7c28d"))
                    .append("<")
                    .append(ChatColor.of("#ff8000"))
                    .append(parameter)
                    .append(ChatColor.of("#f7c28d"))
                    .append(">");
        }

        if(description != null){
            sb.append(ChatUtils.format(" &8- &7" + description));
        }

        return sb.toString();


    }

}
