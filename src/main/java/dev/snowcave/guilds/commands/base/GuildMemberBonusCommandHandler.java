package dev.snowcave.guilds.commands.base;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.Level;
import dev.snowcave.guilds.core.users.User;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import io.github.winterbear.WinterCoreUtils.CommandSenderUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sun.nio.cs.US_ASCII;

import javax.jws.soap.SOAPBinding;
import java.util.Optional;

/**
 * Created by WinterBear on 29/12/2020.
 */
public abstract class GuildMemberBonusCommandHandler extends GuildMemberCommandHandler{

    @Override
    public void handle(Player player, User user, String[] arguments) {
        if(Levels.getAllGuildBonuses(user.getGuild().getLevel()).contains(getBonus())){
            handleWithBonus(player, user, arguments);
        } else {
            ChatUtils.send(player, "&7Your guild has not unlocked this ability.");
        }
    }

    public abstract void handleWithBonus(Player player, User user, String[] arguments);

    public abstract GuildBonus getBonus();

    @Override
    public boolean canUse(CommandSender sender) {
        Optional<Player> player = CommandSenderUtils.getPlayer(sender);
        if(player.isPresent()){
            Optional<User> user = Guilds.getUser(player.get());
            if(user.isPresent()){
                if(Levels.getAllGuildBonuses(user.get().getGuild().getLevel()).contains(getBonus())){
                    return true;
                }
            }
        }
        return false;
    }
}
