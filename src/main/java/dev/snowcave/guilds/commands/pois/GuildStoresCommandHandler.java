package dev.snowcave.guilds.commands.pois;

import dev.snowcave.guilds.commands.base.GuildMemberBonusCommandHandler;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.interaction.GuildStoreListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * Created by WinterBear on 01/01/2021.
 */
public class GuildStoresCommandHandler extends GuildMemberBonusCommandHandler {

    private void openInventory(Player player, int rows, String displayName) {
        Inventory inventory = Bukkit.createInventory(player, rows * 9, displayName);
        player.openInventory(inventory);
        GuildStoreListener.OPEN_STORES.add(player.getUniqueId());
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
        return List.of("stores");
    }

    @Override
    public String describe() {
        return "&b/guild stores &8- &7Deposit food in the guild stores.";
    }
}
