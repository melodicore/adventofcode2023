package me.datafox.advent2023.day2;

import me.datafox.advent2023.SolutionBase;

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
        return String.valueOf(input.lines()
                .mapToInt(this::calculateLine)
                .sum());
    }

    @Override
    protected String solution2(String input) {
        return String.valueOf(input.lines()
                .mapToInt(this::calculateProduct)
                .sum());
    }

    /**
     * Parses the id of the game. If the game is possible, return the id. Otherwise, return 0.
     */
    private int calculateLine(String line) {
        //Split line by a colon to separate game number from game outcome
        String[] split = line.split(":");
        //Get the id of the game
        int id = Integer.parseInt(split[0].substring(5));
        //Return the id of the game if the game is possible, otherwise return 0
        return possibleGame(split[1]) ? id : 0;
    }

    /**
     * Iterates through rounds of the game and returns 1 if all rounds are possible, and 0 otherwise.
     */
    private boolean possibleGame(String game) {
        return 1 == Arrays.stream(game.split(";"))
                .mapToInt(this::possibleRound)
                .min().orElse(0);
    }

    /**
     * Iterates through cubes of the round and returns 1 if all counts of cubes are possible, and 0 otherwise.
     */
    private int possibleRound(String round) {
        return Arrays.stream(round.split(","))
                .mapToInt(this::possibleCount)
                .min().orElse(0);
    }

    /**
     * If the number of cubes is smaller or equal to the maximum number of cubes, returns 1. Otherwise, returns 0.
     */
    private int possibleCount(String count) {
        String[] split = count.substring(1).split(" ");
        int i = Integer.parseInt(split[0]);
        return switch(split[1]) {
            case RED -> i > MAX_RED;
            case GREEN -> i > MAX_GREEN;
            case BLUE -> i > MAX_BLUE;
            default -> false;
        } ? 0 : 1;
    }

    /**
     * Calculates the product of a game.
     */
    private int calculateProduct(String game) {
        return Arrays.stream(game.split(":")[1].split(";"))
                .map(this::mapCubes)
                .reduce(this::maxMap)
                .orElseThrow()
                .values()
                .stream()
                .reduce(1, (i, j) -> i * j);
    }

    /**
     * Maps colors of cubes to the number of cubes of that color.
     */
    private Map<String,Integer> mapCubes(String round) {
        Map<String,Integer> map = new HashMap<>();
        for(String s : round.split(",")) {
            String[] split = s.substring(1).split(" ");
            map.put(split[1], Integer.parseInt(split[0]));
        }
        return map;
    }

    /**
     * Combines two maps by getting the maximum value of each color of cube.
     */
    private Map<String,Integer> maxMap(Map<String,Integer> map1, Map<String,Integer> map2) {
        Map<String,Integer> map = new HashMap<>();
        map.put(RED, maxEntry(RED, map1, map2));
        map.put(GREEN, maxEntry(GREEN, map1, map2));
        map.put(BLUE, maxEntry(BLUE, map1, map2));
        return map;
    }

    /**
     * Returns the maximum value of the specified color in the two maps, or 0 if neither map contains the color.
     */
    private int maxEntry(String color, Map<String,Integer> map1, Map<String,Integer> map2) {
        if(map1.containsKey(color)) {
            if(map2.containsKey(color)) {
                return Math.max(map1.get(color), map2.get(color));
            } else {
                return map1.get(color);
            }
        }
        return map2.getOrDefault(color, 0);
    }
}
