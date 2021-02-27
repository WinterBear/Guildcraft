package dev.snowcave.guilds.commands.options;

import dev.snowcave.guilds.commands.base.GuildMemberPermissionCommandHandler;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.core.users.permissions.GuildPermission;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by WinterBear on 20/12/2020.
 *
 * /guild options tax 20
 * /guild options explosions on
 * /guild options symbol
 * /guild options taxkick off
 * /guild options pvp off
 * /guild options noticeboard "Welcome to dave's guild"
 * /guild options tag ABBA
 * /guild options public off
 *
 */
public class GuildOptionsCommandHandler extends GuildMemberPermissionCommandHandler {

    private static final List<GuildOptionHandler> OPTION_HANDLERS = Arrays.asList(
            new TaxOptionHandler(),
            new ExplosionsOptionHandler(),
            new SymbolOptionHandler(),
            new PvpOptionHandler(),
            new TaxKickOptionHandler(),
            new PublicOptionHandler(),
            new NoticeboardOptionHandler(),
            new TagOptionHandler(),
            new ColorOptionHandler(),
            new RenameGuildOptionHandler());
    @Override
    public List<String> getKeywords() {
        return Arrays.asList("options", "o");
    }

    @Override
    public String describe() {
        return "&b/guild options &8- &7View and edit options for your Guild";
    }

    private String onOff(boolean value){
        if(value){
            return "&aOn";
        } else {
            return "&cOff";
        }
    }

    @Override
    public void handleWithPermission(Player player, User user, String[] arguments) {
        Guild guild = user.getGuild();
        if((arguments[0].equalsIgnoreCase("options") || arguments[0].equalsIgnoreCase("o"))){
            if(arguments.length > 1) {
                for (GuildOptionHandler handler : OPTION_HANDLERS) {
                    if (arguments[1].equalsIgnoreCase(handler.getKeyword())) {
                        if (arguments.length > 2) {
                            handler.setValue(guild, player, String.join(" ", Arrays.copyOfRange(arguments, 2, arguments.length)));
                        } else {
                            handler.displayValue(guild, player);
                        }
                        return;
                    }
                }
            }
            ChatUtils.send(player, "&b/g options tax &e<&6amount&e> &8- &7Set the daily tax charged to your guild members &8(&b" + guild.getGuildOptions().getTax() + "&8)");
            ChatUtils.send(player, "&b/g options explosions &e<&aon&8/&coff&e> &8- &7Toggle explosions within your town &8(" + onOff(guild.getGuildOptions().isExplosionsEnabled()) + "&8)");
            ChatUtils.send(player, "&b/g options symbol &8- &7Shows a list of symbols you can select from for your town.");
            ChatUtils.send(player, "&b/g options symbol &e<&6symbol&e> &8- &7Select a symbol for your town. &8(&6" + guild.getGuildOptions().getGuildSymbol().getSymbol() + "&8)");
            ChatUtils.send(player, "&b/g options taxkick &e<&aon&8/&coff&e> &8- &7Automatically kick players who fail to pay their taxes. &8(" + onOff(guild.getGuildOptions().isKickMembersWhoDontPayTax()) + "&8)");
            ChatUtils.send(player, "&b/g options pvp &e<&aon&8/&coff&e> &8- &7Toggle pvp within the guild &8(" + onOff(guild.getGuildOptions().isPvpEnabled()) + "&8)");
            ChatUtils.send(player, "&b/g options noticeboard &e<&6message&e> &8- &7Set a message on the guild noticeboard");
            ChatUtils.send(player, "&b/g options tag &e<&6tag&e> &8- &7Change the guild tag &8(&6" + guild.getGuildOptions().getGuildTag() + "&8)");
            ChatUtils.send(player, "&b/g options public &e<&aon&8/&coff&e> &8- &7Allow anyone to join your guild. &8(" + onOff(guild.getGuildOptions().isPublic()) + "&8)");
            ChatUtils.send(player, "&b/g options rename &e<&6new name&e> &8- &7Rename your Guild. &8(&6" + guild.getGuildName() + "&8)");
        }
    }

    @Override
    public GuildPermission getPermission() {
        return GuildPermission.OPTIONS;
    }

    //Taxes - Requires Tax Editing Permission





}
