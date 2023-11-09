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
    public String describe() {
        return "&b/guild outpost <name> &8- &7Create an outpost for your Guild away from your existing claims.";
    }

    @Override
    public void handleWithPermissionAndBonus(Player player, User user, String[] arguments) {

        Guild guild = user.getGuild();
        guild.initOutposts();
        if (guild.getOutposts().size() < guild.getMaxOutposts()) {
            Chunk chunk = player.getLocation().getChunk();
            Optional<Guild> chunkOwner = ChunkUtils.getGuild(chunk);
            if (chunkOwner.isPresent()) {
                ChatUtils.send(player, ChatUtils.format("&7This chunk is already claimed by &6" + chunkOwner.get().getGuildName()));
            } else if (ChunkUtils.chunkIsAdjacentToGuildArea(guild, chunk)) {
                ChatUtils.send(player, ChatUtils.format("&7Outposts must not be next to your Guild."));
            } else {
                if (arguments.length < 2) {
                    ChatUtils.send(player, ChatUtils.format("&7Please provide a name for your Outpost"));
                } else {
                    String outpostName = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
                    ChunkReference reference = guild.createNewOutpost(outpostName, player.getLocation().getChunk());
                    ChatUtils.send(player, ChatUtils.format("&7Claimed outpost &6" + outpostName + " &7at &b" + reference.toString()));
                }
            }
        } else {
            handleNotEnoughChunks(player);
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.CLAIM;
    }


    private void handleNotEnoughChunks(Player player) {
        ChatUtils.send(player, ChatUtils.format("&7Your guild has no remaining outpost slots. Level up your guild to increase outpost slots."));
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.OUTPOSTS_1;
    }
}
