package dev.snowcave.guilds.map;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.ChunkReference;
import dev.snowcave.guilds.core.ChunkStore;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.RepeatingTask;
import dev.snowcave.guilds.utils.RepeatingTaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;
import xyz.jpenilla.squaremap.api.marker.MultiPolygon;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

import static org.bukkit.Bukkit.getServer;

/**
 * Created by WinterBear on 28/07/2023.
 */
public class MapRenderer {

    private static final int CHUNKSCALE = 16;

    private static boolean mapEnabled = false;

    private static final HashMap<String, SimpleLayerProvider> LAYER_PROVIDERS = new HashMap<>();

    public static void enable(List<String> worlds, JavaPlugin plugin) {
        RepeatingTaskUtils.everySeconds(30, new RepeatingTask("Guild Map Render Task", () -> renderWorlds(worlds)), plugin);

    }

    private static boolean renderWorlds(List<String> worlds){
        if (getServer().getPluginManager().getPlugin("squaremap") != null) {
            mapEnabled = true;

            for (String world : worlds){
                try {
                    WorldIdentifier identifier = BukkitAdapter.worldIdentifier(Objects.requireNonNull(Bukkit.getWorld(world)));
                    MapWorld mapWorld = SquaremapProvider.get().getWorldIfEnabled(identifier).orElse(null);
                    SimpleLayerProvider layerProvider = getLayerProvider("guilds");
                    assert mapWorld != null;
                    mapWorld.layerRegistry().register(Key.of("guilds"), layerProvider);
                    LAYER_PROVIDERS.put(world, layerProvider);
                    Guilds.GUILDS.forEach(g -> renderGuild(String.valueOf(g.hashCode()), g));
                } catch (IllegalStateException exception) {
                    Bukkit.getLogger().severe("Squaremap API Could not retrieve MapWorld for world " + world);
                    return true;
                }
            }
        }
        Bukkit.getLogger().info("Squaremap API rendering complete");
        return false;
    }

    private static SimpleLayerProvider getLayerProvider(String label) {
        return SimpleLayerProvider.builder(label)
                .showControls(true)
                .defaultHidden(false)
                .layerPriority(5)
                .zIndex(250)
                .build();
    }

    public static void renderGuild(String markerKey, Guild guild) {
        if (mapEnabled) {
            for (String world : LAYER_PROVIDERS.keySet()){
                MultiPolygon multiPolygon = getPolygon(guild, world);
                multiPolygon.markerOptions(getMarkerOptions(guild));
                LAYER_PROVIDERS.get(world).addMarker(Key.of(markerKey), multiPolygon);
            }

        }
    }

    private static MultiPolygon getPolygon(Guild guild, String world) {
        ChunkStore mainChunks = guild.getChunks();
        Collection<ChunkStore> outpostChunks = new ArrayList<>(guild.getOutposts().values());

        List<MultiPolygon.MultiPolygonPart> parts = getPolygon(mainChunks, world);
        outpostChunks.forEach(c -> parts.addAll(getPolygon(c, world)));

        return MultiPolygon.multiPolygon(parts);
    }

    private static ChunkReference findRightMost(ChunkStore store, String world) {
        Optional<ChunkReference> optionalRightMostBlock = store.getChunkReferences().stream().findAny();
        if(optionalRightMostBlock.isPresent()){
            ChunkReference rightMostBlock = optionalRightMostBlock.get();
            for (ChunkReference chunkReference : store.getChunkReferences()) {
                if (chunkReference.getWorldRef().equalsIgnoreCase(world) && (chunkReference.getX() > rightMostBlock.getX()
                        || (chunkReference.getX() == rightMostBlock.getX() && chunkReference.getZ() > rightMostBlock.getZ()))) {
                    rightMostBlock = chunkReference;
                }
            }
            return rightMostBlock;
        }
        return null;
    }

