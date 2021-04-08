package dev.snowcave.guilds.utils;

import org.bukkit.Location;

/**
 * Created by WinterBear on 07/04/2021.
 */
public class LocationUtils {


    public static String toDisplayString(Location location){
        String worldName = location.getWorld().getName();
        if(worldName.equalsIgnoreCase("world")){
            worldName = "Overworld";
        }
        return "&b" + worldName + " &6x&8:&e" + location.getBlockX() + " &2y&8:&a" + location.getBlockY() + " &4z&8:&c" + location.getBlockZ();
    }

}
