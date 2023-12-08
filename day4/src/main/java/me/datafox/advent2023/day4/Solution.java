package me.datafox.advent2023.day4;

import me.datafox.advent2023.SolutionBase;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Solution for advent of code 2023 day 4.
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
        Map<Integer,Integer> map = input.lines()
                .map(this::toScoreEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        int[] cards = new int[map.size()];
        Arrays.fill(cards, 1);
        int[] sum = new int[1];
        map.forEach((id, score) -> {
            sum[0] += cards[id - 1];
            for(int i = id; i < id + score; i++) {
                if(i >= map.size()) break;
                cards[i] += cards[id - 1];
            }
        });
        return String.valueOf(sum[0]);
    }

    private int calculateScore(String game) {
        String[] scores = game
                .replaceAll(" {2,}", " ")
                .split(":")[1]
                .split("\\|");
        Set<Integer> numbers = toNumberSet(scores[0]);
        Set<Integer> winning = toNumberSet(scores[1]);
        long matches = numbers.stream().filter(winning::contains).count();
        if(matches == 0) return 0;
        return 1 << matches - 1;
    }

    private Set<Integer> toNumberSet(String numbers) {
        return Arrays
                .stream(numbers.strip().split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    private Map.Entry<Integer,Integer> toScoreEntry(String game) {
        String[] split = game
                .replaceAll(" {2,}", " ")
                .split(":");
        int id = Integer.parseInt(split[0].split(" ")[1]);
        String[] scores = split[1].split("\\|");
        Set<Integer> numbers = toNumberSet(scores[0]);
        Set<Integer> winning = toNumberSet(scores[1]);
        long score = numbers.stream().filter(winning::contains).count();
        return new AbstractMap.SimpleEntry<>(id, (int) score);
    }
}
