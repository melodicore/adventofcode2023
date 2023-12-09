package me.datafox.advent2023.day9;

import me.datafox.advent2023.SolutionBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Solution for advent of code 2023 day 9.
 *
 * @see <a href=https://adventofcode.com/2023/day/9>https://adventofcode.com/2023/day/9</a>
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    public static void main(String[] args) {
        new Solution().run();
    }

    @Override
    protected String solution1(String input) {
        return String.valueOf(input
                .lines()
                .map(this::getSequence)
                .map(this::fillSequence)
                .mapToInt(this::getNext)
                .sum());
    }

    @Override
    protected String solution2(String input) {
        return String.valueOf(input
                .lines()
                .map(this::getSequence)
                .map(this::fillSequence)
                .mapToInt(this::getPrevious)
                .sum());
    }

    private int[] getSequence(String s) {
        return Arrays
                .stream(s.split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private List<int[]> fillSequence(int[] ints) {
        List<int[]> list = new ArrayList<>();
        list.add(ints);
        boolean running = true;
        while(running) {
            int[] arr = new int[ints.length - 1];
            boolean finished = true;
            for(int i = 0; i < arr.length; i++) {
                arr[i] = ints[i + 1] - ints[i];
                if(arr[i] != 0) {
                    finished = false;
                }
            }
            list.add(arr);
            if(finished) {
                running = false;
            } else {
                ints = arr;
            }
        }
        return list;
    }

    private int getNext(List<int[]> list) {
        int result = 0;
        Collections.reverse(list);
        for(int[] ints : list) {
            result += ints[ints.length - 1];
        }
        return result;
    }

    private int getPrevious(List<int[]> list) {
        int result = 0;
        Collections.reverse(list);
        for(int[] ints : list) {
            result = ints[0] - result;
        }
        return result;
    }
}
