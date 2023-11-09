package dev.snowcave.guilds.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.snowcave.guilds.TestGuildCreator;
import junit.framework.TestCase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by WinterBear on 05/01/2021.
 */
public class GuildToJSONTest extends TestCase {

    @Test
    public void test() throws JsonProcessingException {

        Guild guild = TestGuildCreator.createGuild("Test Guild", "Test World");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(guild);
        System.out.println(json);

        Guild toGuild = new ObjectMapper().readValue(json, Guild.class);

        assertThat(toGuild.getGuildName().equalsIgnoreCase("Test Guild"));
        assertThat(toGuild.getBalance().equals(0.0));
    }

}