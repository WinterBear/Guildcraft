package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.alliances.Alliance;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import dev.snowcave.guilds.utils.EconomyUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by WinterBear on 11/11/2023.
 */
public class GuildAllianceDepositCommandHandler implements GuildCommandHandler {
    @Override
    public List<String> getKeywords() {
        return List.of("deposit");
    }

    @Override
    public @NotNull String describe() {
        return GuildAllianceCommandHandler.allianceCommandDescription("deposit",
                "Deposit money into the Alliance Bank",
                "Amount");
    }

    @Override
    public void handle(Player player, String[] arguments) {
        GuildCommand.asAllianceUser(this::deposit, player, arguments);
    }

    private void deposit(Player player, User user, Alliance alliance, String[] arguments){
        Chatter chatter = new Chatter(player);
        try {
            double amount = Double.parseDouble(arguments[1]);
            if (EconomyUtils.ECONOMY.has(player, amount)) {
                EconomyUtils.ECONOMY.withdrawPlayer(player, amount);
                AllianceController.deposit(player.getName(), alliance, amount);
            } else {
                chatter.error("You do not have " + amount);
            }
        } catch (NumberFormatException numberFormatException) {
            chatter.error( "&7Amount must be a number.");
        }
    }


    @Override
    public boolean canUse(CommandSender player) {
        return CommandSenderUtils.isAllianceMember(player) &&
                CommandSenderUtils.guildHasBonus(player, GuildBonus.ALLIANCES);
    }
}
