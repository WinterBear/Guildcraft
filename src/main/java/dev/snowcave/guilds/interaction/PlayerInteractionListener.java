package dev.snowcave.guilds.interaction;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.ChunkUtils;
import dev.snowcave.guilds.utils.EntityTypeUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class PlayerInteractionListener implements Listener {

    //Explore
    @EventHandler
    public void PlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Chunk fromChunk = event.getFrom().getChunk();
        Chunk toChunk = event.getTo().getChunk();
        checkMove(fromChunk, toChunk, player, event.getTo());
    }

    private void checkMove(Chunk fromChunk, Chunk toChunk, Player player, Location destination) {
        if (fromChunk != toChunk) {
            Optional<Guild> toGuild = ChunkUtils.getGuild(toChunk);
            Optional<Guild> fromGuild = ChunkUtils.getGuild(fromChunk);

            if (toGuild.isPresent()) { // To Chunk is inside a Guild
                if (fromGuild.isEmpty() || !fromGuild.get().equals(toGuild.get())) { // Either we are entering a Guild from the wilderness or moving from one guild to another
                    Optional<String> outpost = ChunkUtils.getChunkOutpost(toGuild.get(), toChunk);
                    Optional<User> user = Guilds.getUser(player);
                    if(user.isPresent()){
                        if (user.get().getGuild().equals(toGuild.get()) && Levels.getAllGuildBonuses(user.get().getGuild().getLevel()).contains(GuildBonus.FLIGHT)){
                            player.setAllowFlight(true);
                        } else if (fromGuild.isPresent() && player.getAllowFlight() && !player.hasPermission("essentials.fly")) {
                            player.setAllowFlight(false);
                        }
                    }
                    if (outpost.isEmpty()) {
                        sentGuildWelcomeTitle(toGuild.get(), player);
                    } else {
                        sentGuildOutpostWelcomeTitle(toGuild.get(), player, outpost.get());
                    }
                }

            } else if (fromGuild.isPresent()) { // Leaving a Guild
                sendWildernessTitle(player, destination);
                if(player.getAllowFlight() && !player.hasPermission("essentials.fly")){
                    player.setAllowFlight(false);
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        Chunk fromChunk = event.getFrom().getChunk();
        Chunk toChunk = event.getTo().getChunk();
        checkMove(fromChunk, toChunk, player, event.getTo());
    }

    private static void sendWildernessTitle(Player player, Location toLocation) {
        if (getWorldGuardRegions(toLocation).size() < 2) {
            player.sendTitle(ChatUtils.format("&fﾟ･&8-&f･ &a✦ &f･&8-&f･ﾟ"), ChatUtils.format("&2Wilderness"), 12, 18, 12);
        }
    }

    @NotNull
    private static ApplicableRegionSet getWorldGuardRegions(Location toLocation) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer()
                .get(BukkitAdapter.adapt(toLocation.getWorld()))
                .getApplicableRegions(BukkitAdapter.asBlockVector(toLocation));
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
