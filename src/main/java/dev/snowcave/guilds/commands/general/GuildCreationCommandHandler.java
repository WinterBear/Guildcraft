package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.config.Prices;
import dev.snowcave.guilds.core.GuildCreator;
import dev.snowcave.guilds.utils.EconomyUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class GuildCreationCommandHandler implements GuildCommandHandler {

    @Override
    public List<String> getKeywords() {
        return List.of("create");
    }

    @Override
    public String describe() {
        return "&b/guild create &e<&6Guild Name&e> &8- &7Create a new Guild";
    }

    @Override
    public void handle(Player player, String[] arguments) {
        if (Guilds.getGuild(player).isPresent()) {
            handleExistingGuild(player);
        } else {
            if (arguments.length < 2) {
                ChatUtils.send(player, "&7Usage&8: &b/guild create &e<&6Guild Name&e> &8- &7Create a new Guild");
                ChatUtils.send(player, "&7You need 500 Silver to purchase a guild.");
                return;
            }
            if (EconomyUtils.ECONOMY.has(player, Prices.BASE_GUILD_COST)) {
                EconomyUtils.ECONOMY.withdrawPlayer(player, Prices.BASE_GUILD_COST);
                String guildName = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
                if (Guilds.getGuild(guildName).isPresent()) {
                    ChatUtils.send(player, "&7A guild named " + guildName + " already exists.");
                } else {
                    GuildCreator.create(guildName, player);
                    Bukkit.broadcastMessage(ChatUtils.format("&3The Guild &b" + guildName + " &3was created by &6" + player.getDisplayName()));
                }
            } else {
                ChatUtils.send(player, "&7You need 500 Silver to purchase a guild.");
            }
        }
    }

    private void handleExistingGuild(Player player) {
        ChatUtils.send(player, ChatUtils.format("&7You are already part of a guild. Leave your current guild to create a new one."));
    }
}
