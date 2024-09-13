package dev.snowcave.guilds.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by WinterBear on 11/02/2024.
 */
public class Outpost  extends ChunkStore{

    private String warppoint;

    @JsonIgnore
    public Location getWarppointLocation() {
        if (warppoint == null){
            return null;
        }
        String[] warppointArray = warppoint.split("\\|");
        return new Location(Bukkit.getWorld(warppointArray[0]), Double.parseDouble(warppointArray[1]), Double.parseDouble(warppointArray[2]), Double.parseDouble(warppointArray[3]));
    }

    @JsonIgnore
    public void setWarppointLocation(Location warppoint) {
        this.warppoint = warppoint.getWorld().getName() + "|" + warppoint.getX() + "|" + warppoint.getY() + "|" + warppoint.getZ();
    }

    public String getWarppoint() {
        return warppoint;
    }

    public void setWarppoint(String warppoint) {
        this.warppoint = warppoint;
    }
}
