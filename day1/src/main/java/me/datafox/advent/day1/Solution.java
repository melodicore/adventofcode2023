package me.datafox.advent.day1;

import me.datafox.advent.SolutionBase;

import java.util.Arrays;

/**
 * Solution for advent of code day 1.
 *
 * @see <a href=https://adventofcode.com/2023/day/1>https://adventofcode.com/2023/day/1</a>
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    private static final String[] DIGITS = { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };


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
                //Sum together
                .sum());
    }

    /**
     * Iterates through a String's characters forwards and backwards, stops at the first digit both times, and combines
     * those digits to a two-digit number.
     */
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

    /**
     * Replaces all textual representations of one-digit numbers with the digit they represent. Because of edge cases
     * like "twone" where two digits share the same letter, the original textual representation is copied to both sides
     * of the digit to keep the shared characters intact. This could be done better, but it was the first solution I
     * came up with.
     */
    private String toDigits(String input) {
        for(int i = 0; i < DIGITS.length; i++) {
            String s = DIGITS[i];
            input = input.replaceAll(s, s + (i + 1) + s);
        }
        return input;
    }
}
