package dev.snowcave.guilds.utils;

import java.util.Arrays;

/**
 * Created by WinterBear on 11/11/2023.
 */
public class CommandUtils {

    public static String squashArgs(int from, String[] arguments){
        return String.join(" ", Arrays.copyOfRange(arguments, from, arguments.length));
    }

    public static String[] dropArg(int from, String[] arguments){
        return Arrays.copyOfRange(arguments, from, arguments.length);
    }

}
