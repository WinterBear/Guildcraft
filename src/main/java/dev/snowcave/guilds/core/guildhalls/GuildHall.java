package dev.snowcave.guilds.core.guildhalls;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.snowcave.guilds.core.Guild;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by WinterBear on 22/12/2020.
 */
public class GuildHall {

    private String centerString;

    @JsonIgnore
    private Location center;

    private GuildHallSize size;

    private GuildHallShape shape;

    private String name;

    @JsonIgnore
    private Guild guild;

    public boolean isInHall(Location location){
        if(shape == GuildHallShape.SQUARE){
            boolean isInX = location.getX() <= getSquareMaxX() && location.getX() >= getSquareMinX();
            boolean isInZ = location.getZ() <= getSquareMaxZ() && location.getZ() >= getSquareMinZ();
            return isInX && isInZ;
        } else if (shape == GuildHallShape.CIRCLE){
            return center.distance(center) > (size.getCircleRadius());
        }
        return false;
    }


    @JsonIgnore
    private double getSquareMaxX(){
        return this.center.getX() + (size.getSquareRadius());
    }

    @JsonIgnore
    private double getSquareMinX(){
        return this.center.getX() - (size.getSquareRadius());
    }

    @JsonIgnore
    private double getSquareMaxZ(){
        return this.center.getZ() + (size.getSquareRadius());
    }

    @JsonIgnore
    private double getSquareMinZ(){
        return this.center.getZ() - (size.getSquareRadius());
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public void setSize(GuildHallSize size) {
        this.size = size;
    }

    public void setShape(GuildHallShape shape) {
        this.shape = shape;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getCenter() {
        if(center == null){
            String[] locationArray = centerString.split("\\|");
            center = new Location(Bukkit.getWorld(locationArray[0]), Double.parseDouble(locationArray[1]), Double.parseDouble(locationArray[2]), Double.parseDouble(locationArray[3]));
        }
        return center;
    }

    public GuildHallSize getSize() {
        return size;
    }

    public GuildHallShape getShape() {
        return shape;
    }

    public String getName() {
        return name;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public String getCenterString() {
        return centerString;
    }

    public void setCenterString(String centerString){
        this.centerString = centerString;
    }


}
