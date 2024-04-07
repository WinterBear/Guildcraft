package dev.snowcave.guilds.utils;

import dev.snowcave.guilds.core.Guild;
import io.github.winterbear.WinterCoreUtils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by WinterBear on 17/11/2023.
 */
public class Chatter {

    private static final Character MODIFIER_UNCONVERTED = '&';

    private static final String PREFIX = ChatColor.of("#475da6") + "⛨ Guilds &8>> &7";

    private final Player player;

    public Chatter(Player player){
        this.player = player;
    }

    public void send(String text){
        send(player, text);
    }

    public void send(Component text){
        send(player, text);
    }

    public void blank(){
        send(player, "");
    }

    public void sendP(String text){
        sendP(player, text);
    }


    public void error(String text){
        sendP(player, "&c" + text);
    }

    public static void send(Player player, String text){
        player.sendMessage(ChatColor.translateAlternateColorCodes(MODIFIER_UNCONVERTED, text));
    }

    public static void send(CommandSender sender, String text){
        sender.sendMessage(ChatColor.translateAlternateColorCodes(MODIFIER_UNCONVERTED, text));
    }

    public static void send(Player player, Component text){
        player.sendMessage(text);
    }

    public static void send(CommandSender sender, Component text){
        sender.sendMessage(text);
    }

    public static void sendP(Player player, String text){
        send(player, PREFIX + text);
    }

    public static void sendP(CommandSender sender, String text){
        send(sender, PREFIX + text);
    }

    public static void error(Player player, String text) {
        sendP(player, "&c" + text);
    }

    public static void error(CommandSender sender, String text) {
        sendP(sender, "&c" + text);
    }

    public static void broadcast(String text){
        Bukkit.broadcastMessage(ChatUtils.format(text));
    }

    public static void broadcastP(String text){
        Bukkit.broadcastMessage(ChatUtils.format(PREFIX + text));
    }

    public static void broadcastP(Guild guild, String text){
        guild.broadcast(ChatUtils.format(PREFIX + text));
    }

    public static void warnConsole(String message) {
        Bukkit.getServer().getLogger().warning(ChatUtils.format(PREFIX + message));
    }

    public static void infoConsole(String message) {
        Bukkit.getServer().getLogger().info(ChatUtils.format(PREFIX + message));
    }

    public static void errorConsole(String message) {
        Bukkit.getServer().getLogger().severe(ChatUtils.format(PREFIX + message));
    }

}
