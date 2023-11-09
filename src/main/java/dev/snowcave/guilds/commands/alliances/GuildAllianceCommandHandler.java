package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class GuildAllianceCommandHandler extends GuildMemberPermissionCommandHandler {

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {

    }

    @Override
    public GuildPermission getPermission() {
        return null;
    }

    @Override
    public List<String> getKeywords() {
        return null;
    }

    @Override
    public String describe() {
        return null;
    }

    //Create Alliance
    private void handleCreation() {

    }

    //Invite Guild to Alliance

    //List Alliances

    //Kick Guild from Alliance

    //Disband Alliance

    //Deposit Alliance Money

    //Withdraw Alliance Money

    //Alliance Upgrades

}
