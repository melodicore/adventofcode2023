package me.datafox.advent2023.day11;

import me.datafox.advent2023.SolutionBase;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.ToLongFunction;
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
        Set<Universe> universes = getUniverses(input);
        universes = expand(universes, 1, Universe::x, (u, l) -> u.add(l, 0));
        universes = expand(universes, 1, Universe::y, (u, l) -> u.add(0, l));
        return String.valueOf(distanceSum(universes));
    }

    @Override
    protected String solution2(String input) {
        Set<Universe> universes = getUniverses(input);
        universes = expand(universes, 999999, Universe::x, (u, l) -> u.add(l, 0));
        universes = expand(universes, 999999, Universe::y, (u, l) -> u.add(0, l));
        return String.valueOf(distanceSum(universes));
    }

    private Set<Universe> getUniverses(String input) {
        String[] lines = input.split("\n");
        Set<Universe> universes = new HashSet<>();
        for(int y = 0; y < lines.length; y++) {
            for(int x = 0; x < lines[y].length(); x++) {
                if(lines[y].charAt(x) == '#') {
                    universes.add(new Universe(x, y));
                }
            }
        }
        return universes;
    }

    private Set<Universe> expand(Set<Universe> universes, long expand, ToLongFunction<Universe> get, BiFunction<Universe,Long,Universe> add) {
        Set<Universe> expanded = new HashSet<>();
        long[] expansion = { 0 };
        for(int[] i = { 0 }; i[0] <= universes.stream().mapToLong(get).max().orElseThrow(); i[0]++) {
            Set<Universe> current = universes.stream().filter(u -> get.applyAsLong(u) == i[0]).collect(Collectors.toSet());
            if(current.isEmpty()) {
                expansion[0] += expand;
            } else {
                expanded.addAll(current.stream().map(u -> add.apply(u, expansion[0])).collect(Collectors.toSet()));
            }
        }
        return expanded;
    }

    private long distanceSum(Set<Universe> universes) {
        Set<Universe> visited = new HashSet<>();
        long sum = 0;
        for(Universe u : universes) {
            visited.add(u);
            for(Universe u1 : universes) {
                if(!visited.contains(u1)) {
                    sum += u.distance(u1);
                }
            }
        }
        return sum;
    }

    private record Universe(long x, long y) {
        public Universe add(long x, long y) {
            return new Universe(this.x + x, this.y + y);
        }

        public long distance(Universe o) {
            return Math.abs(x - o.x) + Math.abs(y - o.y);
        }
    }
}
