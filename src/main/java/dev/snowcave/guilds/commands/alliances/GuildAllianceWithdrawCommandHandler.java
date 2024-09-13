package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.core.alliances.Alliance;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by WinterBear on 11/11/2023.
 */
public class GuildAllianceWithdrawCommandHandler  implements GuildCommandHandler {
    @Override
    public List<String> getKeywords() {
        return null;
    }

    @Override
    public @NotNull String describe() {
        return null;
    }

    @Override
    public void handle(Player player, String[] arguments) {
        GuildCommand.asAllianceLeaderOrAssistant(this::withdraw, player, arguments);
    }

    private void withdraw(Player player, User user, Alliance alliance, String[] arguments){

    }

    @Override
    public boolean canUse(CommandSender player) {
        return CommandSenderUtils.isAllianceLeaderOrAssistant(player);
    }
}
