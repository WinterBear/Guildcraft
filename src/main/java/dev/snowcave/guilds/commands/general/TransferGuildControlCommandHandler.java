package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.commands.base.GuildLeaderCommandHandler;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 21/01/2021.
 */
public class TransferGuildControlCommandHandler extends GuildLeaderCommandHandler {


    @Override
    public void handle(Player player, User user, String[] arguments) {
        Chatter chatter = new Chatter(player);
        if (arguments.length > 1) {
            String transferTo = arguments[1];
            Optional<User> member = user.getGuild().getMembers().stream().filter(u -> u.getName().equals(transferTo)).findAny();
            if (member.isPresent()) {
                user.getGuild().setLeader(member.get());
                user.getGuild().broadcast("&3Guild control has been transferred to &6" + member.get().getName());
            } else {
                chatter.error("You may only transfer control to a member of your Guild.");
            }
        } else {
            chatter.send(describe());
        }

    }

    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("transfer");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild transfer &e<&6Member&e> &8- &7Transfer Guild leadership";
    }
}
