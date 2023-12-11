package me.datafox.advent2023.day11;

import me.datafox.advent2023.SolutionBase;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Solution for advent of code 2023 day 1.
 *
 * @see <a href=https://adventofcode.com/2023/day/1>https://adventofcode.com/2023/day/1</a>
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    public static void main(String[] args) {
        new Solution().run();
    }

    @Override
    protected String solution1(String input) {
        String[] lines = input.split("\n");
        Set<Universe> universes = new HashSet<>();
        for(int y = 0; y < lines.length; y++) {
            for(int x = 0; x < lines[y].length(); x++) {
                if(lines[y].charAt(x) == '#') {
                    universes.add(new Universe(x, y));
                }
            }
        }
        Set<Universe> expanded = new HashSet<>();
        int[] expansion = { 0 };
        for(int[] x = { 0 }; x[0] < lines[0].length(); x[0]++) {
            Set<Universe> current = universes.stream().filter(u -> u.x == x[0]).collect(Collectors.toSet());
            if(current.isEmpty()) {
                expansion[0]++;
            } else {
                expanded.addAll(current.stream().map(u -> u.add(expansion[0], 0)).collect(Collectors.toSet()));
            }
        }
        universes = expanded;
        expanded = new HashSet<>();
        expansion[0] = 0;
        for(int[] y = { 0 }; y[0] < lines.length; y[0]++) {
            Set<Universe> current = universes.stream().filter(u -> u.y == y[0]).collect(Collectors.toSet());
            if(current.isEmpty()) {
                expansion[0]++;
            } else {
                expanded.addAll(current.stream().map(u -> u.add(0, expansion[0])).collect(Collectors.toSet()));
            }
        }
        List<Universe> universeList = new ArrayList<>(expanded);
        Set<Universe> visited = new HashSet<>();
        int sum = 0;
        for(Universe u : universeList) {
            visited.add(u);
            for(Universe u1 : universeList) {
                if(!visited.contains(u1)) {
                    sum += u.distance(u1);
                }
            }
        }
        return String.valueOf(sum);
    }

    @Override
    protected String solution2(String input) {
        return "";
    }

    private record Universe(int x, int y) {
        public Universe add(int x, int y) {
            return new Universe(this.x + x, this.y + y);
        }

        public int distance(Universe o) {
            return Math.abs(x - o.x) + Math.abs(y - o.y);
        }
    }
}
