package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.commands.base.GuildMemberBonusCommandHandler;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by WinterBear on 01/12/2023.
 */
public class GuildChatCommandHandler extends GuildMemberBonusCommandHandler implements Listener {

    private static final Set<UUID> GCHATPLAYERS = new HashSet<>();

    @Override
    public List<String> getKeywords() {
        return List.of("chat", "c");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild chat &8- &7Toggle Guild chat";
    }

    @Override
    public void handleWithBonus(Player player, User user, String[] arguments) {
        GuildCommand.asGuildUser(this::toggleChat, player, arguments);


    }

    private void toggleChat(Player player, User user, String[] arguments){
        Chatter chatter = new Chatter(player);
        if(GCHATPLAYERS.contains(player.getUniqueId())){
            GCHATPLAYERS.remove(player.getUniqueId());
            chatter.sendP("Guild chat disabled.");
        } else {
            GCHATPLAYERS.add(player.getUniqueId());
            chatter.sendP("Guild chat enabled.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event){
        if(GCHATPLAYERS.contains(event.getPlayer().getUniqueId()) && !event.isCancelled()){
            Optional<User> userOptional = Guilds.getUser(event.getPlayer());
            if(userOptional.isPresent()){
                event.setCancelled(true);
                User user = userOptional.get();
                user.getGuild().broadcast(ChatColor.of(user.getGuild().getGuildOptions().getColor()) + "⛨ &f" + event.getPlayer().getDisplayName()  + " &8>> " + event.getMessage());
                Bukkit.getLogger().info("[Guild Chat] " + user.getGuild().getGuildName() + " " + event.getPlayer().getName() + ": " + event.getMessage());
            } else {
                GCHATPLAYERS.remove(event.getPlayer().getUniqueId());
            }
        }
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.GUILD_CHAT;
    }
}

