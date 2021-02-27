package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.Role;
import dev.snowcave.guilds.core.users.RoleComparator;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildRolesCommandHandler extends GuildMemberPermissionCommandHandler {

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        if(arguments[1].equalsIgnoreCase("list")){
            listRoles(player, user);
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.ROLES;
    }

    @Override
    public List<String> getKeywords() {
        return null;
    }

    @Override
    public String describe() {
        return "/guild roles ";
    }

    //TODO

    //View Roles
    public void listRoles(Player player, User user){
        List<Role> orderedRoles = user.getGuild()
                .getRoles().stream()
                .sorted(new RoleComparator().reversed())
                .collect(Collectors.toList());

        for(Role role : orderedRoles){
            List<String> users = user.getGuild().getMembers().stream()
                    .filter(u -> u.getRole().equals(role))
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

    public void createRole(Player player, User user, String newRoleTitle){
        if(user.getGuild().getRole(newRoleTitle).isPresent()){
            ChatUtils.send(player, "&7There is already a role in your guild with that name.");
            return;
        }
        Role newRole = new Role();
        newRole.setTitle(newRoleTitle);
        user.getGuild().getRoles().add(newRole);
        ChatUtils.send(player, "&7Created new role " + newRoleTitle  + " successfully.");
    }

    //Copy permissions from one role to another

    public void copyPermissions(Player player, User user, String fromRoleReference, String toRoleReference){
        Optional<Role> fromRole = user.getGuild().getRole(fromRoleReference);
        Optional<Role> toRole = user.getGuild().getRole(toRoleReference);
        if(!fromRole.isPresent()){
            ChatUtils.send(player, "&7Error. Source role " + fromRoleReference + " does not appear to exist.");
            return;
        }
        if(!toRole.isPresent()){
            ChatUtils.send(player, "&7Error. Destination role " + toRoleReference + " does not appear to exist.");
            return;
        }
        fromRole.get().getPermissions().forEach(p -> toRole.get().getPermissions().add(p));
        ChatUtils.send(player, "&7Copied all permissions from &b" + fromRole.get().getTitle() + " &7to &e" + toRole.get().getTitle());
    }

    //Delete Role

    public void deleteRole(Player player, User user, String role){
        Optional<Role> deleteRole = user.getGuild().getRole(role);
        if(deleteRole.isPresent()){
            if(getLowestRank(user.getGuild()).equals(deleteRole.get())){
                ChatUtils.send(player, "&cError. The default rank cannot be deleted, only renamed/edited.");
                return;
            }
            user.getGuild().getMembers()
                    .stream().filter(u -> u.getRole().equals(deleteRole))
                    .forEach(this::deleteUserRole);
            user.getGuild().getRoles().remove(deleteRole.get());
            ChatUtils.send(player, "&7Deleted role &c" + deleteRole.get().getTitle() + " &7and moved any members to &e" + getLowestRank(user.getGuild()).getTitle());
        }
    }

    private void deleteUserRole(User member){
        member.setRole(getLowestRank(member.getGuild()));
    }

    private Role getLowestRank(Guild guild){
        return guild.getRoles()
                .stream()
                .sorted(new RoleComparator())
                .collect(Collectors.toList())
                .get(0);
    }

    //Add Role Permission

    public void addRolePermission(Player player, User user, String role, String permission){

    }

    //Remove Role Permission
    public void removeRolePermission(Player player, User user, String role, String permission){

    }

    //Change User Role

    public void changeRole(Player player, User user, Player targetPlayer, String role){

    }

    //Rename Role
    public void renameRole(Player player, User user, String role, String newName){

    }



}
