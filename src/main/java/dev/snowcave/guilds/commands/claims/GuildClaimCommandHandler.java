package dev.snowcave.guilds.commands.claims;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.config.Levels;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class GuildClaimCommandHandler extends GuildMemberPermissionCommandHandler {

    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("claim");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild claim &8- &7Claim a chunk for your Guild. Chunks must be adjacent.";
    }

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        Guild guild = user.getGuild();
        Chatter chatter = new Chatter(player);
        if (guild.claimCount() < guild.getLevel().getMaxChunks()) {
            Chunk chunk = player.getLocation().getChunk();
            Optional<Guild> chunkOwner = ChunkUtils.getGuild(chunk);
            if (chunkOwner.isPresent()) {
                chatter.error("This chunk is already claimed by " + chunkOwner.get().getGuildName());
            } else {
                Optional<String> outpost = ChunkUtils.getAdjacentOutpost(guild, chunk);
                if (outpost.isPresent()) {
                    ChunkReference reference = guild.claimOutpostChunk(outpost.get(), player.getLocation().getChunk());
                    chatter.sendP("Claimed outpost " + outpost + " chunk " + reference.toString());
                } else if (!ChunkUtils.chunkIsAdjacentToGuildArea(guild, chunk)) {
                    if (Levels.getAllGuildBonuses(guild.getLevel()).contains(GuildBonus.OUTPOSTS_1)) {
                        chatter.sendP("Use /g outpost <name> to create an outpost, or claim a chunk next to your existing claims.");
                    } else {
                        chatter.error("Chunks must be connected to your Guild. Reach guild level 8 to claim outposts.");
                    }

                } else {
                    ChunkReference reference = guild.claimChunk(player.getLocation().getChunk());
                    chatter.sendP("Claimed chunk &b" + reference.toString());
                }
            }

        } else {
            chatter.send("&7Your guild has no remaining chunk slots. Level up your guild to increase maximum slots.");
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.CLAIM;
    }
}
