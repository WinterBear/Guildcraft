package dev.snowcave.guilds.interaction;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.ChunkUtils;
import dev.snowcave.guilds.utils.EntityTypeUtils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Lectern;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.*;

import java.util.*;

/**
 * Created by WinterBear on 22/12/2020.
 */
public class ProtectionListener implements Listener {

    private static final EnumSet<EntityDamageEvent.DamageCause> EXPLOSIONS = EnumSet.of(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
            EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);

    //TODO - Redstone - how?

    private static final List<Material> CONTAINERS = Arrays.asList(Material.CHEST,
            Material.BARREL,
            Material.ENDER_CHEST,
            Material.BREWING_STAND,
            Material.HOPPER,
            Material.DISPENSER,
            Material.FURNACE,
            Material.TRAPPED_CHEST,
            Material.ENCHANTING_TABLE,
            Material.CRAFTING_TABLE,
            Material.SHULKER_BOX);

    private static final List<Material> TOGGLEABLE = Arrays.asList(Material.LEVER,
            Material.REPEATER,
            Material.COMPARATOR,
            Material.BIRCH_BUTTON,
            Material.ACACIA_BUTTON,
            Material.CRIMSON_BUTTON,
            Material.DARK_OAK_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.OAK_BUTTON,
            Material.POLISHED_BLACKSTONE_BUTTON,
            Material.SPRUCE_BUTTON,
            Material.STONE_BUTTON,
            Material.WARPED_BUTTON,
            Material.DARK_OAK_DOOR,
            Material.BIRCH_DOOR,
            Material.ACACIA_DOOR,
            Material.CRIMSON_DOOR,
            Material.IRON_DOOR,
            Material.JUNGLE_DOOR,
            Material.OAK_DOOR,
            Material.SPRUCE_DOOR,
            Material.WARPED_DOOR,
            Material.ACACIA_TRAPDOOR,
            Material.BIRCH_TRAPDOOR,
            Material.CRIMSON_TRAPDOOR,
            Material.DARK_OAK_TRAPDOOR,
            Material.IRON_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR,
            Material.OAK_TRAPDOOR,
            Material.SPRUCE_TRAPDOOR,
            Material.WARPED_TRAPDOOR,
            Material.ACACIA_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.CRIMSON_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.OAK_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.WARPED_FENCE_GATE
    );

    private static final List<Material> PLATES = Arrays.asList(
            Material.POLISHED_BLACKSTONE_PRESSURE_PLATE,
            Material.ACACIA_PRESSURE_PLATE,
            Material.BIRCH_PRESSURE_PLATE,
            Material.CRIMSON_PRESSURE_PLATE,
            Material.DARK_OAK_PRESSURE_PLATE,
            Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Material.JUNGLE_PRESSURE_PLATE,
            Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Material.OAK_PRESSURE_PLATE,
            Material.SPRUCE_PRESSURE_PLATE,
            Material.STONE_PRESSURE_PLATE,
            Material.WARPED_PRESSURE_PLATE
    );

