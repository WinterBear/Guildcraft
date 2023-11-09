package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class TaxKickOptionHandler extends OnOffGuildOptionHandler {

    @Override
    public void enable(Guild guild, Player player) {
        guild.getGuildOptions().setKickMembersWhoDontPayTax(true);
        ChatUtils.send(player, "&7Players who do not have enough funds to pay taxes each day &cwill be kicked &7from the guild.");
    }

    @Override
    public void disable(Guild guild, Player player) {
        guild.getGuildOptions().setKickMembersWhoDontPayTax(false);
        ChatUtils.send(player, "&7Players who do not have enough funds to pay taxes each day &awill not be kicked &7from the guild.");
    }

    @Override
    public String getKeyword() {
        return "taxkick";
    }

    @Override
    public void displayValue(Guild guild, Player player) {
        if (guild.getGuildOptions().isKickMembersWhoDontPayTax()) {
            ChatUtils.send(player, "&7Players who do not have enough funds to pay taxes each day &awill be kicked &7from the guild.");
        } else {
            ChatUtils.send(player, "&7Players who do not have enough funds to pay taxes each day &cwill not be kicked &7from the guild.");
        }
    }
}
