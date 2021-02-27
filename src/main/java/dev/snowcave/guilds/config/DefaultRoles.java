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
            .withPermission(GuildPermission.CLAIM)
            .withPermission(GuildPermission.INVITE)
            .withPermission(GuildPermission.KICK)
            .withPermission(GuildPermission.OPTIONS)
            .withPermission(GuildPermission.ALLIANCE)
            .withPermission(GuildPermission.LEVELUP)
            .withPermission(GuildPermission.WITHDRAW)
            .withPermission(GuildPermission.DEPOSIT)
            .withPermission(GuildPermission.INHERIT)
            .withPermission(GuildPermission.MOVE_SPAWN)
            .withPermission(GuildPermission.GUILDHALL);

    public static final Role MASTER = new Role("Master")
            .withPermission(GuildPermission.CLAIM)
            .withPermission(GuildPermission.INVITE)
            .withPermission(GuildPermission.KICK)
            .withPermission(GuildPermission.LEVELUP)
            .withPermission(GuildPermission.WITHDRAW)
            .withPermission(GuildPermission.DEPOSIT)
            .withPermission(GuildPermission.INHERIT);

    public static final Role ACOLYTE = new Role("Acolyte")
            .withPermission(GuildPermission.INVITE)
            .withPermission(GuildPermission.KICK)
            .withPermission(GuildPermission.DEPOSIT);

    public static final Role MEMBER = new Role("Member")
            .withPermission(GuildPermission.DEPOSIT);


    private static final List<Role> CORE_ROLES = Arrays.asList(
            MEMBER,
            ACOLYTE,
            MASTER,
            GRAND_MASTER
    );

    public static final List<Role> getDefaultRoles(){
        return new ArrayList<>(CORE_ROLES);
    }



}
