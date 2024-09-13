package dev.snowcave.guilds.utils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by WinterBear on 29/02/2024.
 */
public class TabCompleteUtils {

    public static List<String> doMultiwordTabComplete(String[] arguments, int argStart, List<String> multiwords){
        if (arguments.length >= argStart){
            List<String> matches = new ArrayList<>();
            // Find words that match existing arguments
            String matchArg = arguments[argStart - 1];
            for (String multiword : multiwords){
                if(multiword.startsWith(matchArg)){
                    matches.add(multiword);
                }
            }
            int position = arguments.length;
            return getArgWords(position - 1, matches, Arrays.copyOfRange(arguments, 1, arguments.length - 1));
        } else {
            return new ArrayList<>();
        }
    }

    private static List<String> getArgWords(int wordNum, List<String> words, String[] args){
        List<String> candidates = new ArrayList<>();
        for (String arg : words){
            if(arg.split("\\s").length >= wordNum){

                String endSegment = arg.replaceFirst("^" + StringUtils.join(args, " ") + " ", "");

                candidates.add(endSegment);
            }
        }

        return candidates;
    }

}
