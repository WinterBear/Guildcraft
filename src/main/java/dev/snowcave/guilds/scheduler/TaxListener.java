package dev.snowcave.guilds.scheduler;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.EconomyUtils;
import dev.snowcave.guilds.utils.RepeatingTask;
import dev.snowcave.guilds.utils.RepeatingTaskUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by WinterBear on 21/01/2021.
 */
public class TaxListener {

    public static void start(JavaPlugin plugin) {
        RepeatingTaskUtils.everyMinutes(5, new RepeatingTask("Tax", TaxListener::doTax), plugin);
    }

    private static boolean doTax() {
        if (!taxDoneForDay()) {
            Guilds.GUILDS.forEach(TaxListener::doTax);
            Guilds.GUILDS.forEach(TaxListener::doUpkeep);
            Chatter.broadcastP("A new day is here. Guild Taxes and Upkeep have been taken.");
        }
        return true;
    }

    //TODO
    private static boolean taxDoneForDay() {
        return true;
    }

    private static void doUpkeep(Guild guild) {
        if (guild.getBalance() >= 10.0) {
            guild.setBalance(guild.getBalance() - 10.0);
        } else {
            Guilds.GUILDS.remove(guild);
            Bukkit.broadcastMessage(ChatUtils.format("&7The Guild " + guild + " was closed due to lack of funding."));
        }
    }

    private static void doTax(Guild guild) {
        Double tax = guild.getGuildOptions().getTax();
        Set<User> kickedPlayers = new HashSet<>();
        for (User user : guild.getMembers()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(user.getUuid());
            if (EconomyUtils.ECONOMY.has(player, tax)) {
                EconomyUtils.ECONOMY.withdrawPlayer(player, tax);
            } else {
                if (guild.getGuildOptions().isKickMembersWhoDontPayTax() && !guild.getLeader().getUuid().equals(user.getUuid())) {
                    kickedPlayers.add(user);
                }
            }
        }
        kickedPlayers.forEach(guild.getMembers()::remove);
    }
}
