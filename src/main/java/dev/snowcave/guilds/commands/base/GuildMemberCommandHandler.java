package dev.snowcave.guilds.commands.base;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.users.User;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import io.github.winterbear.WinterCoreUtils.CommandSenderUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by WinterBear on 28/12/2020.
 */
public abstract class GuildMemberCommandHandler implements GuildCommandHandler {

    public void handle(Player player, String[] arguments){
        Optional<User> user = Guilds.getUser(player);
        if(user.isPresent()){
            handle(player, user.get(), arguments);
        } else {
            ChatUtils.send(player, "&7You do not have a guild.");
        }
    }

    public abstract void handle(Player player, User user, String[] arguments);

    @Override
    public boolean canUse(CommandSender sender) {
        Optional<Player> player = CommandSenderUtils.getPlayer(sender);
        return player.filter(value -> Guilds.getUser(value).isPresent()).isPresent();
    }
}
