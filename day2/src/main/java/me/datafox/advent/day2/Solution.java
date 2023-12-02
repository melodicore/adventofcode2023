package me.datafox.advent.day2;

import me.datafox.advent.SolutionBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Solution for advent of code day 2.
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
                .stream(input.split("\n"))
                .mapToInt(this::calculateLine)
                .sum());
    }

    @Override
    protected String solution2(String input) {
        return String.valueOf(Arrays
                .stream(input.split("\n"))
                .mapToInt(this::calculateProduct)
                .sum());
    }

    private int calculateLine(String line) {
        String[] split = line.split(":");
        int id = Integer.parseInt(split[0].substring(5));
        return possibleGame(split[1]) ? id : 0;
    }

    private boolean possibleGame(String game) {
        return 1 == Arrays.stream(game.split(";"))
                .mapToInt(this::possibleRound)
                .min().orElse(0);
    }

    private int possibleRound(String round) {
        return Arrays.stream(round.split(","))
                .mapToInt(this::possibleCount)
                .min().orElse(0);
    }

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

    private int calculateProduct(String game) {
        return Arrays.stream(game.split(":")[1].split(";"))
                .map(this::mapCubes)
                .reduce(this::maxMap)
                .orElseThrow()
                .values()
                .stream()
                .reduce(1, (i, j) -> i * j);
    }

    private Map<String,Integer> mapCubes(String round) {
        Map<String,Integer> map = new HashMap<>();
        for(String s : round.split(",")) {
            String[] split = s.substring(1).split(" ");
            map.put(split[1], Integer.parseInt(split[0]));
        }
        return map;
    }

    private Map<String,Integer> maxMap(Map<String,Integer> map1, Map<String,Integer> map2) {
        Map<String,Integer> map = new HashMap<>();
        map.put(RED, maxEntry(RED, map1, map2));
        map.put(GREEN, maxEntry(GREEN, map1, map2));
        map.put(BLUE, maxEntry(BLUE, map1, map2));
        return map;
    }

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