    private boolean playerHasOverride(Player player, Guild guild) {
        return player.hasPermission("guilds.admin") || guild.playerIsAlly(player);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame
                || event.getRightClicked() instanceof ArmorStand
                || event.getRightClicked() instanceof Lectern
                || event.getRightClicked() instanceof StorageMinecart
                || event.getRightClicked() instanceof Chest) {
            Optional<Guild> playerGuild = Guilds.getGuild(event.getPlayer());
            Optional<Guild> chunkGuild = ChunkUtils.getGuild(event.getPlayer().getLocation().getChunk());
            if (chunkGuild.isPresent() && !chunkGuild.equals(playerGuild) && !playerHasOverride(event.getPlayer(), chunkGuild.get())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Optional<Guild> playerGuild = Guilds.getGuild(player);
        Optional<Guild> chunkGuild = ChunkUtils.getGuild(player.getLocation().getChunk());
        if (chunkGuild.isPresent() && !chunkGuild.equals(playerGuild) && !playerHasOverride(event.getPlayer(), chunkGuild.get())) {
            if (event.getAction().equals(Action.PHYSICAL) && event.getClickedBlock() != null) {
                Material mat = event.getClickedBlock().getType();
                if (CONTAINERS.contains(mat) || TOGGLEABLE.contains(mat) || PLATES.contains(mat)) {
                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                }
            }
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent event){
        Player player = event.getPlayer();
        Optional<Guild> playerGuild = Guilds.getGuild(player);
        Optional<Guild> chunkGuild = ChunkUtils.getGuild(player.getLocation().getChunk());
        if (chunkGuild.isPresent() && !chunkGuild.equals(playerGuild) && !playerHasOverride(event.getPlayer(), chunkGuild.get())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketFillEvent event){
        Player player = event.getPlayer();
        Optional<Guild> playerGuild = Guilds.getGuild(player);
        Optional<Guild> chunkGuild = ChunkUtils.getGuild(player.getLocation().getChunk());
        if (chunkGuild.isPresent() && !chunkGuild.equals(playerGuild) && !playerHasOverride(event.getPlayer(), chunkGuild.get())) {
            event.setCancelled(true);
        }
    }

    //Combat Block
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent entityEvent) {
        if (EntityTypeUtils.entityIsPlayer(entityEvent.getEntity())) {
            Optional<Guild> victimGuild = Guilds.getGuild((Player) entityEvent.getEntity());
            Optional<Guild> attackerGuild;
            boolean attackerIsPlayer = EntityTypeUtils.entityIsPlayer(entityEvent.getDamager());
            if (attackerIsPlayer) {
                attackerGuild = Guilds.getGuild((Player) entityEvent.getDamager());
            } else {
                attackerGuild = Optional.empty();
            }
            Chunk chunkVictim = entityEvent.getEntity().getLocation().getChunk();
            Optional<Guild> chunkGuild = ChunkUtils.getGuild(chunkVictim);
            if (chunkGuild.isPresent()) { //If the victim was damaged within a guild area
                if (attackerIsPlayer //If attacker is player
                        && victimGuild.equals(chunkGuild) //And the victim was a member of the same guild
                        && !attackerGuild.equals(chunkGuild)) { //And the attacker is not a member of of the same guild
                    entityEvent.setCancelled(true);
                }
                if (!attackerIsPlayer //If attacker is mob
                        && Levels.getAllGuildBonuses(victimGuild.get().getLevel()).contains(GuildBonus.MONSTER_WARD)) { //And the victim's guild has Monster Ward
                    entityEvent.setCancelled(true);
                }
            }
        }

    }

    //@EventHandler
    //public void onRedstone(BlockRedstoneEvent redstoneEvent){
    //    Optional<Guild> guild = ChunkUtils.getGuild(redstoneEvent.getBlock().getChunk());
    //    if(guild.isPresent()){
    //        if(redstoneEvent.)
    //    }
    //}

    //What does this even do?
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFromTo(BlockFromToEvent event) {
        Optional<Guild> toGuild = ChunkUtils.getGuild(event.getToBlock().getChunk());
        Optional<Guild> fromGuild = ChunkUtils.getGuild(event.getBlock().getChunk());
        if (toGuild.isPresent() && !fromGuild.equals(toGuild)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        handleExplosion(event.getLocation().getChunk(), event, event.blockList());
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        handleExplosion(event.getBlock().getChunk(), event, event.blockList());
    }

    private void handleExplosion(Chunk chunk, Cancellable event, List<Block> blocks) {
        Optional<Guild> guild = ChunkUtils.getGuild(chunk);
        if (guild.isPresent() && !guild.get().getGuildOptions().isExplosionsEnabled()) {
            event.setCancelled(true);
            return;
        }
        boolean explosionInGuild = ChunkUtils.getGuild(chunk).isPresent();
        List<Block> blocksCopy = new ArrayList<>(blocks);
        for (Block block : blocksCopy) {
            Optional<Guild> blockGuild = ChunkUtils.getGuild(block.getChunk());
            if (blockGuild.isPresent()
                    && (!blockGuild.get().getGuildOptions().isExplosionsEnabled() || !explosionInGuild)) {
                blocks.remove(block);
            }
        }
    }


    @EventHandler
    public void onExplosionDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player &&
                EXPLOSIONS.contains(e.getCause())) {
            Player p = (Player) e.getEntity();
            Optional<Guild> entityGuild = ChunkUtils.getGuild(p.getLocation().getChunk());
            Optional<Guild> explosionGuild = ChunkUtils.getGuild(e.getEntity().getLocation().getChunk());
            if (entityGuild.isPresent() && !explosionGuild.isPresent()) {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onPistonEvent(BlockPistonExtendEvent e) {
        if (ChunkUtils.getGuild(e.getBlock().getChunk()).isEmpty()) {
            if (e.getBlocks().stream().anyMatch(b -> ChunkUtils.getGuild(b.getChunk()).isPresent())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPistonEvent(BlockPistonRetractEvent e) {
        if (ChunkUtils.getGuild(e.getBlock().getChunk()).isEmpty()) {
            if (e.getBlocks().stream().anyMatch(b -> ChunkUtils.getGuild(b.getChunk()).isPresent())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Optional<Guild> chunkGuild = ChunkUtils.getGuild(event.getBlock().getChunk());
        if (chunkGuild.isPresent()) {
            Optional<User> user = Guilds.getUser(event.getPlayer());
            if (user.isPresent()) {
                Guild userGuild = user.get().getGuild();
                if (!userGuild.equals(chunkGuild.get()) && !playerHasOverride(event.getPlayer(), chunkGuild.get())) {
                    event.setCancelled(true);
                }
            } else if (!playerHasOverride(event.getPlayer(), chunkGuild.get())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Optional<Guild> chunkGuild = ChunkUtils.getGuild(event.getBlock().getChunk());
        if (chunkGuild.isPresent() && !playerHasOverride(event.getPlayer(), chunkGuild.get())) {
            Optional<User> user = Guilds.getUser(event.getPlayer());
            if (user.isPresent()) {
                Guild userGuild = user.get().getGuild();
                if (!userGuild.equals(chunkGuild.get())) {
                    event.setCancelled(true);
                }
            } else if (!playerHasOverride(event.getPlayer(), chunkGuild.get())) {
                event.setCancelled(true);
            }
        }
    }

}
