package dev.snowcave.guilds.utils;

import dev.snowcave.guilds.TestGuildCreator;
import dev.snowcave.guilds.core.Guild;
import org.bukkit.Chunk;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class ChunkUtilsTest {

    @Test
    public void testGetGuild() {
        Guild guild = TestGuildCreator.createGuild("Test Guild", "Test World");
        Chunk chunk = TestGuildCreator.getMockChunk("Test World");

        guild.claimChunk(chunk);

        assertThat(ChunkUtils.getGuild(chunk).get()).isEqualTo(guild);

    }


}