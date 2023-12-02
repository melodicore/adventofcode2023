package me.datafox.advent.day1;

import me.datafox.advent.SolutionBase;

import java.util.Arrays;

/**
 * Solution for advent of code day 1 part 1.
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    public static void main(String[] args) {
        new Solution().run();
    }

    @Override
    protected String solution(String input) {
        int out = Arrays.stream(input.split("\n"))
                .mapToInt(this::calculateLine)
                .sum();
        return String.valueOf(out);
    }

    private int calculateLine(String line) {
        char[] chars = line.toCharArray();
        int first = 0, second = 0;
        for(char c : chars) {
            if(Character.isDigit(c)) {
                first = Integer.parseInt(String.valueOf(c));
                break;
            }
        }
        for(int i = chars.length - 1; i >= 0; i--) {
            if(Character.isDigit(chars[i])) {
                second = Integer.parseInt(String.valueOf(chars[i]));
                break;
            }
        }
        return first * 10 + second;
    }
}
