package me.datafox.advent.day6;

import me.datafox.advent.SolutionBase;

import java.util.Arrays;

/**
 * Solution for advent of code day 6.
 *
 * @see <a href=https://adventofcode.com/2023/day/6>https://adventofcode.com/2023/day/6</a>
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    public static void main(String[] args) {
        new Solution().run();
    }

    @Override
    protected String solution1(String input) {
        String[] split = input.split("\n");
        long[] time = Arrays.stream(split[0].split(" +", 2)[1].split(" +")).mapToLong(Long::parseLong).toArray();
        long[] distance = Arrays.stream(split[1].split(" +", 2)[1].split(" +")).mapToLong(Long::parseLong).toArray();
        return String.valueOf(calculate(time, distance));
    }

    @Override
    protected String solution2(String input) {
        String[] split = input.split("\n");
        long time = Long.parseLong(String.join("", split[0].split(" +", 2)[1].split(" +")));
        long distance = Long.parseLong(String.join("", split[1].split(" +", 2)[1].split(" +")));
        return String.valueOf(calculate(new long[] {time}, new long[] {distance}));
    }

    private long calculate(long[] time, long[] distance) {
        long result = 1;
        for(int i = 0; i < time.length; i++) {
            long ways = 0;
            for(int j = 1; j < time[i]; j++) {
                if(j * (time[i] - j) > distance[i]) {
                    ways++;
                }
            }
            result *= ways;
        }
        return result;
    }
}
