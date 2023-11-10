package dev.snowcave.guilds.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.Callable;
import java.util.logging.Level;

/**
 * Created by WinterBear on 22/12/2020.
 */
public class RepeatingTaskUtils {

    private static final int SECOND_IN_TICKS = 20;

    public static void everyTick(int ticks, RepeatingTask r, JavaPlugin plugin) {
        everyTick(ticks, 1, r, plugin);
    }

    public static void everyTick(int ticks, int delay, RepeatingTask r, JavaPlugin plugin) {
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    if (!r.call()) {
                        cancel();
                    }
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.SEVERE, "An error occurred performing a runnable task: " + r.getReference(), e);
                }
            }
        }.runTaskTimer(plugin, 1, ticks);
    }

    public static void everySeconds(int seconds, RepeatingTask r, JavaPlugin plugin) {
        everyTick(seconds * SECOND_IN_TICKS, SECOND_IN_TICKS, r, plugin);
    }

    public static void everyMinutes(int minutes, RepeatingTask r, JavaPlugin plugin) {
        everySeconds(minutes * 60, r, plugin);
    }


}
