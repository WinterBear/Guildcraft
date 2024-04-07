package dev.snowcave.guilds.commands.claims;

import dev.snowcave.guilds.commands.base.GuildMemberBonusCommandHandler;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.Outpost;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by WinterBear on 11/02/2024.
 */
public class GuildOutpostWarpCommandHandler extends GuildMemberBonusCommandHandler {


    @Override
    public List<String> getKeywords() {
        return List.of("outposttp");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild outposttp <name> &8- &7Teleport to an outpost";
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        Optional<User> user = CommandSenderUtils.getUser(sender);
        if (user.isPresent()){
            return new ArrayList<>(user.get().getGuild().getOutposts().keySet());
        }
        return List.of();
    }

    @Override
    public void handleWithBonus(Player player, User user, String[] arguments) {
        String outpostName = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
        Optional<Location> warp = user.getGuild().getOutposts().entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(outpostName))
                .map(Map.Entry::getValue)
                .map(Outpost::getWarppointLocation)
                .filter(Predicate.not(Objects::isNull))
                .findFirst();
        if (warp.isPresent()){
            player.teleport(warp.get());
        } else {
            Chatter.error(player, "No warp appears to exist for the outpost " + outpostName + ".");
        }
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.OUTPOSTS_1;
    }
}
