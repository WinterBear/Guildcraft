package dev.snowcave.guilds.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.snowcave.guilds.config.DefaultRoles;
import dev.snowcave.guilds.config.Levels;
import dev.snowcave.guilds.core.guildhalls.GuildHall;
import dev.snowcave.guilds.core.users.Role;
import dev.snowcave.guilds.core.users.User;
import dev.snowcave.guilds.utils.Chatter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by WinterBear on 28/09/2020.
 */
public class Guild {

    private String guildName;

    private User leader;

    private List<User> members = new ArrayList<>();

    private Map<String, String> allies = new HashMap<>();

    private int level = 1;

    private final ChunkStore chunks = new ChunkStore();

    private String spawn;

    private Double balance = 0.0;

    private GuildOptions guildOptions;

    private List<Role> roles;

    private Map<String, Outpost> outposts = new HashMap<>();

    private GuildHall guildHall;

    private String alliance;

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


    public Optional<User> getMember(Player player) {
        return members.stream()
                .filter(u -> u.getUuid().equals(player.getUniqueId()))
                .findFirst();
    }

    public Optional<User> getMember(UUID uuid) {
        return members.stream()
                .filter(u -> u.getUuid().equals(uuid))
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

    public List<GuildBonus> listAllBonuses() {
        return Levels.getAllGuildBonuses(Levels.get(level));
    }

    public int getRawLevel() {
        return level;
    }

    public void setRawLevel(int level) {
        this.level = level;
    }

    public ChunkStore getChunks() {
        return chunks;
    }

    @JsonIgnore
    public Optional<ChunkReference> getChunkRef(Chunk chunk){
        Optional<ChunkReference> chunkRef = chunks.get(chunk);
        if (!chunkRef.isPresent()){
            for (ChunkStore outpost : outposts.values()){
                chunkRef = outpost.get(chunk);
                if(chunkRef.isPresent()){
                    return chunkRef;
                }
            }
        }
        return chunkRef;
    }

    public List<ChunkReference> allChunks() {
        List<ChunkReference> refs = new ArrayList<>(chunks.getChunkReferences());
        outposts.keySet().forEach(o -> refs.addAll(outposts.get(o).getChunkReferences()));
        return refs;
    }

    public int claimCount() {
        return allChunks().size();
    }

    public double storeModifier() {
        List<GuildBonus> bonuses = Levels.getAllGuildBonuses(getLevel());
        if (bonuses.contains(GuildBonus.BOOST_FOOD_4)) {
            return 1;
        } else if (bonuses.contains(GuildBonus.BOOST_FOOD_3)) {
            return 0.9;
        } else if (bonuses.contains(GuildBonus.BOOST_FOOD_2)) {
            return 0.8;
        } else if (bonuses.contains(GuildBonus.BOOST_FOOD)) {
            return 0.7;
        } else {
            return 0.6;
        }
    }

    public ChunkReference claimChunk(Chunk chunk) {
        ChunkReference chunkReference = new ChunkReference(chunk);
        this.chunks.addChunk(chunkReference);
        return chunkReference;
    }

    public ChunkReference createNewOutpost(Location tp, String outpostRef, Chunk spawnChunk) {
        ChunkReference chunkReference = new ChunkReference(spawnChunk);
        if (!outposts.containsKey(outpostRef)) {
            outposts.put(outpostRef, new Outpost());
            outposts.get(outpostRef).addChunk(chunkReference);
            outposts.get(outpostRef).setWarppointLocation(tp);
            return chunkReference;
        } else {
            return null;
        }
    }

    public ChunkReference claimOutpostChunk(String outpostRef, Chunk chunk) {
        ChunkReference chunkReference = new ChunkReference(chunk);
        if (outposts.containsKey(outpostRef)) {
            this.outposts.get(outpostRef).addChunk(chunkReference);
            return chunkReference;
        }
        return null;
    }

    public void unclaimChunk(Chunk chunk) {
        ChunkReference chunkReference = new ChunkReference(chunk);
        for (String outpost : outposts.keySet()) {
            if (outposts.get(outpost).getChunkReferences().stream().anyMatch(chunkReference::equals)) {
                outposts.get(outpost).removeChunk(chunk);
                if (outposts.get(outpost).getChunkReferences().isEmpty()) {
                    outposts.remove(outpost);
                }
            }
        }
        chunks.removeChunk(chunk);
        chunks.removeChunk(chunk);
    }

    public String getSpawn() {
        return spawn;
    }

    public void setSpawn(String spawnAsString) {
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

    public boolean hasMember(Player player) {
        return members.stream().anyMatch(u -> u.getUuid().equals(player.getUniqueId()));
    }

    public boolean hasMember(UUID uuid) {
        return members.stream().anyMatch(u -> u.getUuid().equals(uuid));
    }

    @JsonIgnore
    public boolean isPublic() {
        return guildOptions.isPublic();
    }

    public void broadcast(String message) {
        for (User user : members) {
            Player player = Bukkit.getPlayer(user.getUuid());
            if (player != null && player.isOnline()) {
                Chatter.send(player, message);
            }
        }
    }

    public void deposit(Double amount) {
        this.balance += amount;
    }

    public void withdraw(Double amount) {
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

    public Optional<Role> getRole(String roleReference) {
        return roles.stream()
                .filter(r -> r.getTitle().equalsIgnoreCase(roleReference))
                .findFirst();
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setGuildHall(GuildHall guildHall) {
        if (guildHall != null) {
            this.guildHall = guildHall;
            guildHall.setGuild(this);
        }
    }

    public void initOutposts() {
        if (this.outposts == null) {
            this.outposts = new HashMap<>();
        }
    }

    public Map<String, Outpost> getOutposts() {
        return outposts;
    }

    @JsonIgnore
    public int getMaxOutposts() {
        List<GuildBonus> bonuses = Levels.getAllGuildBonuses(getLevel());
        if (bonuses.contains(GuildBonus.OUTPOSTS_4)) {
            return 5;
        } else if (bonuses.contains(GuildBonus.OUTPOSTS_3)) {
            return 4;
        } else if (bonuses.contains(GuildBonus.OUTPOSTS_2)) {
            return 3;
        } else if (bonuses.contains(GuildBonus.OUTPOSTS_1)) {
            return 2;
        } else {
            return 0;
        }
    }

    public void setOutposts(Map<String, Outpost> outposts) {
        this.outposts = outposts;
    }

    public boolean playerIsAlly(Player player){
        return allies.containsKey(player.getUniqueId().toString());
    }

    public boolean playerIsAlly(OfflinePlayer player){
        return allies.containsKey(player.getUniqueId().toString());
    }

    public void addAlly(String name, UUID uuid){
        allies.put(uuid.toString(), name);
    }

    public void removeAlly(UUID uuid){
        allies.remove(uuid.toString());
    }

    public Map<String, String> getAllies() {
        return allies;
    }

    public String getAlliance() {
        return alliance;
    }

    public void setAlliance(String alliance) {
        this.alliance = alliance;
    }
}
