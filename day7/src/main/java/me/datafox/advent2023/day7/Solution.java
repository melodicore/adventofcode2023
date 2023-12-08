package me.datafox.advent2023.day7;

import me.datafox.advent2023.SolutionBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

/**
 * Solution for advent of code 2023 day 7.
 *
 * @see <a href=https://adventofcode.com/2023/day/7>https://adventofcode.com/2023/day/7</a>
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    private static final char[] cards = { '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A' };
    private static final char[] cards2 = { 'J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A' };
    private static final int[][] hands = { {}, {2}, {2,2}, {3}, {2,3}, {4}, {5} };

    public static void main(String[] args) {
        new Solution().run();
    }

    @Override
    protected String solution1(String input) {
        return String.valueOf(input
                .lines()
                .sorted(this::compareHands)
                .mapToInt(s -> Integer.parseInt(s.split(" ")[1]))
                .map(new CountingMultiplier())
                .sum());
    }

    @Override
    protected String solution2(String input) {
        return String.valueOf(input
                .lines()
                .sorted(this::compareHandsWithJoker)
                .mapToInt(s -> Integer.parseInt(s.split(" ")[1]))
                .map(new CountingMultiplier())
                .sum());
    }

    private int compareHands(String hand1, String hand2) {
        int value1 = getHandValue(hand1.substring(0, 5));
        int value2 = getHandValue(hand2.substring(0, 5));
        if(value1 != value2) {
            return Integer.compare(value1, value2);
        }
        for(int i = 0;i < 5; i++) {
            char card1 = hand1.charAt(i);
            char card2 = hand2.charAt(i);
            if(card1 != card2) {
                return compareCards(card1, card2, cards);
            }
        }
        return 0;
    }

    private int compareHandsWithJoker(String hand1, String hand2) {
        int value1 = getHandValueWithJoker(hand1.substring(0, 5));
        int value2 = getHandValueWithJoker(hand2.substring(0, 5));
        if(value1 != value2) {
            return Integer.compare(value1, value2);
        }
        for(int i = 0;i < 5; i++) {
            char card1 = hand1.charAt(i);
            char card2 = hand2.charAt(i);
            if(card1 != card2) {
                return compareCards(card1, card2, cards2);
            }
        }
        return 0;
    }

    private int getHandValue(String hand) {
        Map<Character,Integer> map = new HashMap<>();
        for(char c : hand.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        int[] result = map
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .filter(i -> i > 1)
                .sorted()
                .toArray();
        for(int i = 0; i < hands.length; i++) {
            if(Arrays.equals(result, hands[i])) {
                return i;
            }
        }
        return hands.length;
    }

    private int getHandValueWithJoker(String hand) {
        Map<Character,Integer> map = new HashMap<>();
        int jokers = 0;
        for(char c : hand.toCharArray()) {
            if(c == 'J') {
                jokers++;
                continue;
            }
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        int[] result = map
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .filter(i -> i > 1)
                .sorted()
                .toArray();
        if(jokers != 0) {
            if(jokers == 5) {
                result = new int[] { 5 };
            } else if(result.length == 0) {
                result = new int[] { Math.min(5, jokers + 1) };
            } else {
                result[result.length-1] += jokers;
            }
        }
        for(int i = 0; i < hands.length; i++) {
            if(Arrays.equals(result, hands[i])) {
                return i;
            }
        }
        return hands.length;
    }

    private int compareCards(char card1, char card2, char[] arr) {
        return Integer.compare(indexOf(card1, arr), indexOf(card2, arr));
    }

    private int indexOf(char card, char[] arr) {
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] == card) {
                return i;
            }
        }
        return arr.length;
    }

    private static class CountingMultiplier implements IntUnaryOperator {
        int count = 1;

        @Override
        public int applyAsInt(int operand) {
            return operand * count++;
        }
    }
}
