package dev.snowcave.guilds.core.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.snowcave.guilds.Guilds;
import dev.snowcave.guilds.core.Guild;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by WinterBear on 05/01/2021.
 */
public class StorageController {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void save(JavaPlugin plugin){
        ObjectWriter ow = MAPPER.writer().withDefaultPrettyPrinter();
        Path path = Paths.get(plugin.getDataFolder()+ "/guilds/");
        Path backup = Paths.get(plugin.getDataFolder()+ "/guilds/backups/");
        try {
            Files.createDirectories(path);
            Files.createDirectories(backup);

            String backupTimestamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            Path todayBackup = Paths.get(plugin.getDataFolder()+ "/guilds/backups/" + backupTimestamp);

            if(!Files.exists(todayBackup)){
                Files.createDirectories(todayBackup);
                Files.list(Paths.get(plugin.getDataFolder() + "/guilds/"))
                        .forEach(f -> backup(todayBackup, f));
            }

            Files.list(path)
                    .map(Path::toFile)
                    .filter(f -> !f.isDirectory())
                    .forEach(File::delete);


            Guilds.GUILDS.forEach(g -> saveGuild(g, ow, plugin.getDataFolder()+ "/guilds/"));
        } catch (IOException e) {
            ChatUtils.error("There was an unexpected error creating the Guilds directory: ");
            e.printStackTrace();
        }
    }

    private static void saveGuild(Guild guild, ObjectWriter ow, String path){
        try {
            String json = ow.writeValueAsString(guild);
            Files.write(Paths.get(path + guild.getGuildName() + ".json"), json.getBytes());
        } catch (IOException e) {
            ChatUtils.error("There was an unexpected error saving the guild " + guild.getGuildName() + ": ");
            e.printStackTrace();
        }
    }

    public static void backup(Path backupDir, Path filePath){
        try {
            Files.move(filePath, backupDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void load(JavaPlugin plugin){
        try {
            Path path = Paths.get(plugin.getDataFolder()+ "/guilds/");
            Files.createDirectories(path);
            Files.list(Paths.get(plugin.getDataFolder() + "/guilds/"))
            .forEach(StorageController::loadGuild);

        } catch (IOException e) {
            ChatUtils.error("There was an unexpected error loading guilds:");
            e.printStackTrace();
        }
    }

    private static void loadGuild(Path filePath){


        try {
            String json = Files.lines(filePath).collect(Collectors.joining());
            Guild guild = MAPPER.readValue(json, Guild.class);
            Guilds.GUILDS.add(guild);
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


}
