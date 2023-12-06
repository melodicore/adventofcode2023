package me.datafox.advent.day6;

import me.datafox.advent.SolutionBase;

import java.util.Arrays;

/**
 * Solution for advent of code day 6.
 *
 * @see <a href=https://adventofcode.com/2023/day/?>https://adventofcode.com/2023/day/6</a>
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
        int[] time = Arrays.stream(split[0].split(" +", 2)[1].split(" +")).mapToInt(Integer::parseInt).toArray();
        int[] distance = Arrays.stream(split[1].split(" +", 2)[1].split(" +")).mapToInt(Integer::parseInt).toArray();
        System.out.println(Arrays.toString(time));
        System.out.println(Arrays.toString(distance));
        int result = 1;
        for(int i = 0; i < time.length; i++) {
            int ways = 0;
            for(int j = 1; j < time[i]; j++) {
                if(j * (time[i] - j) > distance[i]) {
                    System.out.println(time[i] + ", " + distance[i] + ", " + j);
                    ways++;
                }
            }
            System.out.println(time[i] + ", " + distance[i] + ", " + ways);
            result *= ways;
        }
        return String.valueOf(result);
    }

    @Override
    protected String solution2(String input) {
        return "";
    }
}
