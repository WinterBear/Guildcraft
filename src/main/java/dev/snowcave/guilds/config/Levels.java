package dev.snowcave.guilds.config;

import dev.snowcave.guilds.config.LevelConfBuilder;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WinterBear on 19/12/2020.
 */
public class Levels {

    public static Level get(int level){
        return LEVELS.get(level - 1);
    }

    public static List<Level> LEVELS = levels();

    public static Double getUpgradeCost(Level level){
        return LEVELS.get(level.getLevel()).getCost();
    }

    public static boolean canUpgrade(Level level){
        return LEVELS.size() > level.getLevel();
    }

    public static List<GuildBonus> getAllGuildBonuses(Level level){
        List<GuildBonus> guildBonuses = new ArrayList<>(level.getGuildBonuses());
        for(int i = 0; i < level.getLevel(); i++){
            guildBonuses.addAll(LEVELS.get(i).getGuildBonuses());
        }
        return guildBonuses;
    }

    private static List<Level> levels(){
        return LevelConfBuilder

                //Level 1
                .start(5, 8)

                //Level 2
                .nextLevel(12, 5, 1000.0)
                .withBonus(GuildBonus.GUILD_SPAWN)

                //Level 3
                .nextLevel(14, 6, 2000.0)
                .withBonus(GuildBonus.GUILD_HALL)

                //Level 4
                .nextLevel(16, 7, 3000.0)
                .withBonus(GuildBonus.GUILD_FEASTS)

                //Level 5
                .nextLevel(18, 8, 4000.0)
                .withBonus(GuildBonus.MONSTER_WARD)

                //Level 6
                .nextLevel(20, 10, 5000.0)
                .withBonus(GuildBonus.RESTING_GROUNDS)

                //Level 7
                .nextLevel(22, 15, 8000.0)
                .withBonus(GuildBonus.PVP_EXCLUSIVE_ZONE)

                //Level 8
                .nextLevel(24, 18, 10000.0)
                .withBonus(GuildBonus.NAMED_PLOT)

                //Level 9
                .nextLevel(30, 20, 12000.0)
                .withBonus(GuildBonus.BOOST_MINING_FRENZY)

                //Level 10
                .nextLevel(48, 30, 18000.0)
                .withBonus(GuildBonus.LIVESTOCK_AURA)

                //Level 11
                .nextLevel(60, 35, 20000.0)
                .withBonus(GuildBonus.BOOST_WOODCUTTING_EFFICIENCY)

                //Level 12
                .nextLevel(80, 40, 22000.0)
                .withBonus(GuildBonus.GUILD_HALL_RADIUS_1)

                //Level 13
                .nextLevel(100, 45, 24000.0)
                .withBonus(GuildBonus.BOOST_HUNTING_ATTUNEMENT)

                //Level 14
                .nextLevel(120, 50, 25000.0)
                .withBonus(GuildBonus.OUTPOSTS)

                //Level 15
                .nextLevel(150, 60, 30000.0)
                .withBonus(GuildBonus.GUILD_HALL_RADIUS_2)

                //Level 16
                .nextLevel(160, 70, 31000.0)
                .withBonus(GuildBonus.GUILD_VAULT)

                //Level 17
                .nextLevel(180, 80, 32000.0)
                .withBonus(GuildBonus.NPC_CONTRACT)

                //Level 18
                .nextLevel(200, 90, 33000.0)
                .withBonus(GuildBonus.BOOST_FOOD)

                //Level 19
                .nextLevel(220, 100, 34000.0)
                .withBonus(GuildBonus.GUILD_VAULT_SIZE_UPGRADE_1)

                //Level 20
                .nextLevel(250, 120, 40000.0)
                .withBonus(GuildBonus.BOOST_MIRROR_ATTUNEMENT)

                //Level 21
                .nextLevel(300, 150, 50000.0)
                .withBonus(GuildBonus.GUILD_VAULT_SIZE_UPGRADE_2)

                //Level 22
                .nextLevel(350, 180, 60000.0)
                .withBonus(GuildBonus.GUILD_HALL_RADIUS_3)

                //Level 23
                .nextLevel(400, 220, 70000.0)
                .withBonus(GuildBonus.GUILD_HALL_AMBIENT_MUSIC)

                //Level 24
                .nextLevel(450, 500, 80000.0)
                .withBonus(GuildBonus.NPC_REPAIR)

                //Level 25
                .nextLevel(500, 1000, 100000.0)
                .withBonus(GuildBonus.OUTFITS)

                .build();



    }


}
