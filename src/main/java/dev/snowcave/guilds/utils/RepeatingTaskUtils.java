package dev.snowcave.guilds.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.Callable;

/**
 * Created by WinterBear on 22/12/2020.
 */
public class RepeatingTaskUtils {

    private static final Long SECOND_IN_TICKS = 20L;

    public static void everyTick(int ticks, Callable<Boolean> r, JavaPlugin plugin){
        new BukkitRunnable(){

            @Override
            public void run() {
                try {
                    if(!r.call()){
                        cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(plugin, 1, ticks);
    }

    public static void everySeconds(int seconds, Callable<Boolean> r, JavaPlugin plugin){

        long period = seconds*SECOND_IN_TICKS;

        new BukkitRunnable(){

            @Override
            public void run() {
                try {
                    if(!r.call()){
                        cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(plugin, SECOND_IN_TICKS, period);
    }

    public static void everyMinutes(int minutes, Callable<Boolean> r, JavaPlugin plugin){
        everySeconds(minutes*60, r, plugin);
    }


}
