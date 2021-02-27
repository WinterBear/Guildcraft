package dev.snowcave.guilds.commands.pois;

import dev.snowcave.guilds.commands.base.GuildMemberBonusCommandHandler;
import dev.snowcave.guilds.commands.base.GuildMemberPermissionBonusCommandHandler;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by WinterBear on 01/01/2021.
 */
public class GuildStoresCommandHandler extends GuildMemberBonusCommandHandler {


    @Override
    public void handleWithBonus(Player player, User user, String[] arguments) {
        //TODO open inventory to deposit food
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.GUILD_FEASTS;
    }

    @Override
    public List<String> getKeywords() {
        return Arrays.asList("stores");
    }

    @Override
    public String describe() {
        return null;
    }
}
