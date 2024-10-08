package dev.snowcave.guilds.commands.invites;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.alliances.AllianceController;
import dev.snowcave.guilds.commands.base.GuildMemberCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.EconomyUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildLeaveCommandHandler extends GuildMemberCommandHandler {

    @Override
    public void handle(Player player, User user, String[] arguments) {
        Chatter chatter = new Chatter(player);
        Guild guild = user.getGuild();
        if (guild.getLeader().getUuid().equals(user.getUuid()) && guild.getMembers().size() > 1) {
            User scion = guild.getMembers().stream()
                    .filter(u -> !u.equals(user)) //Don't pick the current leader as the new one
                    .filter(u -> u.hasPermission(GuildPermission.INHERIT)).findAny() //Find heirs if any are present
                    .orElseGet(() -> guild.getMembers().stream().filter(u -> u != user).findAny().orElse(null)); //Pick a random new leader
            guild.broadcast("&b" + player.getName() + " &3has left the guild without appointing a new Leader. " + scion.getName() + " has been chosen as the new Leader.");
            guild.setLeader(scion);
        }
        guild.getMembers().remove(user);
        chatter.sendP("You left &b" + guild.getGuildName());
        if (guild.getMembers().isEmpty()) {
            Guilds.GUILDS.remove(guild);
            if(guild.getAlliance() != null){
                AllianceController.removeGuild(guild);
            }
            Bukkit.broadcastMessage(ChatUtils.format("&3The Guild &b" + guild.getGuildName() + "&3 was disbanded."));
            EconomyUtils.ECONOMY.depositPlayer(player, guild.getBalance());
        }
    }

    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("leave");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild leave &8- &7Leave your Guild.";
    }
}
