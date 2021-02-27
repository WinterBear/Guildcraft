package dev.snowcave.guilds;

import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.data.StorageController;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.interaction.GuildHallInteractionListener;
import dev.snowcave.guilds.interaction.MonsterWard;
import dev.snowcave.guilds.interaction.PlayerInteractionListener;
import dev.snowcave.guilds.interaction.ProtectionListener;
import dev.snowcave.guilds.utils.EconomyUtils;
import io.github.winterbear.WinterCoreUtils.CommandRegistry;
import io.github.winterbear.wintercore.Annotations.SpigotPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 28/09/2020.
 */
@SpigotPlugin(version = "1.0")
public class Guilds extends JavaPlugin {

    public static final List<Guild> GUILDS = new ArrayList<>();

    public static Optional<Guild> getGuild(String guild){
        return GUILDS.stream().filter(g -> g.getGuildName().equalsIgnoreCase(guild)).findFirst();
    }

    public static Optional<Guild> getGuild(Player player){
        for(Guild guild : GUILDS){
            if(guild.hasMember(player)){
                return Optional.of(guild);
            }
        }
        return Optional.empty();
    }

    public static Optional<User> getUser(Player player){
        if(player != null){
            for(Guild guild : GUILDS){
                if(guild.hasMember(player)){
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
        EconomyUtils.setupEconomy();
        MonsterWard.start(this);
        StorageController.load(this);
    }

    @Override
    public void onDisable(){
        StorageController.save(this);
    }
}
