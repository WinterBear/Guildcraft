package dev.snowcave.guilds.config;

import dev.snowcave.guilds.core.users.Role;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by WinterBear on 19/12/2020.
 */
public class DefaultRoles {

    public static final Role GRAND_MASTER = new Role("Grandmaster")
            .addPermission(GuildPermission.CLAIM)
            .addPermission(GuildPermission.INVITE)
            .addPermission(GuildPermission.KICK)
            .addPermission(GuildPermission.OPTIONS)
            .addPermission(GuildPermission.ALLIANCE)
            .addPermission(GuildPermission.LEVELUP)
            .addPermission(GuildPermission.WITHDRAW)
            .addPermission(GuildPermission.DEPOSIT)
            .addPermission(GuildPermission.INHERIT)
            .addPermission(GuildPermission.MOVE_SPAWN)
            .addPermission(GuildPermission.GUILDHALL);

    public static final Role MASTER = new Role("Master")
            .addPermission(GuildPermission.CLAIM)
            .addPermission(GuildPermission.INVITE)
            .addPermission(GuildPermission.KICK)
            .addPermission(GuildPermission.LEVELUP)
            .addPermission(GuildPermission.WITHDRAW)
            .addPermission(GuildPermission.DEPOSIT)
            .addPermission(GuildPermission.INHERIT);

    public static final Role ACOLYTE = new Role("Acolyte")
            .addPermission(GuildPermission.INVITE)
            .addPermission(GuildPermission.KICK)
            .addPermission(GuildPermission.DEPOSIT);

    public static final Role MEMBER = new Role("Member")
            .addPermission(GuildPermission.DEPOSIT);


    private static final List<Role> CORE_ROLES = Arrays.asList(
            MEMBER,
            ACOLYTE,
            MASTER,
            GRAND_MASTER
    );

    public static List<Role> getDefaultRoles() {
        return new ArrayList<>(CORE_ROLES);
    }


}
