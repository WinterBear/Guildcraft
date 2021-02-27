package dev.snowcave.guilds.config;

import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WinterBear on 19/12/2020.
 */
public class LevelConfBuilder {

    List<Level> levels = new ArrayList<>();

    private int currentLevel = 1;

    private int maxMembers;

    private int maxChunks;

    private List<GuildBonus> guildBonuses;

    private Double cost;

    private LevelConfBuilder(int maxMembers, int maxChunks){
        this.maxChunks = maxChunks;
        this.maxMembers = maxMembers;
        this.guildBonuses = new ArrayList<>();
        this.cost = 0.0;
    }

    public static LevelConfBuilder start(int maxMembers, int maxChunks){
        return new LevelConfBuilder(maxMembers, maxChunks);
    }

    public LevelConfBuilder nextLevel(int maxChunks, int maxMembers, Double cost){
        levels.add(new Level(currentLevel, this.maxChunks, this.maxMembers, guildBonuses, this.cost));
        this.currentLevel++;
        this.maxChunks = maxChunks;
        this.maxMembers = maxMembers;
        this.cost = cost;
        guildBonuses = new ArrayList<>();
        return this;
    }

    public LevelConfBuilder withBonus(GuildBonus bonus){
        guildBonuses.add(bonus);
        return this;
    }

    public List<Level> build(){
        levels.add(new Level(currentLevel, maxChunks, maxMembers, guildBonuses, cost));
        return this.levels;
    }



}
