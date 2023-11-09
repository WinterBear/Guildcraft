package dev.snowcave.guilds.commands.banking;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.EconomyUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildDepositCommandHandler extends GuildMemberPermissionCommandHandler {

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        if (arguments.length > 1) {
            try {
                double amount = Double.parseDouble(arguments[1]);
                if (EconomyUtils.ECONOMY.has(player, amount)) {
                    user.getGuild().deposit(amount);
                    EconomyUtils.ECONOMY.withdrawPlayer(player, amount);
                    user.getGuild().broadcast("&b" + player.getName() + " &3deposited &6" + amount + " &3in the guild bank.");
                } else {
                    ChatUtils.send(player, "&7You do not have " + amount);
                }
            } catch (NumberFormatException numberFormatException) {
                ChatUtils.send(player, "&cError &8- &7Amount must be a number.");
            }
        } else {
            ChatUtils.send(player, "&b/guild deposit &e<&6amount&e> &8- &7Deposit money in your Guild bank.");
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.DEPOSIT;
    }

    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("deposit");
    }

    @Override
    public String describe() {
        return "&b/guild deposit &e<&6Amount&e> &8- &7Deposit money in your Guild bank.";
    }
}
