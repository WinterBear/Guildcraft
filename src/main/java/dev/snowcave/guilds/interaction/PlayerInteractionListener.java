package dev.snowcave.guilds.interaction;

import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.ChunkUtils;
import dev.snowcave.guilds.utils.EntityTypeUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class PlayerInteractionListener implements Listener {

    //Block Interact

    //Container Interact

    //Workbench Interact

    //Entity Interact

    //Explore
    @EventHandler
    public void PlayerMove(PlayerMoveEvent event) {
        if (event.getTo() != null) {
            Player player = event.getPlayer();
            Chunk fromChunk = event.getFrom().getChunk();
            Chunk toChunk = event.getTo().getChunk();
            if (fromChunk != toChunk) {
                Optional<Guild> toGuild = ChunkUtils.getGuild(toChunk);
                Optional<Guild> fromGuild = ChunkUtils.getGuild(fromChunk);

                if (toGuild.isPresent()) {

                    if (!fromGuild.isPresent() || !fromGuild.get().equals(toGuild.get())) {
                        Optional<String> outpost = ChunkUtils.getChunkOutpost(toGuild.get(), toChunk);
                        if (!outpost.isPresent()) {
                            sentGuildWelcomeTitle(toGuild.get(), player);
                        } else {
                            sentGuildOutpostWelcomeTitle(toGuild.get(), player, outpost.get());
                        }

                    }
                } else if (fromGuild.isPresent()) {
                    player.sendTitle(ChatUtils.format("&fﾟ･&8-&f･ &a✦ &f･&8-&f･ﾟ"), ChatUtils.format("&2Wilderness"), 12, 18, 12);
                }
            }
        }
    }

    private void sentGuildWelcomeTitle(Guild guild, Player player) {
        ChatColor guildColor = ChatColor.of(guild.getGuildOptions().getColor());
        String guildSymbol = guild.getGuildOptions().getGuildSymbol().getSymbol();
        player.sendTitle(ChatUtils.format("&fﾟ･&8-&f･ &3" + guildColor + guildSymbol + " &f･&8-&f･ﾟ"), guildColor + guild.getGuildName(), 12, 18, 12);
    }

    private void sentGuildOutpostWelcomeTitle(Guild guild, Player player, String outpost) {
        ChatColor guildColor = ChatColor.of(guild.getGuildOptions().getColor());
        String guildSymbol = guild.getGuildOptions().getGuildSymbol().getSymbol();
        player.sendTitle(ChatUtils.format("&fﾟ･&8-&f･ &3" + guildColor + guildSymbol + " &f･&8-&f･ﾟ"), guildColor + guild.getGuildName() + ChatUtils.format(" &f- ") + outpost, 12, 18, 12);
    }

    //PVP Block
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent entityEvent) {
        if (EntityTypeUtils.entityIsPlayer(entityEvent.getDamager())
                && EntityTypeUtils.entityIsPlayer(entityEvent.getEntity())) {
            Chunk chunk = entityEvent.getEntity().getLocation().getChunk();
            if (ChunkUtils.getGuild(chunk).isPresent()
                    && !ChunkUtils.getGuild(chunk).get().getGuildOptions().isPvpEnabled()) {
                entityEvent.setCancelled(true);
            }
        }
    }


}
