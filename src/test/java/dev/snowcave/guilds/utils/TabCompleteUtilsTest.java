package dev.snowcave.guilds.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by WinterBear on 29/02/2024.
 */
public class TabCompleteUtilsTest {

    @Test
    public void testMultiwordTabComplete(){
        String[] arguments = {"info", ""};
        int argStart = 2;
        List<String> guildnames = List.of("Damynewro", "The Eternal Kingdom of Jam", "Bloodmoney", "Jaffa Land");
        List<String> tabCompletions = TabCompleteUtils.doMultiwordTabComplete(arguments, argStart, guildnames);
        Assert.assertEquals(List.of("Damynewro", "The Eternal Kingdom of Jam", "Bloodmoney", "Jaffa Land"), tabCompletions);
    }

    @Test
    public void testMultiwordTabCompletePartialMatch(){
        String[] arguments = {"info", "D"};
        int argStart = 2;
        List<String> guildnames = List.of("Damynewro", "The Eternal Kingdom of Jam", "Bloodmoney", "Jaffa Land");
        List<String> tabCompletions = TabCompleteUtils.doMultiwordTabComplete(arguments, argStart, guildnames);
        Assert.assertEquals(List.of("Damynewro"), tabCompletions);
    }

    @Test
    public void testMultiwordTabCompletePartialMatch2(){
        String[] arguments = {"info", "The", ""};
        int argStart = 2;
        List<String> guildnames = List.of("Damynewro", "The Eternal Kingdom of Jam", "Bloodmoney", "Jaffa Land", "The Lair");
        List<String> tabCompletions = TabCompleteUtils.doMultiwordTabComplete(arguments, argStart, guildnames);
        Assert.assertEquals(List.of("Eternal Kingdom of Jam", "Lair"), tabCompletions);
    }

    @Test
    public void testMultiwordTabCompletePartialMatch3(){
        String[] arguments = {"info", "The", "Eternal", "Ki"};
        int argStart = 2;
        List<String> guildnames = List.of("Damynewro", "The Eternal Kingdom of Jam", "Bloodmoney", "Jaffa Land", "The Lair");
        List<String> tabCompletions = TabCompleteUtils.doMultiwordTabComplete(arguments, argStart, guildnames);
        Assert.assertEquals(List.of("Kingdom of Jam"), tabCompletions);
    }

}