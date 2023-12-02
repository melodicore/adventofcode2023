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
    //Names of numbers/digits from one to nine
    private static final String[] DIGITS = { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };


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
                //Map written numbers to digits
                .map(this::toDigits)
                //Calculate the value of each line
                .mapToInt(this::calculateLine)
                //Sum together
                .sum());
    }

    private int calculateLine(String line) {
        char[] chars = line.toCharArray();
        int first = 0, second = 0;
        //Iterate through characters
        for(char c : chars) {
            //If character is a digit, mark it as the first digit and break the loop
            if(Character.isDigit(c)) {
                first = Integer.parseInt(String.valueOf(c));
                break;
            }
        }
        //Iterate through characters backwards
        for(int i = chars.length - 1; i >= 0; i--) {
            //If character is a digit, mark it as the second digit and break the loop
            if(Character.isDigit(chars[i])) {
                second = Integer.parseInt(String.valueOf(chars[i]));
                break;
            }
        }
        //Combine digits to a two-digit number
        return first * 10 + second;
    }

    private String toDigits(String input) {
        //Iterate through textual representation of digits
        for(int i = 0; i < DIGITS.length; i++) {
            String s = DIGITS[i];
            //Replace textual representation of the digits with two copies of the textual representation with the digit
            //itself sandwiched between them. This is a dirty bodge to overcome edge cases like "twone" where two digits
            //have shared letters.
            input = input.replaceAll(s, s + (i + 1) + s);
        }
        return input;
    }
}
