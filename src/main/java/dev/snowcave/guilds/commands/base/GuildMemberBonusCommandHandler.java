package dev.snowcave.guilds.commands.base;

import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 29/12/2020.
 */
public abstract class GuildMemberBonusCommandHandler extends GuildMemberCommandHandler {

    @Override
    public void handle(Player player, User user, String[] arguments) {
        if (Levels.getAllGuildBonuses(user.getGuild().getLevel()).contains(getBonus())) {
            handleWithBonus(player, user, arguments);
        } else {
            ChatUtils.send(player, "&7Your guild has not unlocked this ability.");
        }
    }

    public abstract void handleWithBonus(Player player, User user, String[] arguments);

    public abstract GuildBonus getBonus();

    @Override
    public boolean canUse(CommandSender sender) {
        return CommandSenderUtils.guildHasBonus(sender, getBonus());
    }
}
