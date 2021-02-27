package dev.snowcave.guilds.commands.pois;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionBonusCommandHandler;
import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.guildhalls.GuildHallShape;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by WinterBear on 22/12/2020.
 */
public class GuildHallCommandHandler extends GuildMemberPermissionBonusCommandHandler {

    @Override
    public void handleWithPermissionAndBonus(Player player, User user, String[] arguments) {
        if(arguments.length == 1){
            ChatUtils.send(player, "&3" + user.getGuild().getGuildHall().getName());
            ChatUtils.send(player, "&3Location&7: &e" + user.getGuild().getGuildHall().getCenter());
            ChatUtils.send(player, "&3Size&7: &b" + user.getGuild().getGuildHall().getSize());
            ChatUtils.send(player, "&3Shape&7: &b" + user.getGuild().getGuildHall().getShape());
            ChatUtils.send(player, "&3Available Commands&7:");
            ChatUtils.send(player, "&b/g hall move &8- &7Move your Guild Hall to your current location.");
            ChatUtils.send(player, "&b/g hall name &e<&6New Name&e> &8- &7Rename your Guild Hall.");
            ChatUtils.send(player, "&b/g hall shape &e<&aCircle&8/&cSquare&e> &8- &7Change the shape of your Guild Hall's area of effect.");
        } else {
            if(arguments[1].equalsIgnoreCase("move")){
                handleMove(player, user);
            } else if(arguments[1].equalsIgnoreCase("name")){
                handleName(player, user, String.join(" ", Arrays.copyOfRange(arguments, 2, arguments.length)));
            } else if (arguments[1].equalsIgnoreCase("shape")){
                handleShape(player, user, arguments[2]);
            } else {
                ChatUtils.send(player, "&7Not a valid option. Options are &emove&7, &ename &7and &eshape&7.");
            }
        }
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.GUILD_HALL;
    }

    private void handleMove(Player player, User user){
        user.getGuild().getGuildHall().setCenter(player.getLocation());
        ChatUtils.send(player, "&3Moved Guild Hall to your current location.");
    }

    private void handleName(Player player, User user, String newName){
        user.getGuild().getGuildHall().setName(newName);
        ChatUtils.send(player, "&3Changed Guild Hall name to &b" + newName);
    }

    private void handleShape(Player player, User user, String shape){
        if(shape.equalsIgnoreCase("circle")){
            user.getGuild().getGuildHall().setShape(GuildHallShape.CIRCLE);
            ChatUtils.send(player, "&3Set Guild Hall shape to &6Circle");
        } else if (shape.equalsIgnoreCase("square")){
            user.getGuild().getGuildHall().setShape(GuildHallShape.SQUARE);
            ChatUtils.send(player, "&3Set Guild Hall shape to &6Square");
        } else {
            ChatUtils.send(player, "&b/g hall shape &e<&aCircle&8/&cSquare&e> &8- &7Change the shape of your Guild Hall's area of effect &7(&6" + user.getGuild().getGuildHall().getShape() + "&7)");
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.GUILDHALL;
    }

    @Override
    public List<String> getKeywords() {
        return Arrays.asList("hall", "h");
    }

    @Override
    public String describe() {
        return "&b/g hall move &8- &7Move your Guild Hall to your current location.\n" +
                "&b/g hall name &e<&6New Name&e> &8- &7Rename your Guild Hall.\n" +
                "&b/g hall shape &e<&aCircle&8/&cSquare&e> &8- &7Change the shape of your Guild Hall's area of effect.";
    }
}
