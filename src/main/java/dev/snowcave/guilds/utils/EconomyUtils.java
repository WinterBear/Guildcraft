package dev.snowcave.guilds.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

/**
 * Created by WinterBear on 21/12/2020.
 */
public class EconomyUtils {

    public static Economy ECONOMY;

    public static void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        ECONOMY = rsp.getProvider();
    }


}
