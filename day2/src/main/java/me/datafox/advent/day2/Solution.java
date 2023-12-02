package me.datafox.advent.day2;

import me.datafox.advent.SolutionBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Solution for advent of code day 2.
 *
 * @see <a href=https://adventofcode.com/2023/day/2>https://adventofcode.com/2023/day/2</a>
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    private static final String RED = "red";
    private static final String GREEN = "green";
    private static final String BLUE = "blue";
    private static final int MAX_RED = 12;
    private static final int MAX_GREEN = 13;
    private static final int MAX_BLUE = 14;

    public static void main(String[] args) {
        new Solution().run();
    }

    @Override
    protected String solution1(String input) {
        return String.valueOf(Arrays
                //Split input by line
                .stream(input.split("\n"))
                //Calculate the value of each line
                .mapToInt(this::calculateLine)
                //Sum together
                .sum());
    }

    @Override
    protected String solution2(String input) {
        return String.valueOf(Arrays
                //Split input by line
                .stream(input.split("\n"))
                //Calculate the value of each line
                .mapToInt(this::calculateProduct)
                //Sum together
                .sum());
    }

    private int calculateLine(String line) {
        //Split line by a colon to separate game number from game outcome
        String[] split = line.split(":");
        //Get the id of the game
        int id = Integer.parseInt(split[0].substring(5));
        //Return the id of the game if the game is possible, otherwise return 0
        return possibleGame(split[1]) ? id : 0;
    }

    private boolean possibleGame(String game) {
        //Split game by a semicolon to separate rounds
        return 1 == Arrays.stream(game.split(";"))
                //Check if the round is possible (method returns 1 for possible rounds and 0 for impossible)
                .mapToInt(this::possibleRound)
                //Get minimum of all values (if any of the rounds are impossible, return 0)
                .min().orElse(0);
    }

    private int possibleRound(String round) {
        //Split round by a comma to separate cubes
        return Arrays.stream(round.split(","))
                //Check if the count of cubes is possible (method returns 1 for possible rounds and 0 for impossible)
                .mapToInt(this::possibleCount)
                //Get minimum of all values (if any of the counts are impossible, return 0)
                .min().orElse(0);
    }

    private int possibleCount(String count) {
        //Split input by a whitespace to separate the number of cubes from the color
        String[] split = count.substring(1).split(" ");
        //Get the number of cubes
        int i = Integer.parseInt(split[0]);
        //Check if the number of cubes is larger than the maximum number of cubes
        return switch(split[1]) {
            case RED -> i > MAX_RED;
            case GREEN -> i > MAX_GREEN;
            case BLUE -> i > MAX_BLUE;
            default -> false;
            //Return 1 if the number of cubes is within the allowed range
        } ? 0 : 1;
    }

    private int calculateProduct(String game) {
        //Split game by a colon, get the second part, and then split by semicolon to get individual rounds
        return Arrays.stream(game.split(":")[1].split(";"))
                //Map the number of cubes to the color of the cubes
                .map(this::mapCubes)
                //Combine maps by using the max function for the number of cubes
                .reduce(this::maxMap)
                .orElseThrow()
                .values()
                .stream()
                //Get the product of all values in the combined map
                .reduce(1, (i, j) -> i * j);
    }

    private Map<String,Integer> mapCubes(String round) {
        Map<String,Integer> map = new HashMap<>();
        //Split round by a comma to separate cubes
        for(String s : round.split(",")) {
            //Split input by a whitespace to separate the number of cubes from the color
            String[] split = s.substring(1).split(" ");
            //Store the color and number of cubes in the map
            map.put(split[1], Integer.parseInt(split[0]));
        }
        return map;
    }

    private Map<String,Integer> maxMap(Map<String,Integer> map1, Map<String,Integer> map2) {
        Map<String,Integer> map = new HashMap<>();
        //Get the maximum of each color in the map
        map.put(RED, maxEntry(RED, map1, map2));
        map.put(GREEN, maxEntry(GREEN, map1, map2));
        map.put(BLUE, maxEntry(BLUE, map1, map2));
        return map;
    }

    private int maxEntry(String color, Map<String,Integer> map1, Map<String,Integer> map2) {
        if(map1.containsKey(color)) {
            if(map2.containsKey(color)) {
                //If both maps contain the same color, get the maximum
                return Math.max(map1.get(color), map2.get(color));
            } else {
                //If the first map contains a color, return its number
                return map1.get(color);
            }
        }
        //If the second map contains a color, return its number, otherwise return 0
        return map2.getOrDefault(color, 0);
    }
}
