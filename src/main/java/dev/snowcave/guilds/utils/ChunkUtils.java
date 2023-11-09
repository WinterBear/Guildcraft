package dev.snowcave.guilds.utils;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.ChunkReference;
import dev.snowcave.guilds.core.ChunkStore;
import dev.snowcave.guilds.core.Guild;
import org.bukkit.Chunk;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Created by WinterBear on 19/12/2020.
 */
public class ChunkUtils {

    public static boolean chunkIsAdjacentToGuildArea(Guild guild, Chunk chunk) {
        return guild.getChunks().getChunkReferences().stream().anyMatch(c -> isAdjacent(c, chunk));
    }

    public static boolean chunkIsAdjacentToOutpost(Guild guild, Chunk chunk) {
        return guild.getOutposts().values().stream().map(ChunkStore::getChunkReferences).flatMap(Collection::stream).anyMatch(c -> isAdjacent(c, chunk));
    }

    public static boolean chunkIsInOutpost(Guild guild, Chunk chunk) {
        ChunkReference chunkReference = new ChunkReference(chunk);
        guild.initOutposts();
        return guild.getOutposts().values().stream().map(ChunkStore::getChunkReferences).flatMap(Collection::stream).anyMatch(c -> c.equals(chunkReference));
    }

    public static Optional<String> getChunkOutpost(Guild guild, Chunk chunk) {
        ChunkReference chunkReference = new ChunkReference(chunk);
        guild.initOutposts();
        return guild.getOutposts().entrySet().stream()
                .filter(e -> e.getValue().getChunkReferences().stream().anyMatch(c -> c.equals(chunkReference)))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public static Optional<String> getAdjacentOutpost(Guild guild, Chunk chunk) {
        for (String outpost : guild.getOutposts().keySet()) {
            if (guild.getOutposts().get(outpost).getChunkReferences().stream().anyMatch(c -> isAdjacent(c, chunk))) {
                return Optional.of(outpost);
            }
        }

        return Optional.empty();
    }

    public static Optional<Guild> getGuild(Chunk chunk) {
        return Guilds.GUILDS.stream()
                .filter(g -> g.getChunks().isPresent(chunk) || chunkIsInOutpost(g, chunk))
                .findFirst();
    }

    public static boolean chunkMatchesReference(ChunkReference chunkReference, Chunk chunk) {
        return chunkReference.getX() == chunk.getX()
                && chunkReference.getZ() == chunk.getZ()
                && chunkReference.getWorldRef().equalsIgnoreCase(chunk.getWorld().getName());

    }

    public static boolean isChunkClaimed(Chunk chunk) {
        return Guilds.GUILDS.stream()
                .anyMatch(g -> g.getChunks().isPresent(chunk));
    }

    private static boolean isAdjacent(ChunkReference guildChunk, Chunk chunk) {
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                if (guildChunk.getX() + x == chunk.getX() && guildChunk.getZ() + z == chunk.getZ()) {
                    return true;
                }
            }
        }
        return false;
    }


}
