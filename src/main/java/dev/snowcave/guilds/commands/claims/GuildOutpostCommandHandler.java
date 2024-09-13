package dev.snowcave.guilds.commands.claims;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionBonusCommandHandler;
import dev.snowcave.guilds.core.ChunkReference;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.ChunkUtils;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 01/08/2023.
 */
public class GuildOutpostCommandHandler extends GuildMemberPermissionBonusCommandHandler {


    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("outpost");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild outpost <name> &8- &7Create an outpost for your Guild away from your existing claims.";
    }

    @Override
    public void handleWithPermissionAndBonus(Player player, User user, String[] arguments) {
        Chatter chatter = new Chatter(player);
        Guild guild = user.getGuild();
        guild.initOutposts();
        if (guild.getOutposts().size() < guild.getMaxOutposts()) {
            Chunk chunk = player.getLocation().getChunk();
            Optional<Guild> chunkOwner = ChunkUtils.getGuild(chunk);
            if (chunkOwner.isPresent()) {
                chatter.error("This chunk is already claimed by " + chunkOwner.get().getGuildName());
            } else if (ChunkUtils.chunkIsAdjacentToGuildArea(guild, chunk)) {
                chatter.error("&7Outposts must not be next to your Guild.");
            } else {
                if (arguments.length < 2) {
                    chatter.error("Please provide a name for your Outpost");
                } else {
                    String outpostName = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
                    ChunkReference reference = guild.createNewOutpost(player.getLocation(), outpostName, player.getLocation().getChunk());
                    chatter.sendP("Claimed outpost &6" + outpostName + " &7at &b" + reference.toString());
                }
            }
        } else {
            chatter.error("Your guild has no remaining outpost slots. Level up your guild to increase outpost slots.");
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.CLAIM;
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.OUTPOSTS_1;
    }
}
