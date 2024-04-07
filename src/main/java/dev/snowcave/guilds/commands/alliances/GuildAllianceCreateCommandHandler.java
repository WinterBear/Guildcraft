package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.commands.GuildCommand;
import dev.snowcave.guilds.commands.base.GuildCommandHandler;
import dev.snowcave.guilds.core.GuildBonus;
import dev.snowcave.guilds.core.alliances.AllianceType;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.CommandSenderUtils;
import dev.snowcave.guilds.utils.CommandUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by WinterBear on 11/11/2023.
 */
public class GuildAllianceCreateCommandHandler implements GuildCommandHandler {

    @Override
    public void handle(Player player, String[] argument) {
        GuildCommand.asGuildUser(this::create, player, argument);
    }

    private void create(Player player, User user, String[] arguments){
        Chatter chatter = new Chatter(player);

        if(user.getGuild().getAlliance() != null){
            chatter.error("&cYou are already part of the Alliance &6" + user.getGuild().getAlliance());;
        } else {
            String allianceName = CommandUtils.squashArgs(2, arguments);
            if (allianceName.contains(" -t ")){
                String[] split = allianceName.split(" -t ");
                String allianceType = split[1];
                allianceName = split[0];
                try {
                    AllianceController.createAlliance(allianceName, AllianceType.valueOf(allianceType), user.getGuild());
                } catch (IllegalArgumentException e){
                    chatter.error(ChatUtils.format("&cInvalid Alliance Type &6" + allianceType));
                }

            } else if(AllianceController.allianceExists(allianceName)){
                chatter.error(ChatUtils.format("&cAn Alliance already exists with the name &6" + user.getGuild().getAlliance()));
            } else if (!allianceName.isEmpty()) {
                sendAllianceTreatySelection(chatter, allianceName);
            } else {
                Chatter.send(player, describe());
            }
        }
    }

    private void sendAllianceTreatySelection(Chatter chatter, String allianceName){
        chatter.blank();
        chatter.send( " " + ChatColor.of("#7d2522") + "&m          " + ChatColor.of("#7d2522") + " *.°&7 " + ChatColor.of("#d13824") + "Guild Alliance Declaration&8: " + ChatColor.of("#1c7ab0") + allianceName + ChatColor.of("#7d2522") + " °.* &m             &7");
        chatter.blank();
        chatter.send(" &7Before your Alliance can be formed, you must choose how power will be distributed amongst its members. Click to select a Treaty from the options below.");
        chatter.blank();
        chatter.send(treaties(allianceName));
        chatter.blank();
        chatter.blank();
        chatter.send(" " + ChatColor.of("#7d2522") + "&m                                                                               &7");
    }

    private Component treaties(String allianceName) {
        final String input = "     <democracy><#80e8a8>\uD83D\uDFE2<white>Democracy<#80e8a8>\uD83E\uDD1D</democracy>   <gray>|   <grandcouncil><#ffa724>\uD83D\uDFE3<white>Grand Council<#ffa724>\uD83C\uDFDB</grandcouncil>   <gray>|   <empire><#bf2e2e>\uD83D\uDD34<white>Empire<#bf2e2e>\uD83D\uDC51</empire>";
        final MiniMessage extendedInstance = MiniMessage.builder()
                .tags(
                        TagResolver.resolver(
                                StandardTags.color(),
                                democracyTag(allianceName),
                                grandCouncilTag(allianceName),
                                empireTag(allianceName)))
                .build();

        return extendedInstance.deserialize(input);
    }

