package dev.snowcave.guilds.interaction;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.ChunkReference;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.utils.EntityTypeUtils;
import dev.snowcave.guilds.utils.RepeatingTask;
import dev.snowcave.guilds.utils.RepeatingTaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

/**
 * Created by WinterBear on 22/12/2020.
 */
public class MonsterWard {

    public static void start(JavaPlugin plugin) {
        RepeatingTaskUtils.everyTick(10, new RepeatingTask("Monster Ward", MonsterWard::wardMobs), plugin);
    }

    public static boolean wardMobs() {
        Guilds.GUILDS.forEach(MonsterWard::wardMobs);
        return true;
    }

    public static void wardMobs(Guild guild) {
        if (Levels.getAllGuildBonuses(guild.getLevel()).contains(GuildBonus.MONSTER_WARD)) {
            for (ChunkReference chunkReference : guild.allChunks()) {
                if(chunkReference.getChunkMetadata() == null || chunkReference.getChunkMetadata().isWardMobs()){
                    World world = Bukkit.getServer().getWorld(chunkReference.getWorldRef());
                    if (world != null) {
                        Chunk chunk = world.getChunkAt(chunkReference.getX(), chunkReference.getZ());
                        if (chunk.isLoaded()) {
                            Arrays.stream(chunk.getEntities())
                                    .filter(e -> EntityTypeUtils.HOSTILE_ENTITIES.contains(e.getType()))
                                    .filter(e -> e.getCustomName() == null)
                                    .forEach(Entity::remove);
                        }
                    }
                }
            }
        }
    }


}
