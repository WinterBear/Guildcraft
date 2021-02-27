package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.commands.base.GuildMemberCommandHandler;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class GuildInfoCommandHandler extends GuildMemberCommandHandler {

    @Override
    public List<String> getKeywords() {
        return Arrays.asList("info");
    }

    @Override
    public String describe() {
        return "&b/guild info &8- &7Display information about your Guild";
    }

    @Override
    public void handle(Player player, User user, String[] arguments) {
        Guild guild = user.getGuild();
        ChatUtils.send(player, ChatUtils.format("&bGuild&8: &6" + guild.getGuildName() + " &7(&3Lvl. &6" + guild.getLevel() + "&7)"));
        if(guild.getGuildOptions().getNoticeboard() != null && !guild.getGuildOptions().getNoticeboard().equals("")){
            ChatUtils.send(player, ChatUtils.format(guild.getGuildOptions().getNoticeboard()));
        }
        ChatUtils.send(player, ChatUtils.format("&3Leader&8: &6" + guild.getLeader().getName()));
        ChatUtils.send(player, ChatUtils.format("&3Chunk Claims&8: &e" + guild.getChunks().getChunkCount() + "&8/&6" + guild.getLevel().getMaxChunks()));
        ChatUtils.send(player, ChatUtils.format("&3Members&8: &b" + guild.getMembers().stream().map(User::getName).collect(Collectors.joining("&7, &b")))
                + " &8(&e" + guild.getMembers().size() + "&8/&3" + guild.getLevel().getMaxMembers() + "&8)");
        ChatUtils.send(player, ChatUtils.format("&3Balance&8: &e" + guild.getBalance()));
        if(!Levels.getAllGuildBonuses(guild.getLevel()).isEmpty()) {
            ChatUtils.send(player, ChatUtils.format("&6Bonuses&8: &e"));
            for (GuildBonus bonus : Levels.getAllGuildBonuses(guild.getLevel())) {
                ChatUtils.send(player, bonus.toString());
            }
        }
    }

    //
    //Guild: <Guild Name>
    //Leader: <Leader>
    //Role Display
    //Bank Balance
    //Claim Usage x/x
    //
    //Subcommands
    //
    //Upkeep
    //Map
    //Here



}
