package dev.snowcave.guilds.core;

/**
 * Created by WinterBear on 22/12/2020.
 */
public enum GuildSymbol {

    DEFAULT("✦"),
    FLOWER("❀"),
    HEART("❤"),
    CROSS("✖"),
    MUSIC("♫"),
    MOON("☽"),
    SUN("☀"),
    SKULL("☠"),
    YINYANG("☯"),
    FLORETTE("❁"),
    SPARKLE("❈"),
    COMPASS("❂"),
    STAR("★"),
    COMET("☄"),
    MUG("☕"),
    MARK("※");

    private final String symbol;

    GuildSymbol(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
