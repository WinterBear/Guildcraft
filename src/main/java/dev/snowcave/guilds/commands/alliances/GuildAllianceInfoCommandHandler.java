package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.core.alliances.Alliance;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.CommandUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by WinterBear on 18/11/2023.
 */
public class GuildAllianceInfoCommandHandler implements GuildCommandHandler {


    @Override
    public List<String> getKeywords() {
        return List.of("info");
    }

    @Override
    public @NotNull String describe() {
        return GuildAllianceCommandHandler.allianceCommandDescription("info",
                "View alliance info", "Alliance");
    }

    @Override
    public void handle(Player player, String[] arguments) {
        GuildCommand.asGuildUser(this::info, player, arguments);
    }

    private void info(Player player, User user, String[] arguments){
        Chatter chatter = new Chatter(player);
        if(arguments.length > 2){
            String infoAlliance = CommandUtils.squashArgs(2, arguments);
            Optional<Alliance> alliance = Optional.ofNullable(Guilds.ALLIANCES.getOrDefault(infoAlliance, null));
            if(alliance.isPresent()){
                showAllianceInfo(alliance.get(), chatter);
            } else {
                chatter.error("Could not find an Alliance by that name.");
            }
        } else {
            Optional<Alliance> alliance = Optional.ofNullable(Guilds.ALLIANCES.getOrDefault(user.getGuild().getAlliance(), null));
            if(alliance.isPresent()){
                showAllianceInfo(alliance.get(), chatter);
            } else {
                chatter.error("You are not in an Alliance.");
            }
        }
    }

    private void showAllianceInfo(Alliance alliance, Chatter chatter) {
        chatter.send("&7&m-------------------&r &4&lAlliance Info &7&m-------------------");
        chatter.send("&7Name: &f" + alliance.getName());
        chatter.send("&7Leaders: &f" + String.join(", ", getLeaders(alliance)));
        chatter.send("&7Type: &f" + alliance.getAllianceType().getDisplayName());
        chatter.send("&7Members: &f" + String.join(", ", alliance.getMembers()));
        chatter.send("&7Unity: &f" + alliance.displayUnity());
        chatter.send("&7Balance: &f" + alliance.displayBalance());


    }

    private List<String> getLeaders(Alliance alliance){
        return alliance.getLeaders().stream()
                .map(Guilds::getUserByUUID)
                .filter(Objects::nonNull)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(User::getName)
                .collect(Collectors.toList());
    }


}
