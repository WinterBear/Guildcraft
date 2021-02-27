package dev.snowcave.guilds.commands.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by WinterBear on 20/12/2020.
 */
public interface GuildCommandHandler {

        List<String> getKeywords();

        public String describe();

        void handle(Player player, String[] arguments);

        public default boolean canUse(CommandSender player){
            return true;
        }

}
