package dev.snowcave.guilds.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by WinterBear on 29/02/2024.
 */
public class DelayedTaskScheduler {

    private final JavaPlugin plugin;

    public DelayedTaskScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void runTaskLater(Runnable runnable, Long delay) {
        if (plugin != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }.runTaskLater(plugin, delay);
        }
    }

    public void runTaskLaterSeconds(Runnable runnable, Long delay) {
        runTaskLater(runnable, delay * 20);
    }

}
