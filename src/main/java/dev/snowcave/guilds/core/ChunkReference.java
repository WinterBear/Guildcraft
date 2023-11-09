package dev.snowcave.guilds.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import xyz.jpenilla.squaremap.api.Point;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by WinterBear on 28/09/2020.
 */
public class ChunkReference {

    private String worldRef;

    private int x;

    private int z;

    private ChunkMetadata chunkMetadata;

    public ChunkReference() {
        //Default Constructor
    }

    public ChunkReference(Chunk chunk) {
        this.worldRef = chunk.getWorld().getName();
        this.x = chunk.getX();
        this.z = chunk.getZ();
        this.chunkMetadata = new ChunkMetadata();
        this.chunkMetadata.setWardMobs(true);
    }

    public ChunkReference(String worldRef, int x, int z) {
        this.worldRef = worldRef;
        this.x = x;
        this.z = z;
    }

    public ChunkReference above() {
        return new ChunkReference(worldRef, x, z + 1);
    }

    public ChunkReference below() {
        return new ChunkReference(worldRef, x, z - 1);
    }

    public ChunkReference left() {
        return new ChunkReference(worldRef, x - 1, z);
    }

    public ChunkReference right() {
        return new ChunkReference(worldRef, x + 1, z);
    }

    public Optional<Chunk> get() {
        World world = Bukkit.getServer().getWorld(worldRef);
        if (world != null) {
            return Optional.of(world.getChunkAt(x, z));
        }
        return Optional.empty();
    }

    //Top left corner
    public Point origin(int chunkScale) {
        double zPoint = (z * chunkScale) + 15;
        double xPoint = (x * chunkScale);
        return Point.of(xPoint, zPoint);
    }

    public Point upperRight(int chunkScale) {
        double zPoint = (z * chunkScale) + 15;
        double xPoint = (x * chunkScale) + 15;
        return Point.of(xPoint, zPoint);
    }

    public Point lowerRight(int chunkScale) {
        double zPoint = (z * chunkScale);
        double xPoint = (x * chunkScale) + 15;
        return Point.of(xPoint, zPoint);
    }

    public Point lowerLeft(int chunkScale) {
        double zPoint = (z * chunkScale);
        double xPoint = (x * chunkScale);
        return Point.of(xPoint, zPoint);
    }

    @Override
    public String toString() {
        return x + ", " + z + " (" + worldRef + ")";
    }

    public static String toString(Chunk chunk) {
        return chunk.getX() + ", " + chunk.getZ() + " (" + chunk.getWorld().getName() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkReference that = (ChunkReference) o;
        return x == that.x &&
                z == that.z &&
                Objects.equals(worldRef, that.worldRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldRef, x, z);
    }

    public String getWorldRef() {
        return worldRef;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public ChunkMetadata getChunkMetadata() {
        return chunkMetadata;
    }

    @JsonIgnore
    public ChunkMetadata initOrGetChunkMetadata(){
        if(chunkMetadata == null){
            chunkMetadata = new ChunkMetadata();
        }
        return chunkMetadata;
    }

    public void setChunkMetadata(ChunkMetadata chunkMetadata) {
        this.chunkMetadata = chunkMetadata;
    }

    public void setWorldRef(String worldRef) {
        this.worldRef = worldRef;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
