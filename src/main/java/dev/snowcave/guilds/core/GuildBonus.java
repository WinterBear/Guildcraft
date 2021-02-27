package dev.snowcave.guilds.core;

import dev.snowcave.guilds.core.guildhalls.GuildHallUtils;
import net.md_5.bungee.api.ChatColor;

import java.util.function.Consumer;

/**
 * Created by WinterBear on 19/12/2020.
 */
public enum GuildBonus {

    GUILD_SPAWN("Guild Spawn","Teleport to your Guild"),
    GUILD_HALL("Guild Hall","Mark an area within your Guild as your Guild Hall. Guild Halls will heal any Guild members within their area", GuildHallUtils::setupGuildHall),
    GUILD_FEASTS("Guild Feasts","Deposit food into your Guild Provisions to automatically feed anyone within the Guild Hall."),
    MONSTER_WARD("Monster Ward","A magical ward prevents hostile mobs from entering your guild's territory."),
    RESTING_GROUNDS("Resting Grounds","Entering the guild hall grants restfulness and prevents Phantom attacks for a while."),
    PVP_EXCLUSIVE_ZONE("Fighting Arena / Safe Zone","Create an area within your guild that negates the effects of pvp protection."),
    NAMED_PLOT("Writ of Proclamation","Name areas within your Guild."),
    BOOST_MINING_FRENZY("Writ of Mining Attunement","Grants bonus mining loot to all guild members on Mondays and Thursdays"),
    BOOST_WOODCUTTING_EFFICIENCY("Writ of Woodcutting Efficiency","Grants bonus woodcutting loot to all guild members on Tuesdays and Fridays"),
    BOOST_HUNTING_ATTUNEMENT("Writ of Hunting Proficiency","Grants bonus mob loot to all guild members on Wednesdays and Saturdays"),
    BOOST_MIRROR_ATTUNEMENT("Writ of Hidden Mirrors","Grants bonus drops within the Mirror world on Sundays"),
    BOOST_FOOD("Writ of Culinary Refinement","Grants bonus Guild Feast stock when depositing food."),
    LIVESTOCK_AURA("Livestock Ward","A mysterious ward protects all livestock within the guild from damage by non-guild members."),
    GUILD_HALL_RADIUS_1("Guild Hall Upgrade 1","Upgrades the maximum size of your Guild Hall"),
    GUILD_HALL_RADIUS_2("Guild Hall Upgrade 2","Upgrades the maximum size of your Guild Hall"),
    GUILD_HALL_RADIUS_3("Guild Hall Upgrade 3","Upgrades the maximum size of your Guild Hall"),
    GUILD_HALL_AMBIENT_MUSIC("Ambient Music","Select music that will play within your Guild Hall"),
    NPC_REPAIR("NPC: Blacksmith","Summon a Blacksmith NPC who will repair items for a price."),
    NPC_CONTRACT("NPC: Foreman","Summon a Foreman NPC who will offer Guild Contracts"),
    GUILD_VAULT("Guild Vault","Use your guild Vault to store items."), //Works like the old infinite bags except not infinite
    GUILD_VAULT_SIZE_UPGRADE_1("Guild Vault Upgrade 1","Increase the size of your Guild Vault."),
    GUILD_VAULT_SIZE_UPGRADE_2("Guild Vault Upgrade 2","Increase the size of your Guild Vault."),
    GUILD_VAULT_SIZE_UPGRADE_3("Guild Vault Upgrade 3","Increase the size of your Guild Vault."),
    OUTPOSTS("Guild Outposts","Create Guild Outposts "),
    OUTFITS("Guild Outfits","Upload a skin that any of your guild members can transform into on command.");

    private String displayName;

    private String description;

    private Consumer<Guild> specialAction;

    GuildBonus(String displayName, String description){
        this.displayName = displayName;
        this.description = description;
    }

    GuildBonus(String displayName, String description, Consumer<Guild> specialAction){
        this.displayName = displayName;
        this.description = description;
        this.specialAction = specialAction;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public void levelUpAction(Guild guild){
        if(specialAction != null){
            specialAction.accept(guild);
        }
    }


    @Override
    public String toString() {
        return ChatColor.of("#52a8ff") + displayName + " &8- &e" + description;
    }
}
