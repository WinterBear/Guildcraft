package dev.snowcave.guilds.core;

import dev.snowcave.guilds.core.GuildBonus;

import java.util.List;

/**
 * Created by WinterBear on 19/12/2020.
 */
public class Level {

    private int level;

    private int maxMembers;

    private int maxChunks;

    private List<GuildBonus> guildBonuses;

    private Double cost;

    public Level(int level, int maxMembers, int maxChunks, List<GuildBonus> guildBonuses, Double cost) {
        this.level = level;
        this.maxMembers = maxMembers;
        this.maxChunks = maxChunks;
        this.guildBonuses = guildBonuses;
        this.cost = cost;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public int getMaxChunks() {
        return maxChunks;
    }

    public List<GuildBonus> getGuildBonuses() {
        return guildBonuses;
    }

    public Double getCost() {
        return cost;
    }

    @Override
    public String toString(){
        return "" + level;
    }
}
