package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.Role;
import dev.snowcave.guilds.core.users.RoleComparator;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildRolesCommandHandler extends GuildMemberPermissionCommandHandler {

    //Tab completions
    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] arguments) {
        if (arguments.length == 2) {
            return Arrays.asList("list", "create", "rename", "copy", "remove", "addperm", "removeperm", "permissions", "perms", "assign");
            //TODO do this better
        }
        return Collections.emptyList();


    }

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        if (arguments.length == 1) {
            ChatUtils.send(player, "&b/g roles list &8- &7List guild roles\n"
                    + "&b/g roles create &e<&6Role Name&e> &8- &7Create a new role\n"
                    + "&b/g roles rename &e<&6Role&e> &7- &e<&6New Name&e> &8- &7Rename a role\n"
                    + "&b/g roles copy &e<&6From Role&e> &7- &e<&6To Role&e> &8- &7Copy permissions from one role to another\n"
                    + "&b/g roles remove &e<&6Role Name&e> &8- &7Remove a role\n"
                    + "&b/g roles addperm &e<&6Role Name&e> &7- &e<&6Permission&e> &8- &7Add a permission to a role\n"
                    + "&b/g roles removeperm &e<&6Role Name&e> &7- &e<&6Permission&e> &8- &7Remove a permission from a role\n"
                    + "&b/g roles permissions &8- &7List permissions\n"
                    + "&b/g roles assign &e<&6Player&e> &e<&6Role Name&e> &8- &7Assign a role to a member of your Guild");
            return;
        }
        if (arguments[1].equalsIgnoreCase("list")) {
            listRoles(player, user);
        } else if (arguments[1].equalsIgnoreCase("create")) {
            if (arguments.length > 2) {
                createRole(player, user, String.join(" ", Arrays.copyOfRange(arguments, 2, arguments.length)));
            } else {
                ChatUtils.send(player, "&7Usage&8: &b/g roles create &e<&6Role Name&e>");
            }
        } else if (arguments[1].equalsIgnoreCase("rename")) {
            if (arguments.length > 4) {
                int splitIndex = Arrays.asList(arguments).indexOf("-");
                if (splitIndex == -1) {
                    ChatUtils.send(player, "&7Usage&8: &b/g roles rename &e<&6Role&e> &c&o- &e<&6New Name&e>");
                    return;
                }
                String oldRole = String.join(" ", Arrays.copyOfRange(arguments, 2, splitIndex));
                String newRole = String.join(" ", Arrays.copyOfRange(arguments, splitIndex + 1, arguments.length));
                renameRole(player, user, oldRole, newRole);
            } else {
                ChatUtils.send(player, "&7Usage&8: &b/g roles rename &e<&6Role&e> &7- &e<&6New Name&e>");
            }
        } else if (arguments[1].equalsIgnoreCase("copy")) {
            if (arguments.length > 4) {
                int splitIndex = Arrays.asList(arguments).indexOf("-");
                if (splitIndex == -1) {
                    ChatUtils.send(player, "&7Usage&8: &b/g roles copy &e<&6From Role&e> &c&o- &e<&6To Role&e>");
                    return;
                }
                String fromRole = String.join(" ", Arrays.copyOfRange(arguments, 2, splitIndex));
                String toRole = String.join(" ", Arrays.copyOfRange(arguments, splitIndex + 1, arguments.length));
                copyPermissions(player, user, fromRole, toRole);
            } else {
                ChatUtils.send(player, "&7Usage&8: &b/g roles copy &e<&6From Role&e> &7- &e<&6To Role&e>");
            }
        } else if (arguments[1].equalsIgnoreCase("remove")) {
            if (arguments.length > 2) {
                deleteRole(player, user, String.join(" ", Arrays.copyOfRange(arguments, 2, arguments.length)));
            } else {
                ChatUtils.send(player, "&7Usage&8: &b/g roles remove &e<&6Role Name&e>");
            }
        } else if (arguments[1].equalsIgnoreCase("addperm")) {
            if (arguments.length > 4) {
                int splitIndex = Arrays.asList(arguments).indexOf("-");
                if (splitIndex == -1) {
                    ChatUtils.send(player, "&7Usage&8: &b/g roles addperm &e<&6Role Name&e> &c&o- &e<&6Permission&e>");
                    return;
                }
                String role = String.join(" ", Arrays.copyOfRange(arguments, 2, splitIndex));
                String perm = String.join(" ", Arrays.copyOfRange(arguments, splitIndex + 1, arguments.length));
                addRolePermission(player, user, role, perm);
            } else {
                ChatUtils.send(player, "&7Usage&8: &b/g roles addperm &e<&6Role Name&e> &7- &e<&6Permission&e>");
            }
        } else if (arguments[1].equalsIgnoreCase("removeperm")) {
            if (arguments.length > 4) {
                int splitIndex = Arrays.asList(arguments).indexOf("-");
                if (splitIndex == -1) {
                    ChatUtils.send(player, "&7Usage&8: &b/g roles removeperm &e<&6Role Name&e> &c&o- &e<&6Permission&e>");
                    return;
                }
                String role = String.join(" ", Arrays.copyOfRange(arguments, 2, splitIndex));
                String perm = String.join(" ", Arrays.copyOfRange(arguments, splitIndex + 1, arguments.length));
                removeRolePermission(player, user, role, perm);
            } else {
                ChatUtils.send(player, "&7Usage&8: &b/g roles removeperm &e<&6Role Name&e> &7- &e<&6Permission&e>");
            }
        } else if (arguments[1].equalsIgnoreCase("assign")) {
            if (arguments.length > 3) {
                changeRole(player, user, arguments[2], String.join(" ", Arrays.copyOfRange(arguments, 3, arguments.length)));
            } else {
                ChatUtils.send(player, "&7Usage&8: &b/g roles assign &e<&6Player&e> &e<&6Role Name&e>");
            }
        } else if (arguments[1].equalsIgnoreCase("permissions") || arguments[1].equalsIgnoreCase("perms")) {
            ChatUtils.send(player, "&7Available Permissions: ");
            Arrays.stream(GuildPermission.values()).forEach(p -> ChatUtils.send(player, "&6" + p.getDisplayName() + "&b: &3" + p.getDescription()));
        }

    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.ROLES;
    }

    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("roles");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild roles &8- &7Edit guild roles";
    }

    //View Roles
    public void listRoles(Player player, User user) {
        List<Role> orderedRoles = user.getGuild()
                .getRoles().stream()
                .sorted(new RoleComparator().reversed())
                .collect(Collectors.toList());

        for (Role role : orderedRoles) {
            List<String> users = user.getGuild().getMembers().stream()
                    .filter(u -> u.getRole() != null)
                    .filter(u -> u.getRole().get().equals(role))
                    .map(User::getName)
                    .collect(Collectors.toList());
            List<String> permissions = role.getPermissions().stream()
                    .map(GuildPermission::getDisplayName)
                    .collect(Collectors.toList());
            ChatUtils.send(player, "&3&l" + role.getTitle());
            ChatUtils.send(player, "&ePermissions&8: &6" + String.join(", ", permissions));
            ChatUtils.send(player, "&eMembers&8: &b" + String.join(", ", users));
        }

    }

    //Create new Role

    public void createRole(Player player, User user, String newRoleTitle) {
        Chatter chatter = new Chatter(player);
        if (user.getGuild().getRole(newRoleTitle).isPresent()) {
            chatter.error("There is already a role in your guild with that name.");
            return;
        }
        Role newRole = new Role();
        newRole.setTitle(newRoleTitle);
        user.getGuild().getRoles().add(newRole);
        chatter.sendP("Created new role " + newRoleTitle + " successfully.");
    }

    //Copy permissions from one role to another

    public void copyPermissions(Player player, User user, String fromRoleReference, String toRoleReference) {
        Chatter chatter = new Chatter(player);
        Optional<Role> fromRole = user.getGuild().getRole(fromRoleReference);
        Optional<Role> toRole = user.getGuild().getRole(toRoleReference);
        if (fromRole.isEmpty()) {
            chatter.error("Source role " + fromRoleReference + " does not appear to exist.");
            return;
        }
        if (toRole.isEmpty()) {
            chatter.error("Destination role " + toRoleReference + " does not appear to exist.");
            return;
        }
        fromRole.get().getPermissions().stream()
                .filter(p -> p != GuildPermission.LEADER).forEach(p -> toRole.get().getPermissions().add(p));
        chatter.sendP("Copied all permissions from &b" + fromRole.get().getTitle() + " &7to &e" + toRole.get().getTitle());
    }

    //Delete Role

    public void deleteRole(Player player, User user, String role) {
        Chatter chatter = new Chatter(player);
        Optional<Role> deleteRole = user.getGuild().getRole(role);
        if (deleteRole.isPresent()) {
            if (getLowestRank(user.getGuild()).equals(deleteRole.get())) {
                chatter.error("The default rank cannot be deleted, only renamed/edited.");
                return;
            }
            if (deleteRole.get().getPermissions().contains(GuildPermission.LEADER)) {
                chatter.error("The Guild Leader rank cannot be deleted or edited, only renamed.");
                return;
            }
            user.getGuild().getMembers()
                    .stream().filter(u -> u.getRole().equals(deleteRole))
                    .forEach(this::deleteUserRole);
            user.getGuild().getRoles().remove(deleteRole.get());
            chatter.sendP("Deleted role &c" + deleteRole.get().getTitle() + " &7and moved any members to &e" + getLowestRank(user.getGuild()).getTitle());
        }
    }

    private void deleteUserRole(User member) {
        member.setRole(getLowestRank(member.getGuild()));
    }

    private Role getLowestRank(Guild guild) {
        return guild.getRoles()
                .stream()
                .sorted(new RoleComparator())
                .toList()
                .get(0);
    }

    //Add Role Permission

    public void addRolePermission(Player player, User user, String roleName, String permission) {
        Chatter chatter = new Chatter(player);
        Optional<GuildPermission> optionalPerm = GuildPermission.get(permission);
        Optional<Role> role = user.getGuild().getRole(roleName);
        if (optionalPerm.isPresent()) {
            if (role.isPresent()) {
                if (role.get().getPermissions().contains(optionalPerm.get())) {
                    chatter.error("That role already has the permission " + optionalPerm.get().getDisplayName());
                    return;
                }
                role.get().addPermission(optionalPerm.get());
                chatter.sendP("Added the permission " + optionalPerm.get().getDisplayName() + " to the role " + role.get().getTitle());
            } else {
                chatter.error("The role " + roleName + " does not appear to exist.");
                chatter.send("&7Available roles:");
                user.getGuild().getRoles().forEach(r -> chatter.send( "&6" + r.getTitle()));
            }
        } else {
            chatter.error("The permission " + permission + " does not exist.");
            chatter.send("&7Available Permissions: ");
            Arrays.stream(GuildPermission.values()).forEach(p -> ChatUtils.send(player, "&6" + p.getDisplayName() + "&b: &3" + p.getDescription()));
        }
    }

    //Remove Role Permission
    public void removeRolePermission(Player player, User user, String roleName, String permission) {
        Chatter chatter = new Chatter(player);
        Optional<GuildPermission> optionalPerm = GuildPermission.get(permission);
        Optional<Role> role = user.getGuild().getRole(roleName);
        if (optionalPerm.isPresent()) {
            if (role.isPresent()) {
                if (role.get().getPermissions().contains(GuildPermission.LEADER)) {
                    chatter.error("The Guild Leader rank's permissions cannot be modified, only renamed.");
                    return;
                }
                if (!role.get().getPermissions().contains(optionalPerm.get())) {
                    chatter.error("That role does not have the permission " + optionalPerm.get().getDisplayName());
                    return;
                }
                role.get().removePermission(optionalPerm.get());
                chatter.sendP("Removed the permission " + optionalPerm.get().getDisplayName() + " from the role " + role.get().getTitle());
            } else {
                chatter.error("The role " + roleName + " does not appear to exist.");
                chatter.send("&7Available roles&8:");
                user.getGuild().getRoles().forEach(r -> chatter.send("&3" + r.getTitle()));
            }
        } else {
            chatter.error("The permission " + permission + " does not exist.");
            chatter.send("&7Available Permissions&8: ");
            Arrays.stream(GuildPermission.values()).forEach(p -> chatter.send("&6" + p.getDisplayName() + "&b: &3" + p.getDescription()));
        }
    }

    //Change User Role

    public void changeRole(Player player, User user, String targetPlayerName, String roleName) {
        Chatter chatter = new Chatter(player);
        Optional<Role> role = user.getGuild().getRole(roleName);
        Optional<User> target = user.getGuild().getMembers().stream().filter(u -> u.getName().equalsIgnoreCase(targetPlayerName)).findAny();
        if (target.isPresent()) {
            if (role.isPresent()) {
                if (role.get().getPermissions().contains(GuildPermission.LEADER)) {
                    chatter.error("The Guild Leader's rank cannot be modified, only renamed.");
                    return;
                }
                target.get().setRole(role.get());
                chatter.sendP( "&7Player &b" + target.get().getName() + "&7 was granted the role of &6" + role.get().getTitle());
                Player targetPlayer = Bukkit.getPlayer(user.getUuid());
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    Chatter.sendP(targetPlayer, "You were granted the role of &b" + role.get().getTitle() + " &7by &6" + player.getDisplayName());
                }
            } else {
                chatter.error("The role " + roleName + " does not appear to exist.");
                chatter.send("&7Available roles&8:");
                user.getGuild().getRoles().forEach(r -> ChatUtils.send(player, "&3" + r.getTitle()));
            }

        } else {
            chatter.error("The player " + targetPlayerName + " does not appear to be a member of your Guild.");
            chatter.send("&7Members&8:");
            user.getGuild().getMembers().forEach(m -> chatter.send("&3" + m.getName()));
        }
    }

    //Rename Role
    public void renameRole(Player player, User user, String roleName, String newName) {
        Chatter chatter = new Chatter(player);
        Optional<Role> role = user.getGuild().getRole(roleName);
        if (role.isPresent()) {
            String oldName = role.get().getTitle();
            role.get().setTitle(newName);
            user.getGuild().getMembers().stream()
                    .filter(m -> m.getRoleReference().equalsIgnoreCase(oldName))
                    .forEach(m -> m.setRoleReference(newName));
            chatter.sendP("Renamed the role &6" + oldName + " &7to &e" + newName);
        } else {
            chatter.error("The role &b" + roleName + " &7does not appear to exist.");
            chatter.send("&7Available roles&8:");
            user.getGuild().getRoles().forEach(r -> chatter.send("&6" + r.getTitle()));
        }
    }


}
