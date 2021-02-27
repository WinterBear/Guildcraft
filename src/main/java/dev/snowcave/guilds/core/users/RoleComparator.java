package dev.snowcave.guilds.core.users;

import java.util.Comparator;

/**
 * Created by WinterBear on 16/01/2021.
 */
public class RoleComparator implements Comparator<Role> {


    @Override
    public int compare(Role o1, Role o2) {
        return o1.getPermissions().size() - o2.getPermissions().size();
    }
}
