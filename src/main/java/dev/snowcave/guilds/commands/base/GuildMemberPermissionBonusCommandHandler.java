package dev.snowcave.guilds.commands.base;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import io.github.winterbear.WinterCoreUtils.CommandSenderUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by WinterBear on 29/12/2020.
 */
public abstract class GuildMemberPermissionBonusCommandHandler extends GuildMemberPermissionCommandHandler{

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        if(user.getGuild().listAllBonuses().contains(getBonus())){
            handleWithPermissionAndBonus(player, user, arguments);
        } else {
            ChatUtils.send(player, "&7Your guild has not unlocked this ability.");
        }
    }

    public abstract void handleWithPermissionAndBonus(Player player, User user, String[] arguments);

    public abstract GuildBonus getBonus();

    @Override
    public boolean canUse(CommandSender sender) {
        Optional<Player> player = CommandSenderUtils.getPlayer(sender);
        return player.filter(value -> Guilds.getUser(value).isPresent()
                && Guilds.getUser(value).get().hasPermission(getPermission())
                && Levels.getAllGuildBonuses(Guilds.getUser(value).get().getGuild().getLevel()).contains(getBonus())
        ).isPresent();
    }

}
