package dev.snowcave.guilds.commands.claims;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionBonusCommandHandler;
import dev.snowcave.guilds.core.ChunkReference;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.ChunkUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 09/11/2023.
 */
public class GuildWardChunkCommandHandler extends GuildMemberPermissionBonusCommandHandler {

    @Override
    public void handleWithPermissionAndBonus(Player player, User user, String[] arguments) {
        Guild guild = user.getGuild();
        Chunk chunk = player.getLocation().getChunk();
        Optional<Guild> chunkOwner = ChunkUtils.getGuild(chunk);
        if(!chunkOwner.isPresent() || !chunkOwner.get().equals(guild)){
            ChatUtils.send(player, ChatUtils.format("&7This chunk is not part of your guild."));
        } else {
            Optional<ChunkReference> chunkReference = guild.getChunkRef(chunk);
            if(chunkReference.isPresent()) {
                if(arguments.length > 1 && arguments[1].equalsIgnoreCase("on")){
                    chunkReference.get().initOrGetChunkMetadata().setWardMobs(true);
                    String message = "&7This chunks mob ward was set to: " + chunkReference.get().initOrGetChunkMetadata().isWardMobs();
                    ChatUtils.send(player, ChatUtils.format(message));
                } else if (arguments.length > 1 && arguments[1].equalsIgnoreCase("off")) {
                    chunkReference.get().initOrGetChunkMetadata().setWardMobs(false);
                    String message = "&7This chunks mob ward was set to: " + chunkReference.get().initOrGetChunkMetadata().isWardMobs();
                    ChatUtils.send(player, ChatUtils.format(message));
                } else {
                    String message = "&7This chunks mob ward is set to: " + chunkReference.get().initOrGetChunkMetadata().isWardMobs();
                    ChatUtils.send(player, ChatUtils.format(message));
                }

            } else {
                ChatUtils.send(player, ChatUtils.format("&cChunk error. Chunk is part of guild but not referencable. Speak to an administrator."));
            }
        }
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.MONSTER_WARD;
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.CLAIM;
    }

    @Override
    public List<String> getKeywords() {
        return List.of("ward");
    }

    @Override
    public String describe() {
        return "&b/guild ward <on/off> &8- &7Turns the Monster Ward on or off within the current chunk.";
    }
}
