package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.commands.general.GuildSubcommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildSymbol;
import dev.snowcave.guilds.utils.Chatter;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class SymbolOptionHandler implements GuildSubcommandHandler {

    @Override
    public String getKeyword() {
        return "symbol";
    }

    @Override
    public void handle(Guild guild, Player player, String argument) {
        Chatter chatter = new Chatter(player);
        try {
            GuildSymbol symbol = GuildSymbol.values()[Integer.parseInt(argument)];
            guild.getGuildOptions().setGuildSymbol(symbol);
            chatter.sendP("&7Your guild symbol was set to " + symbol.getSymbol());
        } catch (NumberFormatException numberFormatException) {
            chatter.error(argument + " is not a valid option.");
            chatter.send("&7Available Symbols (Use /g o symbol <number> to set):");
            StringBuilder line = new StringBuilder();
            for (GuildSymbol symbol : GuildSymbol.values()) {
                line.append("&3").append(symbol.ordinal()).append(" &e").append(symbol.getSymbol()).append(" ");
                if (line.length() > 100) {
                    chatter.send(line.toString());
                    line = new StringBuilder();
                }

            }
            if (!line.isEmpty()) {
                chatter.send(line.toString());
            }
        }
    }

    @Override
    public void handleNoArgs(Guild guild, Player player) {
        ChatUtils.send(player, "&3Current Symbol&8: &6" + guild.getGuildOptions().getGuildSymbol().getSymbol());
        ChatUtils.send(player, "&7Available Symbols (Use /g o symbol <number> to set):");
        StringBuilder line = new StringBuilder();
        for (GuildSymbol symbol : GuildSymbol.values()) {
            line.append("&3").append(symbol.ordinal()).append(" &e").append(symbol.getSymbol()).append(" ");
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