    static TagResolver democracyTag(String allianceName) {
        Component text = MiniMessage.miniMessage().deserialize(" <#80e8a8>\uD83D\uDFE2<white>Democracy<#80e8a8>\uD83E\uDD1D<newline>" +
                "<#80e8a8>Leadership<dark_gray>: <#00a148>Alliance Senate <#80e8a8>- <gray>Power is spread equally among all guilds in your Alliance. " +
                "Any time a Guild is invited to the Alliance, its Grandmaster joins the Alliance Senate. " +
                "Certain actions can only be done by an Alliance Senate majority vote, such as<dark_gray>:<newline>" +
                "       <#80e8a8>- <#1c7ab0>Changing the Alliance Treaty<newline>" +
                "       <#80e8a8>- <#1c7ab0>Kicking a Guild<newline>" +
                "       <#80e8a8>- <#1c7ab0>Establishing a Capital<newline>" +
                "<gray>It is not possible to disband the Alliance unless you are the last Guild remaining.<newline>" +
                "<newline>" +
                "<#80e8a8>Assistants<dark_gray>: <#00a148>Aides <#80e8a8>- <gray>Members of the Alliance Senate can promote members of their Guild to perform Alliance actions.<newline>" +
                "<newline>" +
                "<#1c7ab0>Capital<dark_gray>: <gray>You will be able to construct a <#00a148>Democratic Capital<gray>. You can choose an existing Guild as your Capital, or establish a new one independent of the Guilds.");
        return TagResolver.resolver("democracy", Tag.styling(
                ClickEvent.runCommand("/g a create " + allianceName + " -t DEMOCRACY"),
                HoverEvent.showText(text))
        );
    }

    static TagResolver grandCouncilTag(String allianceName) {
        Component text = MiniMessage.miniMessage().deserialize(
                " <#ffa724>\uD83D\uDFE3<white>Grand Council<#ffa724>\uD83C\uDFDB<newline>" +
                "<#ffa724>Leadership<dark_gray>: <#ffd000>Grand Council <#ffa724>- <gray>Power is kept within a Grand Council of Guild Masters in your Alliance. " +
                "Grandmasters become Alliance Councillors by being promoted by existing members of the Council. " +
                "Certain actions can only be done by a Council majority vote:<newline>" +
                "       <#ffa724>- <#1c7ab0>Changing the Alliance Treaty<newline>" +
                "       <#ffa724>- <#1c7ab0>Kicking a Councillor's Guild<newline>" +
                "       <#ffa724>- <#1c7ab0>Establishing a Capital<newline>" +
                "<newline>" +
                "<#ffa724>Assistants<dark_gray>: <#ffd000>Ambassadors <#ffa724>- <gray>Members of the Grand Council can promote Alliance members to status of Ambassador to allow them to manage Alliance resources.<newline>" +
                "<newline>" +
                "<#1c7ab0>Capital<dark_gray>: <gray>You will be able to construct a <#ffd000>Grand Council Center<gray>. You can choose an existing Guild as your Capital, or establish a new one independent of the Guilds.");
        return TagResolver.resolver("grandcouncil", Tag.styling(
                ClickEvent.runCommand("/g a create " + allianceName + " -t GRAND_COUNCIL"),
                HoverEvent.showText(text))
        );
    }

    static TagResolver empireTag(String allianceName) {
        Component text = MiniMessage.miniMessage().deserialize(" <#bf2e2e>\uD83D\uDD34<white>Empire<#bf2e2e>\uD83D\uDC51<newline>" +
                "<#bf2e2e>Leadership<dark_gray>: <#d61818>Emperor <#bf2e2e>- <gray>Power is granted only to the Grandmaster of the Founding Guild, who becomes the Emperor. Guild Grandmasters get no extra rights, and the Alliance Treaty can only be redefined by the Emperor, or by a Successful Coup.<newline>" +
                "<newline>" +
                "<#bf2e2e>Assistants<dark_gray>: <#d61818>Viziers <#bf2e2e>- <gray>Guild members can be promoted to Viziers by the Emperor. Viziers can manage Alliance affairs but do not hold true power.<newline>" +
                "<newline>" +
                "<#1c7ab0>Capital<dark_gray>: <gray>You will be able to construct an <#d61818>Imperial Throne<gray>. You can choose an existing Guild as your Capital, or establish a new one independent of the Guilds.");
        return TagResolver.resolver("empire", Tag.styling(

                ClickEvent.runCommand("/g a create " + allianceName + " -t IMPERIAL"),
                HoverEvent.showText(text))
        );
    }

    @Override
    public List<String> getKeywords() {
        return List.of("create");
    }

    @Override
    public @NotNull String describe() {
        return GuildAllianceCommandHandler.allianceCommandDescription("create",
                "Create a new Alliance",
                "Alliance Name");
    }

    public boolean canUse(CommandSender player) {
        return CommandSenderUtils.isGuildLeader(player) &&
                CommandSenderUtils.guildHasBonus(player, GuildBonus.ALLIANCES);
    }



}
