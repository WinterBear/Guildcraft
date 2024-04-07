package dev.snowcave.guilds.core.users;

import dev.snowcave.guilds.core.users.permissions.GuildPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WinterBear on 19/12/2020.
 */
public class Role {

    private String title;

    private List<GuildPermission> permissions = new ArrayList<>();

    public Role() {

    }


    public Role(String title) {
        this.title = title;
        this.permissions = new ArrayList<>();
    }

    public Role addPermission(GuildPermission permission) {
        this.permissions.add(permission);
        return this;
    }

    public List<GuildPermission> getPermissions() {
        return permissions;
    }

    public void removePermission(GuildPermission permission) {
        this.permissions.remove(permission);
    }

    public void setPermissions(List<GuildPermission> permissions) {
        this.permissions = permissions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
