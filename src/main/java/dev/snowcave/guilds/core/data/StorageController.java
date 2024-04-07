package dev.snowcave.guilds.core.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.core.alliances.Alliance;
import dev.snowcave.guilds.utils.Chatter;
import dev.snowcave.guilds.utils.RepeatingTask;
import dev.snowcave.guilds.utils.RepeatingTaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by WinterBear on 05/01/2021.
 */
public class StorageController {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void start(JavaPlugin plugin) {
        RepeatingTaskUtils.everyMinutes(60, new RepeatingTask("Guild Save and Backup Task", () -> StorageController.save(plugin)), plugin);
    }

    private static Path getGuildDir(JavaPlugin plugin){
        return Paths.get(plugin.getDataFolder() + "/guilds/");
    }

    private static Path getAllianceDir(JavaPlugin plugin){
        return Paths.get(plugin.getDataFolder() + "/alliances/");
    }

    private static Path getBackupDir(JavaPlugin plugin){
        return Paths.get(plugin.getDataFolder() + "/backups/");
    }


    public static boolean save(JavaPlugin plugin) {
        ObjectWriter ow = MAPPER.writer().withDefaultPrettyPrinter();
        Path guildDir = getGuildDir(plugin);
        Path backupDir = getBackupDir(plugin);
        Path allianceDir = getAllianceDir(plugin);
        try {
            Files.createDirectories(guildDir);
            Files.createDirectories(backupDir);
            Files.createDirectories(allianceDir);

            String backupTimestamp = new SimpleDateFormat("dd-MM-yyyy-HH-mm").format(new Date());

            Path todayBackupGuildsDir = Paths.get(plugin.getDataFolder() + "/backups/" + backupTimestamp + "/guilds");
            Path todayBackupAlliancesDir = Paths.get(plugin.getDataFolder() + "/backups/" + backupTimestamp + "/alliances");

            if (tryBackup(guildDir, todayBackupGuildsDir)){
                clearFolder(guildDir);
            }
            Guilds.GUILDS.forEach(g -> saveGuild(g, ow, getGuildDir(plugin)));
            if (tryBackup(allianceDir, todayBackupAlliancesDir)){
                clearFolder(allianceDir);
            }
            Guilds.ALLIANCES.values().forEach(a -> saveAlliance(a, ow, getAllianceDir(plugin)));

        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error writing to disk: ", e);
        }
        return true;
    }

    private static boolean tryBackup(Path fromDir, Path toDir){
        if (!Files.exists(toDir)) {
            try {
                moveFolderContents(fromDir, toDir);
                return true;
            } catch (StorageControllerIOException e){
                Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error backing up the " + fromDir +" Folder: ", e);
            }
        }
        return false;
    }

    private static void moveFolderContents(Path from, Path to) throws StorageControllerIOException {
        try {
            if (!Files.exists(to)) {
                Files.createDirectories(to);
                try (Stream<Path> files = Files.list(from)) {
                    files.forEach(f -> backup(to, f));
                }
            }
        } catch (Exception e){
            throw new StorageControllerIOException("An exception occurred while moving folder contents.", e);
        }
    }

    private static void clearFolder(Path path) throws StorageControllerIOException {
        try(Stream<Path> files = Files.list(path)){
            files.map(Path::toFile)
                    .filter(f -> !f.isDirectory())
                    .forEach(File::delete);
        } catch (Exception e){
            throw new StorageControllerIOException("An exception occurred while wiping the folder " + path, e);
        }
    }

    private static void saveGuild(Guild guild, ObjectWriter ow, Path baseDir) {
        try {
            String json = ow.writeValueAsString(guild);
            Files.write(baseDir.resolve(guild.getGuildName() + ".json"), json.getBytes());
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error saving the guild " + guild.getGuildName(), e);
        }
    }

    private static void saveAlliance(Alliance alliance, ObjectWriter ow, Path baseDir){
        try {
            String json = ow.writeValueAsString(alliance);
            Files.write(baseDir.resolve(alliance.getName() + ".json"), json.getBytes());
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error saving the guild " + alliance.getName(), e);
        }
    }

    public static void backup(Path backupDir, Path filePath) {
        Chatter.infoConsole("Backing up " + filePath + " to " + backupDir);
        try {
            Path destination = Paths.get(backupDir.toString(), filePath.getFileName().toString());
            Files.move(filePath, destination);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error backing up the file " + filePath, e);
        }

    }

    public static void load(JavaPlugin plugin) {
        Path path = getGuildDir(plugin);
        try {
            Files.createDirectories(path);
            try(Stream<Path> guildJsonFiles = Files.list(getGuildDir(plugin)) ){
                guildJsonFiles.forEach(StorageController::loadGuild);
            }
            try(Stream<Path> allianceJsonFiles = Files.list(getAllianceDir(plugin)) ){
                allianceJsonFiles.forEach(StorageController::loadAlliance);
            }
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error loading guilds.", e);
        }
    }

    private static void loadGuild(Path filePath) {

        Chatter.infoConsole("Loading Guild " + filePath);
        try {
            String json = Files.lines(filePath).collect(Collectors.joining());
            Guild guild = MAPPER.readValue(json, Guild.class);
            Guilds.GUILDS.add(guild);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error loading the guild from " + filePath, e);
        }
    }

    private static void loadAlliance(Path filePath) {

        Chatter.infoConsole("Loading Alliance " + filePath);
        try {
            String json = Files.lines(filePath).collect(Collectors.joining());
            Alliance alliance = MAPPER.readValue(json, Alliance.class);
            Guilds.ALLIANCES.put(alliance.getName(), alliance);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error loading the alliance from " + filePath, e);
        }
    }


}
