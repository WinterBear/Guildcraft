package dev.snowcave.guilds.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WinterBear on 22/12/2020.
 */
public class EntityTypeUtils {

    public static final EnumSet<EntityType> HOSTILE_ENTITIES = EnumSet.of(
            EntityType.CREEPER,
            EntityType.ENDERMITE,
            EntityType.ENDERMAN,
            EntityType.SLIME,
            EntityType.ZOMBIE,
            EntityType.BLAZE,
            EntityType.CAVE_SPIDER,
            EntityType.DROWNED,
            EntityType.SPIDER,
            EntityType.EVOKER,
            EntityType.HUSK,
            EntityType.HOGLIN,
            EntityType.MAGMA_CUBE,
            EntityType.PILLAGER,
            EntityType.RAVAGER,
            EntityType.SILVERFISH,
            EntityType.SKELETON,
            EntityType.STRAY,
            EntityType.VEX,
            EntityType.VINDICATOR,
            EntityType.WITCH,
            EntityType.WITHER_SKELETON,
            EntityType.ZOGLIN,
            EntityType.ZOMBIE_VILLAGER
    );

    public static boolean entityIsPlayer(Entity entity) {
        return entity.getType().equals(EntityType.PLAYER);
    }



}
