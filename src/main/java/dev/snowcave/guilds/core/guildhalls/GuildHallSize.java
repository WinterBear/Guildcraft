package dev.snowcave.guilds.core.guildhalls;

/**
 * Created by WinterBear on 22/12/2020.
 */
public enum GuildHallSize {

    SMALL("Small",11.28, 10.0),
    MEDIUM("Medium",16.93, 15.0),
    LARGE("Large",22.57, 20.0),
    HUGE("Huge",28.21, 25.0);

    private final Double circleRadius;

    private final Double squareRadius;

    private final String name;

    GuildHallSize(String name, Double circleRadius, Double squareRadius){
        this.name = name;
        this.circleRadius = circleRadius;
        this.squareRadius = squareRadius;
    }

    public Double getCircleRadius() {
        return circleRadius;
    }

    public Double getSquareRadius() {
        return squareRadius;
    }


    @Override
    public String toString() {
        return name;
    }
}
