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
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created by WinterBear on 22/12/2020.
 */
public class MonsterWard {

    public static void start(JavaPlugin plugin) {
        RepeatingTaskUtils.everyTick(20, new RepeatingTask("Monster Ward", () -> MonsterWard.wardMobs(plugin)), plugin);
    }

    public static boolean wardMobs(JavaPlugin plugin) {
        Guilds.GUILDS.forEach(g -> MonsterWard.wardMobsAsync(g, plugin));
        return true;
    }

    private static boolean checkWardEntity(Entity entity, Guild g){
        if (EntityTypeUtils.HOSTILE_ENTITIES.contains(entity.getType())){
            return true;
        } else if (g.getGuildOptions().isTradersBanned() &&
                EntityTypeUtils.TRADER_ENTITIES.contains(entity.getType())){
            return true;
        }
        return false;
    }

    public static void wardMobsAsync(Guild guild, JavaPlugin plugin){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> MonsterWard.wardMobs(plugin, guild));
    }

    public static void wardMobs(JavaPlugin plugin, Guild guild) {
        if (Levels.getAllGuildBonuses(guild.getLevel()).contains(GuildBonus.MONSTER_WARD)) {
            for (ChunkReference chunkReference : guild.allChunks()) {
                if(chunkReference.getChunkMetadata() == null || chunkReference.getChunkMetadata().isWardMobs()){
                    World world = Bukkit.getServer().getWorld(chunkReference.getWorldRef());
                    if (world != null && world.isChunkLoaded(chunkReference.getX(), chunkReference.getZ())) {
                        Chunk chunk = world.getChunkAt(chunkReference.getX(), chunkReference.getZ());
                        if (chunk.isLoaded()) {
                            Arrays.stream(chunk.getEntities())
                                    .filter(e -> checkWardEntity(e, guild))
                                    .filter(e -> e.getCustomName() == null)
                                    .forEach(e -> MonsterWard.removeEntity(plugin, e));
                        }
                    }
                }
            }
        }
    }

    private static void removeEntity(JavaPlugin plugin, @NotNull Entity entity) {
        if (! Bukkit.getServer().isPrimaryThread()){
            Bukkit.getScheduler().runTask(plugin, entity::remove);
        } else {
            entity.remove();
        }
    }


}
