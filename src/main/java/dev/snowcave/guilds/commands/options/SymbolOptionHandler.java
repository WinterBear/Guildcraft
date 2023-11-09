package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildSymbol;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class SymbolOptionHandler implements GuildOptionHandler {

    @Override
    public String getKeyword() {
        return "symbol";
    }

    @Override
    public void setValue(Guild guild, Player player, String argument) {
        try {
            GuildSymbol symbol = GuildSymbol.values()[Integer.parseInt(argument)];
            guild.getGuildOptions().setGuildSymbol(symbol);
            ChatUtils.send(player, "&7Your guild symbol was set to " + symbol.getSymbol());
        } catch (NumberFormatException numberFormatException) {
            ChatUtils.send(player, "&cError &8- &c" + argument + " &7is not a valid option.");
            ChatUtils.send(player, "&7Available Symbols (Use /g o symbol <number> to set):");
            StringBuilder line = new StringBuilder();
            for (GuildSymbol symbol : GuildSymbol.values()) {
                line.append("&3" + symbol.ordinal() + " &e" + symbol.getSymbol() + " ");
                if (line.length() > 100) {
                    ChatUtils.send(player, line.toString());
                    line = new StringBuilder();
                }

            }
            if (line.length() > 0) {
                ChatUtils.send(player, line.toString());
            }
        }
    }

    @Override
    public void displayValue(Guild guild, Player player) {
        ChatUtils.send(player, "&3Current Symbol&8: &6" + guild.getGuildOptions().getGuildSymbol().getSymbol());
        ChatUtils.send(player, "&7Available Symbols (Use /g o symbol <number> to set):");
        StringBuilder line = new StringBuilder();
        for (GuildSymbol symbol : GuildSymbol.values()) {
            line.append("&3" + symbol.ordinal() + " &e" + symbol.getSymbol() + " ");
            if (line.length() > 100) {
                ChatUtils.send(player, line.toString());
                line = new StringBuilder();
            }

        }
        if (line.length() > 0) {
            ChatUtils.send(player, line.toString());
        }

    }
}
