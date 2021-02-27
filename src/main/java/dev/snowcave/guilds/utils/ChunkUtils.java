package dev.snowcave.guilds.utils;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.ChunkReference;
import dev.snowcave.guilds.core.Guild;
import org.bukkit.Chunk;

import java.util.Optional;

/**
 * Created by WinterBear on 19/12/2020.
 */
public class ChunkUtils {

    public static boolean chunkIsAdjacentToGuildArea(Guild guild, Chunk chunk){
        return guild.getChunks().getChunkReferences().stream().anyMatch(c -> isAdjacent(c, chunk));
    }

    public static Optional<Guild> getGuild(Chunk chunk){
        return Guilds.GUILDS.stream()
                .filter(g -> g.getChunks().isPresent(chunk))
                .findFirst();
    }

    public static boolean chunkMatchesReference(ChunkReference chunkReference, Chunk chunk){
        return chunkReference.getX() == chunk.getX()
                && chunkReference.getZ() == chunk.getZ()
                && chunkReference.getWorldRef().equalsIgnoreCase(chunk.getWorld().getName());

    }

    public static boolean isChunkClaimed(Chunk chunk){
        return Guilds.GUILDS.stream()
                .anyMatch(g -> g.getChunks().isPresent(chunk));
    }

    private static boolean isAdjacent(ChunkReference guildChunk, Chunk chunk){
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                if(guildChunk.getX() + x == chunk.getX() && guildChunk.getZ() + z == chunk.getZ()){
                    return true;
                }
            }
        }
        return false;
    }


}
