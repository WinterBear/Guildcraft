package dev.snowcave.guilds.core.alliances;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.users.User;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by WinterBear on 20/12/2020.
 */
public class Alliance {

    private String name;

    private AllianceType allianceType;

    private Set<Guild> members = new HashSet<>();

    private Set<UUID> leaders = new HashSet<>();

    private Set<UUID> assistants = new HashSet<>();

    private Double balance = 0.0;

    private Double unity = 0.0;

    public void broadcast(String message, Object... args){
        String formattedMessage = String.format(message.replace("{}", "%s"), args);
        for (User user : members.stream().map(Guild::getMembers)
                .flatMap(Collection::stream)
                .toList()) {
            Player player = Bukkit.getPlayer(user.getUuid());
            if (player != null && player.isOnline()) {
                ChatUtils.send(player, ChatColor.of("#d13824") + ChatUtils.format(formattedMessage));
            }
        }
    }

    public void addMember(Guild member){
        members.add(member);
    }

    public void addLeader(User leader){
        leaders.add(leader.getUuid());
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public Set<Guild> getGuildMembers() {
        return members;
    }

    public List<String> getMembers(){
        return members.stream().map(Guild::getGuildName).collect(Collectors.toList());
    }

    public Set<UUID> getLeaders() {
        return leaders;
    }

    public Set<UUID> getAssistants(){
        return assistants;
    }

    public Set<UUID> getAssistantsAndLeaders(){
        return Stream.concat(leaders.stream(), assistants.stream()).collect(Collectors.toSet());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(List<String> members) {
        this.members = members.stream().map(Guilds::getGuild)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public void setLeaders(Set<UUID> leaders) {
        this.leaders = leaders;
    }

    public void setAssistants(Set<UUID> assistants) {
        this.assistants = assistants;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void addToBalance(Double addition){
        this.balance = this.balance + addition;
    }

    public void subtractFromBalance(Double subtraction){
        this.balance = this.balance - subtraction;
    }

    public AllianceType getAllianceType() {
        return allianceType;
    }

    public void setAllianceType(AllianceType allianceType) {
        this.allianceType = allianceType;
    }

    public Double getUnity() {
        return unity;
    }

    @JsonIgnore
    public String displayUnity(){
        return BigDecimal.valueOf(unity)
                .setScale(3, RoundingMode.HALF_UP)
                .toString();
    }

    public String displayBalance(){
        return BigDecimal.valueOf(balance)
                .setScale(2, RoundingMode.HALF_UP)
                .toString();
    }
}
