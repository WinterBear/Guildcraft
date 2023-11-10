package dev.snowcave.guilds.map;

import dev.snowcave.guilds.core.ChunkReference;
import dev.snowcave.guilds.core.ChunkStore;
import org.junit.Test;
import xyz.jpenilla.squaremap.api.marker.MultiPolygon;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by WinterBear on 08/11/2023.
 */
public class MapRendererTest {

    @Test
    public void checkMapRenderParts(){

        ChunkStore store = new ChunkStore();
        store.addChunk(new ChunkReference("world", 10, 10));
        store.addChunk(new ChunkReference("world", 11, 10));
        store.addChunk(new ChunkReference("world", 10, 11));
        List<MultiPolygon.MultiPolygonPart> results = MapRenderer.getPolygon(store, "world");

        MultiPolygon.MultiPolygonPart mainGuildArea = results.get(0);
        assertEquals(mainGuildArea.mainPolygon().get(0).x(), 191.0d, 0);
        assertEquals(mainGuildArea.mainPolygon().get(0).z(), 175.0d, 0);

        assertEquals(mainGuildArea.mainPolygon().get(1).x(), 191.0d, 0);
        assertEquals(mainGuildArea.mainPolygon().get(1).z(), 160.0d, 0);

        assertEquals(mainGuildArea.mainPolygon().get(2).x(), 160.0d, 0);
        assertEquals(mainGuildArea.mainPolygon().get(2).z(), 160.0d, 0);

        assertEquals(mainGuildArea.mainPolygon().get(3).x(), 160.0d, 0);
        assertEquals(mainGuildArea.mainPolygon().get(3).z(), 191.0d, 0);

        assertEquals(mainGuildArea.mainPolygon().get(4).x(), 175.0d, 0);
        assertEquals(mainGuildArea.mainPolygon().get(4).z(), 191.0d, 0);

        assertEquals(mainGuildArea.mainPolygon().get(5).x(), 175.0d, 0);
        assertEquals(mainGuildArea.mainPolygon().get(5).z(), 175.0d, 0);


    }


}
