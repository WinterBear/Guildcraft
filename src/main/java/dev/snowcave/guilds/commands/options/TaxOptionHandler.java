package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.commands.general.GuildSubcommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class TaxOptionHandler implements GuildSubcommandHandler {

    @Override
    public String getKeyword() {
        return "tax";
    }

    @Override
    public void handle(Guild guild, Player player, String argument) {
        try {
            Double newTax = Double.parseDouble(argument);
            guild.getGuildOptions().setTax(newTax);
            guild.broadcast("&7Guild tax was changed to &6" + newTax + " &7per day by &3" + player.getName() + "&7.");
        } catch (NumberFormatException numberFormatException) {
            Chatter.error(player, argument + " &7is not a valid number.");
        }

    }

    @Override
    public void handleNoArgs(Guild guild, Player player) {
        Chatter.sendP(player, "&7Guild tax is currently &6" + guild.getGuildOptions().getTax() + " &7per day.");
    }
}
