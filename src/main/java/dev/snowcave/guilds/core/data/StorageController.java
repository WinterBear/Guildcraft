package dev.snowcave.guilds.core.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.Guild;
import dev.snowcave.guilds.utils.RepeatingTask;
import dev.snowcave.guilds.utils.RepeatingTaskUtils;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
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
        RepeatingTaskUtils.everyMinutes(20, new RepeatingTask("Guild Save and Backup Task", () -> StorageController.save(plugin)), plugin);
    }

    private static Path getGuildDir(JavaPlugin plugin){
        return Paths.get(plugin.getDataFolder() + "/guilds/");
    }

    private static Path getBackupDir(JavaPlugin plugin){
        return Paths.get(plugin.getDataFolder() + "/backups/");
    }

    public static boolean save(JavaPlugin plugin) {
        ObjectWriter ow = MAPPER.writer().withDefaultPrettyPrinter();
        Path path = getGuildDir(plugin);
        Path backup = getBackupDir(plugin);
        try {
            Files.createDirectories(path);
            Files.createDirectories(backup);

            String backupTimestamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            Path todayBackup = Paths.get(plugin.getDataFolder() + "/backups/" + backupTimestamp);

            if (!Files.exists(todayBackup)) {
                Files.createDirectories(todayBackup);
                try(Stream<Path> files = Files.list(path)){
                    files.forEach(f -> backup(todayBackup, f));
                } catch (Exception e){
                    Bukkit.getLogger().log(Level.SEVERE, "An exception occurred while backing up guilds.", e);
                }
            }

            try(Stream<Path> files = Files.list(path)){
                files.map(Path::toFile)
                        .filter(f -> !f.isDirectory())
                        .forEach(File::delete);
            } catch (Exception e){
                Bukkit.getLogger().log(Level.SEVERE, "An exception occurred while saving guilds.", e);
            }


            Guilds.GUILDS.forEach(g -> saveGuild(g, ow, plugin.getDataFolder() + "/guilds/"));
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error writing to disk: ", e);
        }
        return true;
    }

    private static void saveGuild(Guild guild, ObjectWriter ow, String path) {
        try {
            String json = ow.writeValueAsString(guild);
            Files.write(Paths.get(path + guild.getGuildName() + ".json"), json.getBytes());
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error saving the guild " + guild.getGuildName(), e);
        }
    }

    public static void backup(Path backupDir, Path filePath) {
        ChatUtils.info("Backing up " + filePath + " to " + backupDir);
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
            Files.list(Paths.get(plugin.getDataFolder() + "/guilds/"))
                    .forEach(StorageController::loadGuild);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error loading guilds.", e);
        }
    }

    private static void loadGuild(Path filePath) {

        ChatUtils.info("Loading Guild " + filePath);
        try {
            String json = Files.lines(filePath).collect(Collectors.joining());
            Guild guild = MAPPER.readValue(json, Guild.class);
            Guilds.GUILDS.add(guild);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "There was an unexpected error loading the guild from " + filePath, e);
        }


    }


}
