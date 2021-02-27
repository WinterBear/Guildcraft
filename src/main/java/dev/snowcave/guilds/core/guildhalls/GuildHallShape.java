package dev.snowcave.guilds.core.guildhalls;

/**
 * Created by WinterBear on 22/12/2020.
 */
public enum GuildHallShape {

    SQUARE,
    CIRCLE;


    @Override
    public String toString() {
        return this == SQUARE ? "Square" : "Circle";
    }
}
