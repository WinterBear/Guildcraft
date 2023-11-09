package dev.snowcave.guilds.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Chunk;

import java.util.*;

/**
 * Created by WinterBear on 22/12/2020.
 */
public class ChunkStore {

    private Set<ChunkReference> chunkReferences;

    private Map<String, Map<Integer, Map<Integer, ChunkReference>>> chunks;

    public ChunkStore() {
        this.chunks = new HashMap<>();
        this.chunkReferences = new HashSet<>();
    }

    public ChunkStore(List<ChunkReference> chunkReferenceList) {
        this.chunks = new HashMap<>();
        chunkReferenceList.forEach(this::addChunk);
    }

    @JsonIgnore
    public int getChunkCount() {
        return chunkReferences.size();
    }

    public boolean has(ChunkReference chunkReference) {
        return isPresent(chunkReference.getWorldRef(), chunkReference.getX(), chunkReference.getZ());
    }

    public Optional<ChunkReference> get(Chunk chunk) {
        if (isPresent(chunk)) {
            return Optional.of(getChunk(chunk));
        }
        return Optional.empty();
    }

    private ChunkReference getChunk(Chunk chunk) {
        return chunks.get(chunk.getWorld().getName()).get(chunk.getX()).get(chunk.getZ());
    }

    public Set<ChunkReference> getChunkReferences() {
        return chunkReferences;
    }

    public void setChunkReferences(Set<ChunkReference> chunkReferences) {
        this.chunkReferences = chunkReferences;
        this.chunks = new HashMap<>();
        chunkReferences.forEach(this::addChunk);
    }

    public void addChunk(ChunkReference reference) {
        chunks.putIfAbsent(reference.getWorldRef(), new HashMap<>());
        chunks.get(reference.getWorldRef())
                .putIfAbsent(reference.getX(), new HashMap<>());
        if (!chunks.get(reference.getWorldRef()).get(reference.getX()).containsKey(reference.getZ())) {
            chunks.get(reference.getWorldRef()).get(reference.getX()).put(reference.getZ(), reference);
            chunkReferences.add(reference);
        }

    }

    public void removeChunk(Chunk chunk) {
        if (isPresent(chunk)) {
            ChunkReference chunkReference = chunks.get(chunk.getWorld().getName()).get(chunk.getX()).get(chunk.getZ());
            chunkReferences.remove(chunkReference);
            chunks.get(chunk.getWorld().getName()).get(chunk.getX()).remove(chunk.getZ());
        }
    }

    public boolean isPresent(Chunk chunk) {
        return isPresent(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

    public boolean isPresent(String worldRef, int x, int z) {
        return chunks.containsKey(worldRef)
                && chunks.get(worldRef).containsKey(x)
                && chunks.get(worldRef).get(x).containsKey(z);
    }

}
