package dev.snowcave.guilds.commands.base;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by WinterBear on 20/12/2020.
 */
public interface GuildCommandHandler {

    List<String> getKeywords();

    @NotNull
    String describe();

    void handle(Player player, String[] arguments);

    default boolean canUse(CommandSender player) {
        return true;
    }

    default List<String> getTabCompletions(CommandSender sender, String[] args) {
        return List.of();
    }

}
