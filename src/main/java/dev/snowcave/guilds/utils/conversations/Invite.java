package dev.snowcave.guilds.utils.conversations;

import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class Invite {

    private Guild guild;

    private User user;

    public Invite(Guild guild, User user) {
        this.guild = guild;
        this.user = user;
    }

    public Guild getGuild() {
        return guild;
    }

    public User getUser() {
        return user;
    }
}
