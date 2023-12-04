package me.datafox.advent.day4;

import me.datafox.advent.SolutionBase;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Solution for advent of code day 4.
 *
 * @see <a href=https://adventofcode.com/2023/day/4>https://adventofcode.com/2023/day/4</a>
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    public static void main(String[] args) {
        new Solution().run();
    }

    @Override
    protected String solution1(String input) {
        return String.valueOf(input.lines()
                .mapToInt(this::calculateScore)
                .sum());
    }

    @Override
    protected String solution2(String input) {
        return "";
    }

    private int calculateScore(String game) {
        String[] scores = game
                .replaceAll(" {2,}", " ")
                .split(":")[1]
                .split("\\|");
        Set<Integer> numbers = Arrays
                .stream(scores[0].strip().split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
        Set<Integer> winning = Arrays
                .stream(scores[1].strip().split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
        long matches = numbers.stream().filter(winning::contains).count();
        if(matches == 0) return 0;
        return 1 << matches - 1;
    }
}
