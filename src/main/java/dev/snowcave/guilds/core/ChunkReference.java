package dev.snowcave.guilds.core;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by WinterBear on 28/09/2020.
 */
public class ChunkReference {

    private String worldRef;

    private int x;

    private int z;

    public ChunkReference(){
        //Default Constructor
    }

    public ChunkReference(Chunk chunk){
        this.worldRef = chunk.getWorld().getName();
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    public ChunkReference(String worldRef, int x, int z) {
        this.worldRef = worldRef;
        this.x = x;
        this.z = z;
    }

    public Optional<Chunk> get(){
        World world = Bukkit.getServer().getWorld(worldRef);
        if(world!= null){
            return Optional.of(world.getChunkAt(x, z));
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return x + ", " + z + " (" + worldRef + ")";
    }

    public static String toString(Chunk chunk){
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
