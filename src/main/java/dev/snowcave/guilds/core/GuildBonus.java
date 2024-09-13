package dev.snowcave.guilds.core;

import dev.snowcave.guilds.core.guildhalls.GuildHallUtils;
import net.md_5.bungee.api.ChatColor;

import java.util.function.Consumer;

/**
 * Created by WinterBear on 19/12/2020.
 */
public enum GuildBonus {

    GUILD_SPAWN("Guild Spawn", "❈", "Teleport to your Guild"),
    GUILD_HALL("Guild Hall", "🏠", "Mark an area within your Guild as your Guild Hall. Guild Halls will heal any Guild members within their area", GuildHallUtils::setupGuildHall),
    GUILD_FEASTS("Guild Feasts", "🍰", "Deposit food into your Guild Provisions to automatically feed anyone within the Guild Hall."),
    MONSTER_WARD("Monster Ward", "🐺", "A magical ward prevents hostile mobs from entering your guild's territory."),
    RESTING_GROUNDS("Resting Grounds", "❤", "Entering the guild hall grants restfulness and prevents Phantom attacks for a while."),
    GUILD_CHAT("Guild Chat", "💬", "Chat with your guild members"),
    PVP_EXCLUSIVE_ZONE("Fighting Arena / Safe Zone", "⚔", "Create an area within your guild that negates the effects of pvp protection."),
    NAMED_PLOT("Writ of Proclamation", "\uD83D\uDC8E", "Name areas within your Guild."),
    BOOST_MINING_FRENZY("Writ of Mining Attunement", "\uD83D\uDC8E", "Grants bonus mining loot to all guild members on Mondays and Thursdays"),
    BOOST_WOODCUTTING_EFFICIENCY("Writ of Woodcutting Efficiency", "\uD83D\uDC8E", "Grants bonus woodcutting loot to all guild members on Tuesdays and Fridays"),
    BOOST_HUNTING_ATTUNEMENT("Writ of Hunting Proficiency", "\uD83D\uDC8E", "Grants bonus mob loot to all guild members on Wednesdays and Saturdays"),
    BOOST_MIRROR_ATTUNEMENT("Writ of Hidden Mirrors", "\uD83D\uDC8E", "Grants bonus drops within the Mirror world on Sundays"),
    BOOST_FOOD("Writ of Culinary Refinement", "&a🍰", "Grants bonus Guild Feast stock when depositing food."),
    BOOST_FOOD_2("Writ of Culinary Refinement 2", "&e🍰", "Grants bonus Guild Feast stock when depositing food."),
    BOOST_FOOD_3("Writ of Culinary Refinement 3", "&b🍰", "Grants bonus Guild Feast stock when depositing food."),
    BOOST_FOOD_4("Writ of Culinary Refinement 4", "&6🍰", "Grants bonus Guild Feast stock when depositing food."),
    LIVESTOCK_AURA("Livestock Ward", "\uD83D\uDC8E", "A mysterious ward protects all livestock within the guild from damage by non-guild members."),
    GUILD_HALL_RADIUS_1("Guild Hall Upgrade 1", "&a🏠", "Upgrades the maximum size of your Guild Hall", GuildHallUtils::increaseHallSize1),
    GUILD_HALL_RADIUS_2("Guild Hall Upgrade 2", "&e🏠", "Upgrades the maximum size of your Guild Hall", GuildHallUtils::increaseHallSize2),
    GUILD_HALL_RADIUS_3("Guild Hall Upgrade 3", "&b🏠", "Upgrades the maximum size of your Guild Hall"),
    GUILD_HALL_AMBIENT_MUSIC("Ambient Music", "\uD83D\uDC8E", "Select music that will play within your Guild Hall"),
    NPC_REPAIR("NPC: Blacksmith", "\uD83D\uDC8E", "Summon a Blacksmith NPC who will repair items for a price."),
    NPC_CONTRACT("NPC: Foreman", "\uD83D\uDC8E", "Summon a Foreman NPC who will offer Guild Contracts"),
    GUILD_VAULT("Guild Vault", "\uD83D\uDC8E", "Use your guild Vault to store items."), //Works like the old infinite bags except not infinite
    GUILD_VAULT_SIZE_UPGRADE_1("Guild Vault Upgrade 1", "\uD83D\uDC8E", "Increase the size of your Guild Vault."),
    GUILD_VAULT_SIZE_UPGRADE_2("Guild Vault Upgrade 2", "\uD83D\uDC8E", "Increase the size of your Guild Vault."),
    GUILD_VAULT_SIZE_UPGRADE_3("Guild Vault Upgrade 3", "\uD83D\uDC8E", "Increase the size of your Guild Vault."),

    GUILD_AURA_1("Guild Aura 1", "\uD83D\uDC8E", "Bestow a variety of buffs on guild members who are close to each other"),
    GUILD_AURA_2("Guild Aura 2", "\uD83D\uDC8E", "Bestow a greater variety of buffs on guild members who are close to each other"),
    GUILD_AURA_3("Guild Aura 3", "\uD83D\uDC8E", "Bestow a buff on guild members who are close to each other"),
    OUTPOSTS_1("Guild Outposts", "🎌", "Create Guild Outposts "),
    OUTPOSTS_2("Bonus Guild Outpost 1", "&a🎌", "Create an additional Guild Outpost "),
    OUTPOSTS_3("Bonus Guild Outpost 2", "&e🎌", "Create an additional Guild Outpost "),
    OUTPOSTS_4("Bonus Guild Outpost 3", "&b🎌", "Create an additional Guild Outpost "),
    OUTFITS("Guild Outfits", "\uD83D\uDC8E", "Upload a skin that any of your guild members can transform into on command."),

    ALLIANCES("Alliances", "", "Form an Alliance with other Guilds"),
    FLIGHT("Flight", "\uD83E\uDEB6", "Fly within your Guild's territory");

    private final String displayName;

    private final String description;

    private final String symbol;

    private Consumer<Guild> specialAction;

    GuildBonus(String displayName, String symbol, String description) {
        this.displayName = displayName;
        this.symbol = symbol;
        this.description = description;
    }

    GuildBonus(String displayName, String symbol, String description, Consumer<Guild> specialAction) {
        this.displayName = displayName;
        this.symbol = symbol;
        this.description = description;
        this.specialAction = specialAction;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getSymbol() {
        return symbol;
    }

    public void levelUpAction(Guild guild) {
        if (specialAction != null) {
            specialAction.accept(guild);
        }
    }


    @Override
    public String toString() {
        return ChatColor.of("#52a8ff") + getDisplayName() + " &8- &e" + getDescription() + "&8";
    }
}
