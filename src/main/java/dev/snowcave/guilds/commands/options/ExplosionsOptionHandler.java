package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.core.Guild;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 26/12/2020.
 */
public class ExplosionsOptionHandler extends OnOffGuildOptionHandler {

    @Override
    public String getKeyword() {
        return "explosions";
    }

    @Override
    public void enable(Guild guild, Player player) {
        guild.getGuildOptions().setExplosionsEnabled(true);
        ChatUtils.send(player, "&7Enabled &cexplosions &7within guild territory. Explosions outside guild territory will still be unable to affect blocks and entities within guild territory.");
    }

    @Override
    public void disable(Guild guild, Player player) {
        guild.getGuildOptions().setExplosionsEnabled(false);
        ChatUtils.send(player, "&7Disabled &cexplosions &7within guild territory. Explosions will be unable to affect blocks and entities within guild territory.");
    }

    @Override
    public void displayValue(Guild guild, Player player) {
        if (guild.getGuildOptions().isExplosionsEnabled()) {
            ChatUtils.send(player, "&7Explosions are &aenabled &7within guild territory. Explosions outside guild territory will still be unable to affect blocks and entities within guild territory.");
        } else {
            ChatUtils.send(player, "&7Explosions are &cdisabled &7within guild territory. Explosions will be unable to affect blocks and entities within guild territory.");
        }
    }
}
