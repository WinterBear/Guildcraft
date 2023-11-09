package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class TaxOptionHandler implements GuildOptionHandler {

    @Override
    public String getKeyword() {
        return "tax";
    }

    @Override
    public void setValue(Guild guild, Player player, String argument) {
        try {
            Double newTax = Double.parseDouble(argument);
            guild.getGuildOptions().setTax(newTax);
            guild.broadcast("&7Guild tax was changed to &6" + newTax + " &7per day by &3" + player.getName() + "&7.");
        } catch (NumberFormatException numberFormatException) {
            ChatUtils.send(player, "&cError &8- &c" + argument + " &7is not a valid number.");
        }

    }

    @Override
    public void displayValue(Guild guild, Player player) {
        ChatUtils.send(player, "&7Guild tax is currently &6" + guild.getGuildOptions().getTax() + " &7per day.");
    }
}
