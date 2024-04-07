package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.alliances.AllianceController;
import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.EconomyUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by WinterBear on 29/02/2024.
 */
public class GuildDisbandCommandHandler  extends GuildMemberPermissionCommandHandler {

    private static final List<UUID> CONFIRM = new ArrayList<>();

    @Override
    public List<String> getKeywords() {
        return List.of("disband");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild disband &8- &7Disband your Guild.";
    }

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        Chatter chatter = new Chatter(player);
        Guild guild = user.getGuild();

        if (!CONFIRM.contains(player.getUniqueId())) {
            chatter.sendP("&4WARNING&8: &cThis command will DELETE your guild. If you're absolutely sure, use the command again in the next 12 seconds.");
            CONFIRM.add(player.getUniqueId());
            Guilds.SCHEDULER.runTaskLaterSeconds(() -> CONFIRM.remove(player.getUniqueId()), 12L);
        } else {
            chatter.sendP("You disbanded &b" + guild.getGuildName() + " and received " + guild.getBalance() + " from the guild bank.");
            Guilds.GUILDS.remove(guild);
            if(guild.getAlliance() != null){
                AllianceController.removeGuild(guild);
            }
            Bukkit.broadcastMessage(ChatUtils.format("&3The Guild &b" + guild.getGuildName() + "&3 was disbanded by " + player.getDisplayName()));
            EconomyUtils.ECONOMY.depositPlayer(player, guild.getBalance());
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.LEADER;
    }
}
