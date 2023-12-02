package me.datafox.advent.day1;

import me.datafox.advent.SolutionBase;

import java.util.Arrays;

/**
 * Solution for advent of code day 1.
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    private static final String[] DIGITS =
            { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };

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
                .map(this::toDigits)
                .mapToInt(this::calculateLine)
                .sum());
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

    private String toDigits(String input) {
        for(int i = 0; i < DIGITS.length; i++) {
            String s = DIGITS[i];
            input = input.replaceAll(s, s + (i + 1) + s);
        }
        return input;
    }
}
