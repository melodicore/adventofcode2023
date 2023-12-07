package me.datafox.advent.day7;

import me.datafox.advent.SolutionBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

/**
 * Solution for advent of code day 7.
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
                .map(new IntUnaryOperator() {
                    int count = 1;
                    @Override
                    public int applyAsInt(int operand) {
                        return operand * count++;
                    }
                }).sum());
    }

    @Override
    protected String solution2(String input) {
        return String.valueOf(input
                .lines()
                .sorted(this::compareHands2)
                .mapToInt(s -> Integer.parseInt(s.split(" ")[1]))
                .map(new IntUnaryOperator() {
                    int count = 1;
                    @Override
                    public int applyAsInt(int operand) {
                        return operand * count++;
                    }
                }).sum());
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
                return compareCards(card1, card2);
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

    private int compareCards(char card1, char card2) {
        return Integer.compare(indexOf(card1), indexOf(card2));
    }

    private int indexOf(char card) {
        for(int i = 0; i < cards.length; i++) {
            if(cards[i] == card) {
                return i;
            }
        }
        return cards.length;
    }

    private int compareHands2(String hand1, String hand2) {
        int value1 = getHandValue2(hand1.substring(0, 5));
        int value2 = getHandValue2(hand2.substring(0, 5));
        if(value1 != value2) {
            return Integer.compare(value1, value2);
        }
        for(int i = 0;i < 5; i++) {
            char card1 = hand1.charAt(i);
            char card2 = hand2.charAt(i);
            if(card1 != card2) {
                return compareCards2(card1, card2);
            }
        }
        return 0;
    }

    private int getHandValue2(String hand) {
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

    private int compareCards2(char card1, char card2) {
        return Integer.compare(indexOf2(card1), indexOf2(card2));
    }

    private int indexOf2(char card) {
        for(int i = 0; i < cards2.length; i++) {
            if(cards2[i] == card) {
                return i;
            }
        }
        return cards2.length;
    }
}
