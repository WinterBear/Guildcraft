package dev.snowcave.guilds.config;

import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.Level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by WinterBear on 19/12/2020.
 */
public class Levels {

    public static Level get(int level) {
        return LEVELS.get(level - 1);
    }

    public static final List<Level> LEVELS = levels();

    public static Double getUpgradeCost(Level level) {
        return LEVELS.get(level.getLevel()).getCost();
    }

    public static boolean canUpgrade(Level level) {
        return LEVELS.size() > level.getLevel();
    }

    public static List<GuildBonus> getAllGuildBonuses(Level level) {
        Set<GuildBonus> guildBonuses = new HashSet<>(level.getGuildBonuses());
        for (int i = 0; i < level.getLevel(); i++) {
            guildBonuses.addAll(LEVELS.get(i).getGuildBonuses());
        }
        return new ArrayList<>(guildBonuses);
    }

    private static List<Level> levels() {
        return LevelConfBuilder

                //Level 1
                .start(5, 8)

                //Level 2
                .nextLevel(12, 5, 1000.0)
                .withBonus(GuildBonus.GUILD_SPAWN)

                //Level 3
                .nextLevel(14, 6, 3000.0)
                .withBonus(GuildBonus.GUILD_CHAT)

                //Level 4
                .nextLevel(16, 7, 5000.0)


                //Level 5
                .nextLevel(18, 8, 8000.0)
                .withBonus(GuildBonus.MONSTER_WARD)

                //Level 6
                .nextLevel(20, 9, 12000.0)


                //Level 7
                .nextLevel(22, 10, 16000.0)


                //Level 8
                .nextLevel(24, 11, 20000.0)
                .withBonus(GuildBonus.OUTPOSTS_1)

                //Level 9
                .nextLevel(30, 12, 24000.0)


                //Level 10
                .nextLevel(48, 13, 29000.0)
                .withBonus(GuildBonus.GUILD_HALL)

                //Level 11
                .nextLevel(60, 14, 34000.0)
                .withBonus(GuildBonus.GUILD_FEASTS)
                //.withBonus(GuildBonus.BOOST_WOODCUTTING_EFFICIENCY)

                //Level 12
                .nextLevel(80, 15, 40000.0)
                .withBonus(GuildBonus.GUILD_HALL_RADIUS_1)

                //Level 13
                .nextLevel(100, 16, 46000.0)
                .withBonus(GuildBonus.RESTING_GROUNDS)
                //.withBonus(GuildBonus.BOOST_HUNTING_ATTUNEMENT)

                //Level 14
                .nextLevel(120, 17, 52000.0)
                .withBonus(GuildBonus.OUTPOSTS_2)

                //Level 15
                .nextLevel(150, 18, 58000.0)
                .withBonus(GuildBonus.GUILD_HALL_RADIUS_2)

                //Level 16
                .nextLevel(170, 19, 65000.0)
                .withBonus(GuildBonus.BOOST_FOOD)


                //Level 17
                .nextLevel(180, 20, 72000.0)
                //.withBonus(GuildBonus.NPC_CONTRACT)

                //Level 18
                .nextLevel(190, 21, 79000.0)
                .withBonus(GuildBonus.OUTPOSTS_3)

                //Level 19
                .nextLevel(200, 22, 86000.0)


                //Level 20
                .nextLevel(210, 23, 93000.0)
                //.withBonus(GuildBonus.BOOST_MIRROR_ATTUNEMENT)

                //Level 21
                .nextLevel(220, 24, 100000.0)
                .withBonus(GuildBonus.OUTPOSTS_4)

                //Level 22
                .nextLevel(230, 25, 108000.0)
                //.withBonus(GuildBonus.GUILD_HALL_RADIUS_3)

                //Level 23
                .nextLevel(240, 26, 116000.0)
                //.withBonus(GuildBonus.GUILD_HALL_AMBIENT_MUSIC)

                //Level 24
                .nextLevel(250, 27, 124000.0)


                //Level 25
                .nextLevel(260, 28, 132000.0)
                //.withBonus(GuildBonus.ALLIANCES)

                //Level 26
                .nextLevel(270, 29, 140000.0)


                //Level 27
                .nextLevel(280, 30, 148000.0)

                //Level 28
                .nextLevel(290, 31, 156000.0)
                .withBonus(GuildBonus.BOOST_FOOD_2)

                //Level 29
                .nextLevel(300, 32, 164000.0)

                //Level 30
                .nextLevel(310, 33, 172000.0)
                //Warfare

                //Level 31
                .nextLevel(320, 34, 180000.0)

                //Level 32
                .nextLevel(330, 35, 190000.0)
                //.withBonus(GuildBonus.GUILD_AURA_1)

                //Level 33
                .nextLevel(340, 36, 200000.0)

                //Level 34
                .nextLevel(340, 37, 210000.0)

                //Level 35
                .nextLevel(340, 38, 220000.0)
                .withBonus(GuildBonus.BOOST_FOOD_3)

                //Level 36
                .nextLevel(340, 39, 230000.0)
                //.withBonus(GuildBonus.GUILD_AURA_2)

                //Level 37
                .nextLevel(340, 40, 240000.0)

                //Level 38
                .nextLevel(340, 41, 250000.0)

                //Level 39
                .nextLevel(340, 42, 260000.0)

                //Level 40
                .nextLevel(340, 43, 280000.0)
                .withBonus(GuildBonus.BOOST_FOOD_4)

                //Level 41
                .nextLevel(340, 44, 295000.0)

                //Level 42
                .nextLevel(340, 45, 310000.0)

                //Level 43
                .nextLevel(340, 46, 340000.0)

                //Level 44
                .nextLevel(340, 47, 370000.0)
                //.withBonus(GuildBonus.GUILD_AURA_3)

                //Level 45
                .nextLevel(340, 48, 400000.0)

                //Level 46
                .nextLevel(340, 49, 450000.0)

                //Level 47
                .nextLevel(340, 50, 500000.0)

                //Level 48
                .nextLevel(340, 55, 600000.0)

                //Level 49
                .nextLevel(340, 60, 800000.0)

                //Level 50
                .nextLevel(340, 80, 1000000.0)
                .withBonus(GuildBonus.FLIGHT)


                .build();


    }


}
