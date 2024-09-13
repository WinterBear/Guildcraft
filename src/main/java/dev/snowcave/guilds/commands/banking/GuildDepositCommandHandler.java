package dev.snowcave.guilds.commands.banking;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.EconomyUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildDepositCommandHandler extends GuildMemberPermissionCommandHandler {

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        Chatter chatter = new Chatter(player);
        if (arguments.length > 1) {
            try {
                double amount = Double.parseDouble(arguments[1]);
                if (EconomyUtils.ECONOMY.has(player, amount)) {
                    user.getGuild().deposit(amount);
                    EconomyUtils.ECONOMY.withdrawPlayer(player, amount);
                    user.getGuild().broadcast("&b" + player.getName() + " &3deposited &6" + amount + " &3into the guild bank.");
                } else {
                    chatter.error("You do not have " + amount + "s");
                }
            } catch (NumberFormatException numberFormatException) {
                chatter.error( "Amount must be a number.");
            }
        } else {
            chatter.send( "&7Usage&8: &b/guild deposit &e<&6amount&e> &8- &7Deposit money in your Guild bank.");
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
    public @NotNull String describe() {
        return "&b/guild deposit &e<&6Amount&e> &8- &7Deposit money in your Guild bank.";
    }
}
