package dev.snowcave.guilds.commands.pois;

import dev.snowcave.guilds.commands.base.GuildMemberBonusCommandHandler;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.interaction.GuildStoreListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by WinterBear on 01/01/2021.
 */
public class GuildStoresCommandHandler extends GuildMemberBonusCommandHandler {

    private Inventory openInventory(Player player, int rows, String displayName) {
        Inventory inventory = Bukkit.createInventory(player, rows * 9, displayName);
        player.openInventory(inventory);
        GuildStoreListener.OPEN_STORES.add(player.getUniqueId());
        return inventory;
    }

    @Override
    public void handleWithBonus(Player player, User user, String[] arguments) {
        openInventory(player, 3, "Deposit Food");
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.GUILD_FEASTS;
    }

    @Override
    public List<String> getKeywords() {
        return Arrays.asList("stores");
    }

    @Override
    public String describe() {
        return "&b/guild stores &8- &7Deposit food in the guild stores.";
    }
}
