package dev.snowcave.guilds.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.snowcave.guilds.config.DefaultRoles;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.guildhalls.GuildHall;
import dev.snowcave.guilds.core.users.Role;
import dev.snowcave.guilds.utils.ChunkUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import dev.snowcave.guilds.core.users.User;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by WinterBear on 28/09/2020.
 */
public class Guild {

    private String guildName;

    private User leader;

    private List<User> members = new ArrayList<>();

    private int level = 1;

    private ChunkStore chunks = new ChunkStore();

    private String spawn;

    private Double balance = 0.0;

    private GuildOptions guildOptions;

    private List<Role> roles;

    private GuildHall guildHall;

    public Guild() {

    }

    //Create new guild
    public Guild(String guildName, User leader) {
        this.guildName = guildName;
        this.leader = leader;
        this.members.add(leader);
        this.guildOptions = new GuildOptions(this);
        this.roles = DefaultRoles.getDefaultRoles();
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public User getLeader() {
        return leader;
    }

    public Optional<User> getMember(Player player){
        return members.stream()
                .filter(u -> u.getUuid().equals(player.getUniqueId()))
                .findFirst();
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
        members.forEach(m -> m.setGuild(this));
    }

    @JsonIgnore
    public Level getLevel() {
        return Levels.get(level);
    }

    public List<GuildBonus> listAllBonuses(){
        return Levels.getAllGuildBonuses(Levels.get(level));
    }

    public int getRawLevel(){
        return level;
    }

    public void setRawLevel(int level) {
        this.level = level;
    }

    public ChunkStore getChunks() {
        return chunks;
    }

    public ChunkReference claimChunk(Chunk chunk){
        ChunkReference chunkReference = new ChunkReference(chunk);
        this.chunks.addChunk(chunkReference);
        return chunkReference;
    }

    public void unclaimChunk(Chunk chunk){
        chunks.removeChunk(chunk);
        chunks.removeChunk(chunk);
    }

    public String getSpawn(){
        return spawn;
    }

    public void setSpawn(String spawnAsString){
        this.spawn = spawnAsString;
    }

    @JsonIgnore
    public Location getSpawnLocation() {
        String[] spawnArray = spawn.split("\\|");
        return new Location(Bukkit.getWorld(spawnArray[0]), Double.parseDouble(spawnArray[1]), Double.parseDouble(spawnArray[2]), Double.parseDouble(spawnArray[3]));
    }

    @JsonIgnore
    public void setSpawnLocation(Location spawn) {
        this.spawn = spawn.getWorld().getName() + "|" + spawn.getX() + "|" + spawn.getY() + "|" + spawn.getZ();
    }

    public boolean hasMember(Player player){
        return members.stream().anyMatch(u -> u.getUuid().equals(player.getUniqueId()));
    }

    @JsonIgnore
    public boolean isPublic() {
        return guildOptions.isPublic();
    }

    public void broadcast(String message){
        for(User user : members){
            Player player = Bukkit.getPlayer(user.getUuid());
            if(player != null && player.isOnline()){
                ChatUtils.send(player, ChatUtils.format(message));
            }
        }
    }

    public void deposit(Double amount){
        this.balance += amount;
    }

    public void withdraw(Double amount){
        this.balance -= amount;
    }

    public Double getBalance() {
        return balance;
    }

    public GuildOptions getGuildOptions() {
        return guildOptions;
    }

    public void setGuildOptions(GuildOptions guildOptions) {
        this.guildOptions = guildOptions;
    }

    public GuildHall getGuildHall() {
        return guildHall;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public Optional<Role> getRole(String roleReference){
        return roles.stream()
                .filter(r -> r.getTitle().equalsIgnoreCase(roleReference))
                .findFirst();
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setGuildHall(GuildHall guildHall) {
        if(guildHall != null){
            this.guildHall = guildHall;
            guildHall.setGuild(this);
        }
    }
}
