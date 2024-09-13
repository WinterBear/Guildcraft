package dev.snowcave.guilds.commands.general;

import com.google.common.collect.Lists;
import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.Chatter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildTopCommandHandler implements GuildCommandHandler {

    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("top");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild top &e<&6Page&e> &8- &7View top Guilds on the server";
    }

    @Override
    public void handle(Player player, String[] arguments) {
        Chatter chatter = new Chatter(player);
        int partition = 0;
        List<Guild> sortedGuilds = new ArrayList<>(Guilds.GUILDS);
        sortedGuilds.sort(Comparator.comparing(Guild::getRawLevel).thenComparing(Guild::getBalance).reversed());
        List<List<Guild>> pages = Lists.partition(sortedGuilds, 8);
        if (arguments.length > 1) {
            try {
                partition = Integer.parseInt(arguments[1]) - 1;
                if (partition > pages.size() - 1) {
                    chatter.error("Invalid argument \"" + partition + "\". Must be a number between 1 and " + pages.size());
                    return;
                }
            } catch (NumberFormatException numberFormatException) {
                chatter.error("Invalid argument \"" + partition + "\". Must be a number between 1 and " + (pages.size() - 1));
                return;
            }
        }
        if (!pages.isEmpty()) {
            int i = (partition * 8) + 1;
            for (Guild guild : pages.get(partition)) {
                String symbol = guild.getGuildOptions().getGuildSymbol().getSymbol();
                String name = guild.getGuildName();
                ChatColor color = ChatColor.of(guild.getGuildOptions().getColor());
                chatter.send("&6" + i++ + " &f" + symbol + " &3" + color + name + " &7- &b"
                        + guild.getMembers().size() + " Members" + " &8[&eLevel&7: &b" + guild.getRawLevel()
                        + " &eMoney&7: &b" + new DecimalFormat("0.00").format(guild.getBalance()) + "&8]");
            }
        } else {
            chatter.error("&cNo guilds exist.");
        }

    }

}
