package dev.snowcave.guilds.commands.claims;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionBonusCommandHandler;
import dev.snowcave.guilds.core.ChunkReference;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.Outpost;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by WinterBear on 11/02/2024.
 */
public class GuildOutpostSetWarpCommandHandler extends GuildMemberPermissionBonusCommandHandler {
    @Override
    public List<String> getKeywords() {
        return List.of("setoutposttp");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild setoutposttp <outpost> &8- &7Moves your Guild Spawn to your current location.";
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return super.getTabCompletions(sender, args);
    }

    @Override
    public void handleWithPermissionAndBonus(Player player, User user, String[] arguments) {
        String outpostName = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
        Optional<Outpost> outpost = user.getGuild().getOutposts().entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(outpostName))
                .map(Map.Entry::getValue)
                .findFirst();
        if (outpost.isPresent()){
            if(outpost.get().has(new ChunkReference(player.getLocation().getChunk()))){
                outpost.get().setWarppointLocation(player.getLocation());
                Chatter.sendP(player, "Warp for outpost " + outpostName + " is set to your location.");
            } else {
                Chatter.error(player, "This chunk is not inside the outpost.");
            }
        } else {
            Chatter.error(player, "No outpost could be found named " + outpostName + ".");
        }
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.OUTPOSTS_1;
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.CLAIM;
    }
}
