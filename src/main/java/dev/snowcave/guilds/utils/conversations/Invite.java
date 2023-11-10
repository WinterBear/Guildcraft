package dev.snowcave.guilds.utils.conversations;

import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;

import java.util.UUID;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class Invite {

    private final Guild guild;

    private final UUID user;

    public Invite(Guild guild, UUID user) {
        this.guild = guild;
        this.user = user;
    }

    public Guild getGuild() {
        return guild;
    }

    public UUID getUser() {
        return user;
    }
}
