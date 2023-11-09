package dev.snowcave.guilds.utils;

import dev.snowcave.guilds.TestGuildCreator;
import dev.snowcave.guilds.core.Guild;
import junit.framework.TestCase;
import org.bukkit.Chunk;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class ChunkUtilsTest extends TestCase {

    @Test
    public void testGetGuild() {
        Chunk chunk = TestGuildCreator.getMockChunk("Test World");

        Guild guild = TestGuildCreator.createGuild("Test Guild", "Test World");
        guild.claimChunk(chunk);

        assertThat(ChunkUtils.getGuild(chunk).get()).isEqualTo(guild);

    }


}