package dev.snowcave.guilds.commands.claims;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionBonusCommandHandler;
import dev.snowcave.guilds.core.ChunkReference;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 09/11/2023.
 */
public class GuildWardChunkCommandHandler extends GuildMemberPermissionBonusCommandHandler {

    @Override
    public void handleWithPermissionAndBonus(Player player, User user, String[] arguments) {
        Chatter chatter = new Chatter(player);
        Guild guild = user.getGuild();
        Chunk chunk = player.getLocation().getChunk();
        Optional<Guild> chunkOwner = ChunkUtils.getGuild(chunk);
        if(!chunkOwner.isPresent() || !chunkOwner.get().equals(guild)){
            chatter.error("This chunk is not part of your guild.");
        } else {
            Optional<ChunkReference> chunkReference = guild.getChunkRef(chunk);
            if(chunkReference.isPresent()) {
                String message;
                if(arguments.length > 1 && arguments[1].equalsIgnoreCase("on")){
                    chunkReference.get().initOrGetChunkMetadata().setWardMobs(true);
                    message = "This chunks mob ward was set to: " + chunkReference.get().initOrGetChunkMetadata().isWardMobs();
                } else if (arguments.length > 1 && arguments[1].equalsIgnoreCase("off")) {
                    chunkReference.get().initOrGetChunkMetadata().setWardMobs(false);
                    message = "This chunks mob ward was set to: " + chunkReference.get().initOrGetChunkMetadata().isWardMobs();
                } else {
                    message = "This chunks mob ward is set to: " + chunkReference.get().initOrGetChunkMetadata().isWardMobs();
                }
                chatter.sendP(message);
            } else {
                Bukkit.getLogger().warning("Chunk error. Chunk " + player.getLocation().getChunk() + " is part of guild " + user.getGuild().getGuildName() + " but not referencable.");
                chatter.error("&cChunk error. Chunk is part of guild but not referencable. Speak to an administrator.");
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
    public @NotNull String describe() {
        return "&b/guild ward <on/off> &8- &7Turns the Monster Ward on or off within the current chunk.";
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if(args.length < 3) {
            return List.of("on", "off");
        }
        return List.of();
    }

}
