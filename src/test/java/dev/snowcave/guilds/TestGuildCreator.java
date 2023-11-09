package dev.snowcave.guilds;

import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildCreator;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by WinterBear on 05/01/2021.
 */
public class TestGuildCreator {

    private static final Map<String, World> WORLDS = new HashMap<>();


    public static Guild createGuild(String name, String worldName) {
        if (!WORLDS.containsKey(worldName)) {
            World world = mock(World.class);
            when(world.getName()).thenReturn(worldName);
            WORLDS.put(worldName, world);
        }
        Player player = mock(Player.class);
        Location location = mock(Location.class);
        Chunk chunk = mock(Chunk.class);
        when(player.getLocation()).thenReturn(location);
        when(location.getChunk()).thenReturn(chunk);
        when(location.getWorld()).thenReturn(WORLDS.get(worldName));
        when(chunk.getX()).thenReturn(43);
        when(chunk.getZ()).thenReturn(18);
        when(chunk.getWorld()).thenReturn(WORLDS.get(worldName));

        return GuildCreator.create(name, player);
    }

    public static Chunk getMockChunk(String worldName) {
        Chunk chunk = mock(Chunk.class);
        when(chunk.getX()).thenReturn(86);
        when(chunk.getZ()).thenReturn(22);
        when(chunk.getWorld()).thenReturn(WORLDS.get(worldName));
        return chunk;
    }

}