    public static List<MultiPolygon.MultiPolygonPart> getPolygon(ChunkStore store, String world) {

        ChunkReference rightMostBlock = findRightMost(store, world);

        Set<Point> poly = new LinkedHashSet<>();

        // Origin point is the upper left of the chunk
        assert rightMostBlock != null;
        Point originPoint = rightMostBlock.origin(CHUNKSCALE);

        Queue<ChunkReference> guildBlocksToVisit = new ArrayDeque<>(1);
        guildBlocksToVisit.add(rightMostBlock);

        int direction = 0; // RIGHT = 0   DOWN = 1  LEFT =  2  UP = 3

        boolean isOriginPoint = true;
        while (!guildBlocksToVisit.isEmpty()) {

            ChunkReference chunkRef = guildBlocksToVisit.poll();
            //System.out.println("Scanning: " + (direction == 0 ? "Right" : direction == 1 ? "Down": direction == 2 ? "Left": "Right"));

            switch (direction) {
                case 0: {
                    //printPoint("Origin: ", chunkRef.origin(CHUNKSCALE));
                    Point upperLeft = chunkRef.origin(CHUNKSCALE);
                    // We approach the origin from the right so verify we are not back at the origin
                    if (!isOriginPoint && upperLeft.equals(originPoint))
                        continue;
                    else if (isOriginPoint)
                        isOriginPoint = false;

                    // Check if there's a chunk above us
                    // Theoretically can only happen if we are going towards the origin, not away
                    // We have hit a corner!
                    if (store.has(chunkRef.above())) {
                        guildBlocksToVisit.add(chunkRef.above());
                        direction = 3;
                        // Mark UL
                        poly.add(chunkRef.origin(CHUNKSCALE));
                    }
                    // Check if there's a chunk to the right of us
                    else if (store.has(chunkRef.right())) {
                        // Keep Going Right
                        // Don't mark anything
                        guildBlocksToVisit.add(chunkRef.right());
                    } else {
                        // We're the rightmost, so switch direction to down and queue the same block
                        direction = 1;
                        guildBlocksToVisit.add(chunkRef);
                        // Mark UR
                        poly.add(chunkRef.upperRight(CHUNKSCALE));
                    }
                    break;
                }
                case 2: {
                    // Check if there's a townblock below us (This happens at corners)
                    if (store.has(chunkRef.below())) {
                        // Queue the block below
                        guildBlocksToVisit.add(chunkRef.below());
                        // Set direction as down
                        direction = 1;
                        // Mark LR
                        poly.add(chunkRef.lowerRight(CHUNKSCALE));
                    }
                    // Check if there's a townblock to the left of us
                    else if (store.has(chunkRef.left())) {
                        // Keep Going Left
                        // Don't mark anything
                        guildBlocksToVisit.add(chunkRef.left());
                    } else {
                        // We're the leftmost, so switch direction to up
                        direction = 3;
                        guildBlocksToVisit.add(chunkRef);
                        // Mark LL
                        poly.add(chunkRef.lowerLeft(CHUNKSCALE));
                    }

                    break;
                }
                case 1: {
                    // Check if there's a townblock to the right us (We have hit a corner)
                    if (store.has(chunkRef.right())) {
                        // Queue the block right
                        guildBlocksToVisit.add(chunkRef.right());
                        // Set direction as right
                        direction = 0;
                        // Mark UR
                        poly.add(chunkRef.upperRight(CHUNKSCALE));
                    }
                    // Check if there's a townblock below us
                    else if (store.has(chunkRef.below())) {
                        // Keep Going Down
                        // Don't mark anything
                        guildBlocksToVisit.add(chunkRef.below());
                    } else {
                        // We're the bottom most, so make a left turn
                        direction = 2;
                        guildBlocksToVisit.add(chunkRef);
                        // Mark LR
                        poly.add(chunkRef.lowerRight(CHUNKSCALE));
                    }

                    break;
                }
                case 3: {
                    // Check if there's a townblock to the left of us (We have hit a corner)
                    if (store.has(chunkRef.left())) {
                        guildBlocksToVisit.add(chunkRef.left());
                        direction = 2;
                        // Mark LL
                        poly.add(chunkRef.lowerLeft(CHUNKSCALE));
                    }
                    // Check if there's a townblock above us
                    else if (store.has(chunkRef.above())) {
                        // Keep Going up
                        // Don't mark anything
                        guildBlocksToVisit.add(chunkRef.above());
                    } else {
                        // We're the top most, so make a right turn
                        direction = 0;
                        guildBlocksToVisit.add(chunkRef);
                        // Mark UL
                        poly.add(chunkRef.origin(CHUNKSCALE));
                    }

                    break;
                }
            }
        }

        List<MultiPolygon.MultiPolygonPart> parts = new ArrayList<>();
        parts.add(MultiPolygon.part(new ArrayList<>(poly), new ArrayList<>()));
        return parts;

    }

    private static MarkerOptions getMarkerOptions(Guild guild) {
        return MarkerOptions
                .builder()

                .clickTooltip(guild.getGuildName())
                .hoverTooltip(guild.getGuildName())

                .fill(true)
                .fillColor(Color.decode(guild.getGuildOptions().getColor()))
                .fillOpacity(0.2)

                .stroke(true)
                .strokeColor(Color.decode(guild.getGuildOptions().getColor()))
                .strokeWeight(1)
                .strokeOpacity(1.0)

                .build();
    }


}
