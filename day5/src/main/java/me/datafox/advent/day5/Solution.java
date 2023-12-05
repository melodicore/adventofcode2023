package me.datafox.advent.day5;

import me.datafox.advent.SolutionBase;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Solution for advent of code day 5.
 *
 * @see <a href=https://adventofcode.com/2023/day/5>https://adventofcode.com/2023/day/5</a>
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    private static final Set<String> keys = new LinkedHashSet<>(List.of(
            "seed-to-soil",
            "soil-to-fertilizer",
            "fertilizer-to-water",
            "water-to-light",
            "light-to-temperature",
            "temperature-to-humidity",
            "humidity-to-location"));

    public static void main(String[] args) {
        new Solution().run();
    }

    @Override
    protected String solution1(String input) {
        long[] seeds = Arrays
                .stream(input
                        .split("\n", 2)[0]
                        .substring(7)
                        .split(" "))
                .mapToLong(Long::parseLong)
                .toArray();
        Map<String,Mapper> mappers = Arrays
                .stream(input.split("\n\n"))
                .collect(Collectors.toMap(
                        this::getKey,
                        this::parseMapper));
        return String.valueOf(Arrays.stream(seeds).map(operand -> {
            long[] i = {operand};
            keys.stream()
                    .map(mappers::get)
                    .forEachOrdered(mapper -> i[0] = mapper.map(i[0]));
            return i[0];
        }).min().orElse(-1));
    }

    private String getKey(String str) {
        String key = str.split(" ", 2)[0];
        if(keys.contains(key)) {
            return key;
        }
        return null;
    }

    private Mapper parseMapper(String str) {
        if(str.startsWith("seeds:")) return new Mapper(List.of());
        List<long[]> map = new ArrayList<>();
        for(String lines : str.split("\n", 2)[1].split("\n")) {
            map.add(Arrays.stream(lines.split(" "))
                    .mapToLong(Long::parseLong)
                    .toArray());
        }
        return new Mapper(map);
    }

    @Override
    protected String solution2(String input) {
        return "";
    }

    private static class Mapper {
        private final Map<Range,Long> ranges;

        private Mapper(List<long[]> map) {
            ranges = map
                    .stream()
                    .collect(Collectors.toMap(
                            arr -> new Range(arr[1], arr[2]),
                            arr -> arr[0]));
        }

        long map(long i)  {
            return ranges
                    .keySet()
                    .stream()
                    .filter(r -> r.isInRange(i))
                    .findFirst()
                    .map(r -> r.convert(i, ranges.get(r)))
                    .orElse(i);
        }
    }

    private record Range(long start, long range) {
        public boolean isInRange(long i) {
            return i >= start && i < start + range;
        }

        public long convert(long in, long firstOut) {
            return in - start + firstOut;
        }
    }
}
