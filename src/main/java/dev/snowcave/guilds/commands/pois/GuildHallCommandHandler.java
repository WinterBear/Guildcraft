package dev.snowcave.guilds.commands.pois;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionBonusCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.guildhalls.GuildHallShape;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.ChunkUtils;
import dev.snowcave.guilds.utils.LocationUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by WinterBear on 22/12/2020.
 */
public class GuildHallCommandHandler extends GuildMemberPermissionBonusCommandHandler {

    @Override
    public void handleWithPermissionAndBonus(Player player, User user, String[] arguments) {
        if (arguments.length == 1) {
            ChatUtils.send(player, "&3" + user.getGuild().getGuildHall().getName());
            ChatUtils.send(player, "&3Location&7: &e" + LocationUtils.toDisplayString(user.getGuild().getGuildHall().getCenter()));
            ChatUtils.send(player, "&3Size&7: &b" + user.getGuild().getGuildHall().getSize());
            ChatUtils.send(player, "&3Shape&7: &b" + user.getGuild().getGuildHall().getShape());
            ChatUtils.send(player, "&3Available Commands&7:");
            ChatUtils.send(player, "&b/g hall move &8- &7Move your Guild Hall to your current location.");
            ChatUtils.send(player, "&b/g hall name &e<&6New Name&e> &8- &7Rename your Guild Hall.");
            ChatUtils.send(player, "&b/g hall shape &e<&aCircle&8/&cSquare&e> &8- &7Change the shape of your Guild Hall's area of effect.");
        } else {
            if (arguments[1].equalsIgnoreCase("move")) {
                handleMove(player, user);
            } else if (arguments[1].equalsIgnoreCase("name")) {
                if (arguments.length < 3) {
                    Chatter.error(player, "You must specify a name.");
                }
                handleName(player, user, String.join(" ", Arrays.copyOfRange(arguments, 2, arguments.length)));
            } else if (arguments[1].equalsIgnoreCase("shape")) {
                if (arguments.length < 3) {
                    Chatter.error(player, "You must specify a shape - &acircle&7 or &bsquare.");
                } else {
                    handleShape(player, user, arguments[1]);
                }

            } else {
                Chatter.error(player, "Not a valid option. Options are &emove&c, &ename &cand &eshape&c.");
            }
        }
    }

    @Override
    public GuildBonus getBonus() {
        return GuildBonus.GUILD_HALL;
    }

    private void handleMove(Player player, User user) {
        Optional<Guild> guildAtLocation = ChunkUtils.getGuild(player.getLocation().getChunk());
        if (guildAtLocation.isPresent() && user.getGuild().equals(guildAtLocation.get())) {
            user.getGuild().getGuildHall().setCenter(player.getLocation());
            Chatter.sendP(player, "The Guild Hall was moved to your current location.");
        } else {
            Chatter.error(player, "Location is not within your Guilds territory.");
        }
    }

    private void handleName(Player player, User user, String newName) {
        user.getGuild().getGuildHall().setName(newName);
        Chatter.sendP(player, "Changed Guild Hall name to &b" + newName);
    }

    private void handleShape(Player player, User user, String shape) {
        if (shape.equalsIgnoreCase("circle")) {
            user.getGuild().getGuildHall().setShape(GuildHallShape.CIRCLE);
            Chatter.sendP(player, "Set Guild Hall shape to &6Circle");
        } else if (shape.equalsIgnoreCase("square")) {
            user.getGuild().getGuildHall().setShape(GuildHallShape.SQUARE);
            Chatter.sendP(player, "Set Guild Hall shape to &6Square");
        } else {
            Chatter.send(player, "&b/g hall shape &e<&aCircle&8/&cSquare&e> &8- &7Change the shape of your Guild Hall's area of effect &7(&6" + user.getGuild().getGuildHall().getShape() + "&7)");
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
    public @NotNull String describe() {
        return "&b/guild hall &8- &7View and edit Guild Hall options.";
    }
}
