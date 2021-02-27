package dev.snowcave.guilds.commands.claims;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.ChunkReference;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.ChunkUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

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
    public String describe() {
        return "&b/guild claim &8- &7Claim a chunk for your Guild. Chunks must be adjacent.";
    }

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        Guild guild = user.getGuild();
        if(guild.getChunks().getChunkCount() < guild.getLevel().getMaxChunks()){
            Chunk chunk = player.getLocation().getChunk();
            Optional<Guild> chunkOwner = ChunkUtils.getGuild(chunk);
            if(chunkOwner.isPresent()){
                ChatUtils.send(player, ChatUtils.format("&7This chunk is already claimed by &6" + chunkOwner.get().getGuildName()));
            } else if (!ChunkUtils.chunkIsAdjacentToGuildArea(guild, chunk)){
                ChatUtils.send(player, ChatUtils.format("&7Chunk is not adjacent to your Guild."));
            } else {
                ChunkReference reference = guild.claimChunk(player.getLocation().getChunk());
                ChatUtils.send(player, ChatUtils.format("&7Claimed chunk &b" + reference.toString()));
            }
        } else {
            handleNotEnoughChunks(player);
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.CLAIM;
    }


    private void handleNotEnoughChunks(Player player){
        ChatUtils.send(player, ChatUtils.format("&7Your guild has no remaining chunk slots. Level up your guild to increase maximum slots."));
    }
}
