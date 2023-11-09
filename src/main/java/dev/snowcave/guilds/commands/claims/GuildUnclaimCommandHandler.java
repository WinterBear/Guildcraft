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
 * Created by WinterBear on 28/12/2020.
 */
public class GuildUnclaimCommandHandler extends GuildMemberPermissionCommandHandler {

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        Guild guild = user.getGuild();
        Chunk chunk = player.getLocation().getChunk();
        Optional<Guild> chunkOwner = ChunkUtils.getGuild(chunk);
        if (chunkOwner.isPresent()) {
            if (guild.allChunks().size() < 2) {
                ChatUtils.send(player, ChatUtils.format("&7You cannot unclaim the last chunk in a guild."));
            } else if (chunkOwner.get().equals(guild)) {
                guild.unclaimChunk(chunk);
                ChatUtils.send(player, ChatUtils.format("&7Unclaimed chunk &b" + ChunkReference.toString(chunk)));
            } else {
                ChatUtils.send(player, ChatUtils.format("&7Chunk is not owned by your guild."));
            }
        } else {
            ChatUtils.send(player, ChatUtils.format("&7Chunk is not part of a guilds territory."));
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.CLAIM;
    }

    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("unclaim");
    }

    @Override
    public String describe() {
        return "&b/guild unclaim &8- &7Unclaim a chunk from your Guilds territory.";
    }
}
