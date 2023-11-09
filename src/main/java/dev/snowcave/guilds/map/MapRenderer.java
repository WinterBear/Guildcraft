package dev.snowcave.guilds.map;

import dev.snowcave.guilds.core.ChunkReference;
import dev.snowcave.guilds.core.ChunkStore;
import dev.snowcave.guilds.core.Guild;
import org.bukkit.Bukkit;
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

    private static SimpleLayerProvider layerProvider = null;

    private static xyz.jpenilla.squaremap.api.MapWorld mapWorld;

    public static void enable() {
        if (getServer().getPluginManager().getPlugin("squaremap") == null) {
            return;
        } else {
            mapEnabled = true;
            layerProvider = getLayerProvider("guilds");
            WorldIdentifier identifier = BukkitAdapter.worldIdentifier(Bukkit.getWorld("world"));
            mapWorld = SquaremapProvider.get().getWorldIfEnabled(identifier).orElse(null);
            mapWorld.layerRegistry().register(Key.of("guilds"), layerProvider);
        }
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
        MultiPolygon multiPolygon = getPolygon(guild);
        multiPolygon.markerOptions(getMarkerOptions(guild));

        layerProvider.addMarker(Key.of(markerKey), multiPolygon);
    }

    private static MultiPolygon getPolygon(Guild guild) {

        ChunkStore mainChunks = guild.getChunks();
        Collection<ChunkStore> outpostChunks = guild.getOutposts().values();

        List<MultiPolygon.MultiPolygonPart> parts = getPolygon(mainChunks);
        outpostChunks.forEach(c -> parts.addAll(getPolygon(c)));

        return MultiPolygon.multiPolygon(parts);


    }

    private static ChunkReference findRightMost(ChunkStore store) {
        ChunkReference rightMostBlock = store.getChunkReferences().stream().findAny().get();
        for (ChunkReference chunkReference : store.getChunkReferences()) {
            if (chunkReference.getX() > rightMostBlock.getX()
                    || (chunkReference.getX() == rightMostBlock.getX() && chunkReference.getZ() > rightMostBlock.getZ())) {
                rightMostBlock = chunkReference;
            }
        }

        return rightMostBlock;
    }

    public static List<MultiPolygon.MultiPolygonPart> getPolygon(ChunkStore store) {

        ChunkReference rightMostBlock = findRightMost(store);

        Set<Point> poly = new LinkedHashSet<>();

        // Origin point is the upper left of the chunk
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

                    long offsetHash;
                    // Check if there's a chunk above us
                    // Theoretically can only happen if we are going towards the origin, not away
                    // We have hit a corner!
                    if (store.has(chunkRef.above())) {
                        guildBlocksToVisit.add(chunkRef.above());
                        direction = 3;
                        // Mark UL
                        //printPoint("Upper Left: ", chunkRef.origin(CHUNKSCALE));
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
                        //printPoint("Upper Right: ", chunkRef.upperRight(CHUNKSCALE));
                        poly.add(chunkRef.upperRight(CHUNKSCALE));
                    }
                    break;
                }
                case 2: {
                    long offsetHash;
                    // Check if there's a townblock below us (This happens at corners)
                    if (store.has(chunkRef.below())) {
                        // Queue the block below
                        guildBlocksToVisit.add(chunkRef.below());
                        // Set direction as down
                        direction = 1;
                        // Mark LR
                        //printPoint("Lower Right: ",  chunkRef.lowerRight(CHUNKSCALE));
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
                        //printPoint("Lower Left: ", chunkRef.lowerLeft(CHUNKSCALE));
                        poly.add(chunkRef.lowerLeft(CHUNKSCALE));
                    }

                    break;
                }
                case 1: {
                    long offsetHash;
                    // Check if there's a townblock to the right us (We have hit a corner)
                    if (store.has(chunkRef.right())) {
                        // Queue the block right
                        guildBlocksToVisit.add(chunkRef.right());
                        // Set direction as right
                        direction = 0;
                        // Mark UR
                        //printPoint("Upper Right: ", chunkRef.upperRight(CHUNKSCALE));
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
                        //printPoint("Lower Right: ", chunkRef.lowerRight(CHUNKSCALE));
                        poly.add(chunkRef.lowerRight(CHUNKSCALE));
                    }

                    break;
                }
                case 3: {
                    long offsetHash;
                    // Check if there's a townblock to the left of us (We have hit a corner)
                    if (store.has(chunkRef.left())) {
                        guildBlocksToVisit.add(chunkRef.left());
                        direction = 2;
                        // Mark LL
                        //printPoint("Lower Left: ", chunkRef.lowerLeft(CHUNKSCALE));
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
                        //printPoint("Upper Left: ", chunkRef.origin(CHUNKSCALE));
                        poly.add(chunkRef.origin(CHUNKSCALE));
                    }

                    break;
                }
            }
        }


        //List<ChunkReference> negativeSpace todo
        List<MultiPolygon.MultiPolygonPart> parts = new ArrayList<>();

        parts.add(MultiPolygon.part(new ArrayList<>(poly), new ArrayList<>()));
        return parts;

    }

    private static void printPoint(String prefix, Point p) {
        System.out.println(prefix + p.x() + "," + p.z());
    }


    private static MarkerOptions getMarkerOptions(Guild guild) {
        return MarkerOptions
                .builder()
                .clickTooltip(guild.getGuildName())
                .hoverTooltip(guild.getGuildName())
                .fill(true)
                .fillOpacity(0.2)
                .stroke(true)
                .strokeColor(Color.decode(guild.getGuildOptions().getColor()))
                .strokeWeight(1)
                .strokeOpacity(1.0)
                .fillColor(Color.decode(guild.getGuildOptions().getColor()))
                .build();
    }


}
