package dev.snowcave.guilds.commands.alliances;

import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.alliances.Alliance;
import dev.snowcave.guilds.core.alliances.AllianceType;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.EconomyUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by WinterBear on 16/11/2023.
 */
public class AllianceController {

    private static final Map<String, Set<AllianceInvite>> INVITES = new HashMap<>();

    public static void sendInvite(Alliance alliance, String inviteGuild){
        INVITES.putIfAbsent(inviteGuild, new HashSet<>());
        INVITES.get(inviteGuild).add(new AllianceInvite(alliance.getName(), inviteGuild));
        alliance.broadcast("The Guild {} has been invited to join {}.", inviteGuild, alliance.getName());
    }




    @NotNull
    public static Set<AllianceInvite> getAllianceInvites(Guild guild){
        return INVITES.getOrDefault(guild.getGuildName(), Collections.emptySet());
    }

    @NotNull
    public static Optional<AllianceInvite> getAllianceInvite(User user, String joinAlliance) {
        return INVITES.get(user.getGuild().getGuildName()).stream()
                    .filter(i -> i.getAlliance().equalsIgnoreCase(joinAlliance))
                    .findFirst();
    }

    public static void joinAlliance(Guild guild, String joinAlliance){
        Guilds.ALLIANCES.get(joinAlliance).addMember(guild);
        guild.setAlliance(joinAlliance);
        Chatter.broadcastP("&7The Guild &b" + ChatColor.of(guild.getGuildOptions().getColor()) + guild.getGuildName() + " &3has joined the " + Guilds.ALLIANCES.get(joinAlliance).getAllianceType().getDisplayName() + " " + joinAlliance);
    }

    public static boolean allianceExists(String allianceName){
        return Guilds.ALLIANCES.containsKey(allianceName);
    }

    public static void deposit(String playerName, Alliance alliance, Double amount){
        alliance.addToBalance(amount);
        alliance.broadcast("&b" + playerName + " &3deposited &6" + amount + " &3into the alliance bank.");
    }


    public static void createAlliance(String allianceName, AllianceType allianceType, Guild founderGuild){
        Alliance alliance = new Alliance();
        alliance.setName(allianceName);
        alliance.addMember(founderGuild);
        alliance.addLeader(founderGuild.getLeader());
        alliance.setAllianceType(allianceType);
        Guilds.ALLIANCES.put(allianceName, alliance);
        Chatter.broadcastP("&7The Guild &b" + founderGuild.getGuildName() + " &7has formed the " + allianceType.getDisplayName() + " of " + allianceName);
        founderGuild.setAlliance(allianceName);
    }

    public static void removeAlliance(Alliance alliance){
        Guilds.ALLIANCES.remove(alliance.getName());
        if(!alliance.getLeaders().isEmpty()){
            double balance = alliance.getBalance() / alliance.getLeaders().size();
            alliance.getLeaders().forEach(l -> {
                EconomyUtils.ECONOMY.depositPlayer(Bukkit.getOfflinePlayer(l), balance);
            });
        }
        alliance.getMembers().forEach(member -> Guilds.getGuild(member)
                .ifPresent(g -> g.setAlliance(null)));
        Chatter.broadcastP("&7The " + alliance.getName() + " " + alliance.getAllianceType().getDisplayName() + " &7has fallen.");
    }

    public static void removeGuild(Guild guild){
        if(Guilds.ALLIANCES.containsKey(guild.getAlliance())){
            Alliance alliance = Guilds.ALLIANCES.get(guild.getAlliance());
            alliance.getMembers().remove(guild.getGuildName());
            for(User member : guild.getMembers()){
                alliance.getAssistants().remove(member.getUuid());
                if (alliance.getLeaders().contains(member.getUuid())){
                    if(alliance.getLeaders().size() == 1){
                        removeAlliance(alliance);
                    }
                    alliance.getLeaders().remove(member.getUuid());
                    Chatter.broadcastP("&7The Guild &b" + guild.getGuildName() + " &7has left the " + alliance.getAllianceType().getDisplayName() + " of " + alliance.getName());
                }
            }
        }
        guild.setAlliance(null);
    }

    public static boolean canPromote(User user, Alliance alliance){
        return canPromoteToAllianceLeader(user, alliance) || canPromoteToAssistant(user, alliance);
    }

    private static boolean canPromoteToAllianceLeader(User user, Alliance alliance){
        return alliance.getAllianceType() != AllianceType.IMPERIAL && !alliance.getLeaders().contains(user.getUuid())  && user.getGuild().getLeader().getUuid().equals(user.getUuid());
    }

    private static boolean canPromoteToAssistant(User user, Alliance alliance){
        return !alliance.getLeaders().contains(user.getUuid()) && !alliance.getAssistants().contains(user.getUuid());
    }

    //Rules of promotion
    //In a democracy, only those who are not guild-leaders can be promoted to aides
    //In a grand council, guild leaders can be promoted to councillors, and those who are not guild leaders can be promoted to ambassadors
    //In an empire, only the emperor can promote anyone to Vizier

    @NotNull
    public static Optional<String> promote(User user, Alliance alliance){
        if(alliance.getLeaders().contains(user.getUuid())) {
            return Optional.of("&cThat player is already a leader.");
        } else if (alliance.getAllianceType().equals(AllianceType.GRAND_COUNCIL) && user.getGuild().getLeader().getUuid().equals(user.getUuid())){
            alliance.getLeaders().add(user.getUuid());
            alliance.broadcast("&b" + user.getName() + " &3was promoted to " + alliance.getAllianceType().getLeaderTitle() + ".");
            return Optional.empty();
        } else if (!alliance.getAssistants().contains(user.getUuid())){
            //promote them to assistant
            alliance.getAssistants().add(user.getUuid());
            alliance.broadcast("&b" + user.getName() + " &3was promoted to " + alliance.getAllianceType().getAssistantTitle() + ".");
            return Optional.empty();
        } else {
            return Optional.of("&cThat player is already an assistant.");
        }

    }

    public static Optional<String> demote(User user, Alliance alliance){
        if(alliance.getAssistants().contains(user.getUuid())){
            alliance.getAssistants().remove(user.getUuid());
            alliance.broadcast("&b" + user.getName() + " &3was demoted from " + alliance.getAllianceType().getAssistantTitle() + ".");
            return Optional.empty();
        } else if (alliance.getLeaders().contains(user.getUuid())){
            alliance.getLeaders().remove(user.getUuid());
            alliance.broadcast("&b" + user.getName() + " &3was demoted from " + alliance.getAllianceType().getLeaderTitle() + ".");
            if(alliance.getLeaders().isEmpty()){
                removeAlliance(alliance);
            }
            return Optional.empty();
        } else {
            return Optional.of("&cThat player is not an assistant or leader.");
        }
    }


}
