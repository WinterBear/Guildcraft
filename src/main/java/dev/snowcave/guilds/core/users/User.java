package dev.snowcave.guilds.core.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by WinterBear on 28/09/2020.
 */
public class User {

    private String name;

    private UUID uuid;

    private String roleReference;

    @JsonIgnore
    private Guild guild;

    public User(){

    }

    public User(String name, UUID uuid, Role role) {
        this.name = name;
        this.uuid = uuid;
        this.roleReference = role.getTitle();
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }


    public String getRoleReference() {
        return roleReference;
    }

    @JsonIgnore
    public Optional<Role> getRole() {
        if(guild == null){
            return Optional.empty();
        }
        return guild.getRole(roleReference);
    }

    public void setGuild(Guild guild){
        this.guild = guild;
    }

    public Guild getGuild(){
        return guild;
    }

    public boolean hasPermission(GuildPermission permission){
        if(guild.getLeader().getUuid().equals(this.getUuid())){
            return true;
        }
        if(getRole().isPresent()){
            return getRole().get().getPermissions().contains(permission);
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setRoleReference(String roleReference) {
        this.roleReference = roleReference;
    }

    @JsonIgnore
    public void setRole(Role role) {
        this.roleReference = role.getTitle();
    }
}
