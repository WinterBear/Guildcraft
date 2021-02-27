package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.commands.base.GuildLeaderCommandHandler;
import dev.snowcave.guilds.commands.base.GuildMemberPermissionBonusCommandHandler;
import dev.snowcave.guilds.core.users.User;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 21/01/2021.
 */
public class TransferGuildControlCommandHandler extends GuildLeaderCommandHandler {


    @Override
    public void handle(Player player, User user, String[] arguments) {
        if(arguments.length > 1){
            String transferTo = arguments[1];
            Optional<User> member = user.getGuild().getMembers().stream().filter(u -> u.getName().equals(transferTo)).findAny();
            if(member.isPresent()){
                user.getGuild().setLeader(member.get());
                user.getGuild().broadcast("&3Guild control has been transferred to &6" +  member.get().getName());
            } else {
                ChatUtils.send(player, "&7You may only transfer control to a member of your Guild.");
            }
        } else {
            ChatUtils.send(player, describe());
        }

    }

    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("transfer");
    }

    @Override
    public String describe() {
        return "&b/guild transfer &e<&6member&e> &8- &7Transfer Guild leadership";
    }
}
