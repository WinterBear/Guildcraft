package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by WinterBear on 21/12/2020.
 */
public class GuildLevelCommandHandler extends GuildMemberPermissionCommandHandler {

    @Override
    public List<String> getKeywords() {
        return List.of("levelup");
    }

    @Override
    public String describe() {
        return "&b/guild levelup &8- &7Level up your Guild";
    }

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        Guild guild = user.getGuild();
        if (!Levels.canUpgrade(guild.getLevel())){
            ChatUtils.send(player, ChatUtils.format("&7You are already at the max guild level! Congratulations"));
        } else if (guild.getBalance() >= Levels.getUpgradeCost(guild.getLevel())) {
            guild.withdraw(Levels.getUpgradeCost(guild.getLevel()));
            guild.setRawLevel(guild.getLevel().getLevel() + 1);
            guild.getLevel().getGuildBonuses().forEach(b -> b.levelUpAction(guild));
            Bukkit.broadcastMessage(ChatUtils.format("&bThe Guild &l") + ChatColor.of(guild.getGuildOptions().getColor()) + guild.getGuildName() + ChatUtils.format(" &bupgraded to level &6") + guild.getLevel().getLevel() + ChatUtils.format("&b!"));
            guild.broadcast("&bGuild Bonuses&8: " + guild.getLevel().getGuildBonuses());
            guild.broadcast("&bMax Chunks&8: &3" + guild.getLevel().getMaxChunks() + " &bMax Members&8: &3" + guild.getLevel().getMaxMembers());

        } else {
            //Not enough currency
            ChatUtils.send(player, ChatUtils.format("&7You need " + Levels.getUpgradeCost(guild.getLevel()) + " Silver to purchase the next level for your guild."));
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.LEVELUP;
    }
}
