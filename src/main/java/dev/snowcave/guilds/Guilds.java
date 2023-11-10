package dev.snowcave.guilds;

import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.data.StorageController;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.interaction.*;
import dev.snowcave.guilds.map.MapRenderer;
import dev.snowcave.guilds.utils.EconomyUtils;
import io.github.winterbear.wintercore.Annotations.SpigotPlugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 28/09/2020.
 */
@SpigotPlugin(version = "1.0")
public class Guilds extends JavaPlugin {

    public static final List<Guild> GUILDS = new ArrayList<>();

    public static Optional<Guild> getGuild(String guild) {
        return GUILDS.stream()
                .filter(g -> g.getGuildName().equalsIgnoreCase(guild)
                        || g.getGuildOptions().getGuildTag().equalsIgnoreCase(guild))
                .findFirst();
    }

    public static Optional<Guild> getGuild(Player player) {
        for (Guild guild : GUILDS) {
            if (guild.hasMember(player)) {
                return Optional.of(guild);
            }
        }
        return Optional.empty();
    }

    public static Optional<User> getUser(Player player) {
        if (player != null) {
            for (Guild guild : GUILDS) {
                if (guild.hasMember(player)) {
                    return Optional.of(guild.getMember(player)).get();
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void onEnable() {
        CommandExecutor guildCommand = new GuildCommand();
        this.getCommand("guild").setExecutor(guildCommand);
        this.getCommand("g").setExecutor(guildCommand);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractionListener(), this);
        this.getServer().getPluginManager().registerEvents(new ProtectionListener(), this);
        this.getServer().getPluginManager().registerEvents(new GuildHallInteractionListener(), this);
        this.getServer().getPluginManager().registerEvents(new GuildStoreListener(), this);
        EconomyUtils.setupEconomy();
        MonsterWard.start(this);
        GuildHallInteractionListener.start(this);
        StorageController.load(this);
        StorageController.start(this);

        MapRenderer.enable(List.of("world"));
        GUILDS.forEach(g -> MapRenderer.renderGuild(String.valueOf(g.hashCode()), g));
    }

    @Override
    public void onDisable() {
        StorageController.save(this);
    }
}
