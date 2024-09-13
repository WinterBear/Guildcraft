package dev.snowcave.guilds.commands.pois;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionBonusCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.ChunkUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 28/12/2020.
 */
public class GuildMoveSpawnCommandHandler extends GuildMemberPermissionBonusCommandHandler {

    @Override
    public void handleWithPermissionAndBonus(Player player, User user, String[] arguments) {
        Optional<Guild> chunkGuild = ChunkUtils.getGuild(player.getLocation().getChunk());
        Guild userGuild = user.getGuild();
        if (chunkGuild.isPresent() && chunkGuild.get().equals(userGuild)) {
            userGuild.setSpawnLocation(player.getLocation());
            Chatter.sendP(player, "Guild spawn was moved to your current location.");
        } else {
            Chatter.error(player, "Location is not within guild territory.");
        }

    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.GUILD_SPAWN;
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.MOVE_SPAWN;
    }

    @Override
    public List<String> getKeywords() {
        return Collections.singletonList("movespawn");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild movespawn &8- &7Moves your Guild Spawn to your current location.";
    }
}
