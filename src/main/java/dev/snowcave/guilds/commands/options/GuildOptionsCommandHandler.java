package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.commands.general.GuildSubcommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by WinterBear on 20/12/2020.
 * <p>
 * /guild options tax 20
 * /guild options explosions on
 * /guild options symbol
 * /guild options taxkick off
 * /guild options pvp off
 * /guild options noticeboard "Welcome to dave's guild"
 * /guild options tag ABBA
 * /guild options public off
 */
public class GuildOptionsCommandHandler extends GuildMemberPermissionCommandHandler {

    private static final List<GuildSubcommandHandler> OPTION_HANDLERS = Arrays.asList(
            new TaxOptionHandler(),
            new ExplosionsOptionHandler(),
            new SymbolOptionHandler(),
            new PvpOptionHandler(),
            new TaxKickOptionHandler(),
            new PublicOptionHandler(),
            new NoticeboardOptionHandler(),
            new TagOptionHandler(),
            new ColorOptionHandler(),
            new RenameGuildOptionHandler(),
            new TraderOptionHandler());

    @Override
    public List<String> getKeywords() {
        return Arrays.asList("options", "o");
    }

    @Override
    public @NotNull String describe() {
        return "&b/guild options &8- &7View and edit options for your Guild";
    }

    private String onOff(boolean value) {
        if (value) {
            return "&aOn";
        } else {
            return "&cOff";
        }
    }

    private String onOff(String on, String off, boolean value) {
        if (value) {
            return on;
        } else {
            return off;
        }
    }

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        Guild guild = user.getGuild();
        if ((arguments[0].equalsIgnoreCase("options") || arguments[0].equalsIgnoreCase("o"))) {
            if (arguments.length > 1) {
                for (GuildSubcommandHandler handler : OPTION_HANDLERS) {
                    if (arguments[1].equalsIgnoreCase(handler.getKeyword())) {
                        if (arguments.length > 2) {
                            handler.handle(guild, player, String.join(" ", Arrays.copyOfRange(arguments, 2, arguments.length)));
                        } else {
                            handler.handleNoArgs(guild, player);
                        }
                        return;
                    }
                }
            }
            ChatUtils.send(player, "&b/g options tax &e<&6amount&e> &8- &7Set the daily tax charged to your guild members &8(&b" + guild.getGuildOptions().getTax() + "&8)");
            ChatUtils.send(player, "&b/g options explosions &e<&aon&8/&coff&e> &8- &7Toggle explosions within your guild &8(" + onOff(guild.getGuildOptions().isExplosionsEnabled()) + "&8)");
            ChatUtils.send(player, "&b/g options symbol &8- &7Shows a list of symbols you can select from for your guild.");
            ChatUtils.send(player, "&b/g options symbol &e<&6symbol&e> &8- &7Select a symbol for your guild. &8(&6" + guild.getGuildOptions().getGuildSymbol().getSymbol() + "&8)");
            ChatUtils.send(player, "&b/g options taxkick &e<&aon&8/&coff&e> &8- &7Automatically kick players who fail to pay their taxes. &8(" + onOff(guild.getGuildOptions().isKickMembersWhoDontPayTax()) + "&8)");
            ChatUtils.send(player, "&b/g options pvp &e<&aon&8/&coff&e> &8- &7Toggle pvp within the guild &8(" + onOff(guild.getGuildOptions().isPvpEnabled()) + "&8)");
            ChatUtils.send(player, "&b/g options noticeboard &e<&6message&e> &8- &7Set a message on the guild noticeboard");
            ChatUtils.send(player, "&b/g options tag &e<&6tag&e> &8- &7Change the guild tag &8(&6" + guild.getGuildOptions().getGuildTag() + "&8)");
            ChatUtils.send(player, "&b/g options public &e<&aon&8/&coff&e> &8- &7Allow anyone to join your guild. &8(" + onOff(guild.getGuildOptions().isPublic()) + "&8)");
            ChatUtils.send(player, "&b/g options rename &e<&6new name&e> &8- &7Rename your Guild. &8(&6" + guild.getGuildName() + "&8)");
            ChatUtils.send(player, "&b/g options traders &e<&aon&8/&coff&e> &8- &7Toggle Wandering Traders. &8(&6" + onOff("&aTraders Allowed", "&cTraders Banned", guild.getGuildOptions().isTradersBanned()) + "&8)");
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.OPTIONS;
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return OPTION_HANDLERS.stream().map(GuildSubcommandHandler::getKeyword).collect(Collectors.toList());
        } else if (args.length > 2) {
            for (GuildSubcommandHandler handler : OPTION_HANDLERS) {
                if (args[1].equalsIgnoreCase(handler.getKeyword())) {
                    return handler.getTabCompletions(sender, args);
                }
            }
        }
        return List.of();
    }

    //Taxes - Requires Tax Editing Permission


}
