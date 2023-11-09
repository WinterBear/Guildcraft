package dev.snowcave.guilds.commands.base;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import io.github.winterbear.WinterCoreUtils.CommandSenderUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by WinterBear on 28/12/2020.
 */
public abstract class GuildMemberPermissionCommandHandler extends GuildMemberCommandHandler {

    public void handle(Player player, User user, String[] arguments) {
        if (user.hasPermission(getPermission())) {
            handleWithPermission(player, user, arguments);
        } else {
            ChatUtils.send(player, "&7You do not have permission to use this command.");
        }
    }

    public abstract void handleWithPermission(Player player, User user, String[] arguments);

    public abstract GuildPermission getPermission();

    @Override
    public boolean canUse(CommandSender sender) {
        Optional<Player> player = CommandSenderUtils.getPlayer(sender);
        return player.filter(value -> Guilds.getUser(value).isPresent()
                && Guilds.getUser(value).get().hasPermission(getPermission())).isPresent();
    }


}
