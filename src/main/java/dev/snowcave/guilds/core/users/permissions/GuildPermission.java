package dev.snowcave.guilds.core.users.permissions;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by WinterBear on 19/12/2020.
 */
public enum GuildPermission {

    CLAIM("Claim", "Allows the user to claim and unclaim guild territory."),
    INVITE("Invite", "Allows the user to invite new members to the guild."),
    KICK("Kick", "Allows the user to kick members from the guild."),
    OPTIONS("Options", "Allows the user to modify guild settings."),
    ALLIANCE("Alliance", "Allows the user to manage the guilds Alliance."),
    LEVELUP("Level", "Allows the user to level up the guild."),
    DEPOSIT("Deposit", "Allows the user to deposit money in the guild bank."),
    WITHDRAW("Withdraw", "Allows the user to withdraw money from the guild bank."),
    ROLES("Roles", "Allows the user to manage guild roles."),
    INHERIT("Inherit", "Users with this permission will be considered first if the guild leader leaves without nominating someone."),
    MOVE_SPAWN("Move Spawn", "Allows the user to move the guild spawn."),
    GUILDHALL("Guild Hall", "Allows the user to manage the guild hall.");

    private final String displayName;

    private final String description;

    GuildPermission(String name, String description) {
        this.displayName = name;
        this.description = description;
    }

    public static Optional<GuildPermission> get(String name) {
        return Arrays.stream(GuildPermission.values())
                .filter(g -> g.getDisplayName().equalsIgnoreCase(name))
                .findFirst();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
