package dev.snowcave.guilds.commands.general;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.commands.base.GuildMemberCommandHandler;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.users.User;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class GuildInfoCommandHandler extends GuildMemberCommandHandler {

    @Override
    public List<String> getKeywords() {
        return Arrays.asList("info");
    }

    @Override
    public String describe() {
        return "&b/guild info &8- &7Display information about your Guild";
    }

    @Override
    public void handle(Player player, User user, String[] arguments) {

        if (arguments.length > 1) {
            String guildName = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));
            Optional<Guild> guild = Guilds.getGuild(guildName);
            if (guild.isPresent()) {
                showGuildInfo(player, guild.get(), player.hasPermission("guilds.admin"));
            } else {
                ChatUtils.send(player, "&7Could not find a guild by that name.");
            }
        } else {
            showGuildInfo(player, user.getGuild(), true);
        }


    }

    private void showGuildInfo(Player player, Guild guild, boolean showAll) {
        String symbol = guild.getGuildOptions().getGuildSymbol().getSymbol();
        ChatColor color = ChatColor.of(guild.getGuildOptions().getColor());
        ChatUtils.send(player, ChatUtils.format("&c*&6 --- ₊&3˚&f" + symbol + " " + color + guild.getGuildName() + " &7(&3Lvl. &6" + guild.getLevel() + "&7) &f" + symbol + "&c˚&6₊ --- &3*&r\n"));
        //ChatUtils.send(player, ChatUtils.format("&bGuild&8: &6" + guild.getGuildName() + " &7(&3Lvl. &6" + guild.getLevel() + "&7)"));
        if (showAll && guild.getGuildOptions().getNoticeboard() != null && !guild.getGuildOptions().getNoticeboard().equals("")) {
            ChatUtils.send(player, ChatUtils.format(guild.getGuildOptions().getNoticeboard()));
        }
        ChatUtils.send(player, ChatUtils.format("&3&lGrandmaster&8: " + color + "&l" + guild.getLeader().getName()));
        if (showAll) {
            ChatUtils.send(player, ChatUtils.format("&3Chunk Claims&8: &e" + guild.allChunks().size() + "&8/&6" + guild.getLevel().getMaxChunks()));
        }

        ChatUtils.send(player, ChatUtils.format("&3Members&8: &b" + guild.getMembers().stream().map(User::getName).collect(Collectors.joining("&7, &b")))
                + " &8(&e" + guild.getMembers().size() + "&8/&3" + guild.getLevel().getMaxMembers() + "&8)");
        if (showAll) {
            ChatUtils.send(player, ChatUtils.format("&3Balance&8: &e" + guild.getBalance() + " &b◎"));
        }
        if (showAll && Levels.getAllGuildBonuses(guild.getLevel()).contains(GuildBonus.GUILD_FEASTS)) {
            ChatUtils.send(player, ChatUtils.format("&3Stores&8: &e" + guild.getGuildHall().getFoodStore() + " &6\uD83E\uDD67"));
        }
        if (showAll && Levels.getAllGuildBonuses(guild.getLevel()).contains(GuildBonus.OUTPOSTS_1)) {
            guild.initOutposts();
            ChatUtils.send(player, ChatUtils.format("&3Outposts&8: &e" + guild.getOutposts().size() + "&7/&3" + guild.getMaxOutposts() + " &6\uD83C\uDFC1"));
            for (String outpost : guild.getOutposts().keySet()) {
                ChatUtils.send(player, ChatUtils.format("&7- &3") + outpost + " &8(&a" + guild.getOutposts().get(outpost).getChunkReferences().size() + " chunks claimed&8)");
            }
            guild.initOutposts();
        }
        if (showAll) {
            ChatUtils.send(player, "");
            if (!Levels.getAllGuildBonuses(guild.getLevel()).isEmpty()) {
                TextComponent text = new TextComponent(ChatUtils.format("&6Bonuses&8: &e"));
                for (GuildBonus bonus : Levels.getAllGuildBonuses(guild.getLevel())) {
                    text.addExtra(coloredTextComponent(ChatColor.DARK_GRAY, "["));
                    text.addExtra(bonusDisplay(ChatColor.of("#52a8ff"), ChatUtils.format(bonus.getSymbol()), ChatUtils.format(bonus.toString())));
                    text.addExtra(coloredTextComponent(ChatColor.DARK_GRAY, "] "));
                }
                player.spigot().sendMessage(text);
            }
        }

    }

    private TextComponent coloredTextComponent(ChatColor color, String text) {
        TextComponent message = new TextComponent(text);
        message.setColor(color);
        return message;
    }

    private TextComponent bonusDisplay(ChatColor color, String text, String hover) {
        TextComponent message = new TextComponent(text);
        message.setColor(color);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        return message;
    }


}
