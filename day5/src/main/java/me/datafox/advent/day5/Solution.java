package me.datafox.advent.day5;

import me.datafox.advent.SolutionBase;

import java.util.*;
import java.util.function.Consumer;
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

    @Override
    protected String solution2(String input) {
        long[] seeds = Arrays
                .stream(input
                        .split("\n", 2)[0]
                        .substring(7)
                        .split(" "))
                .mapToLong(Long::parseLong)
                .toArray();

        List<Range> ranges = new ArrayList<>();

        for(int i=0;i<seeds.length;i+=2) {
            ranges.add(new Range(seeds[i], seeds[i] + seeds[i+1]));
        }

        System.out.println("Ranges:\n" + ranges);

        Map<String,Mapper> mappers = Arrays
                .stream(input.split("\n\n"))
                .collect(Collectors.toMap(
                        this::getKey,
                        this::parseMapper));

        //System.out.println("Mappers:\n" + mappers);

        Set<Range> out = new HashSet<>(ranges);
        keys.stream()
            .peek(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    System.out.println("For key:\n" + s);
                }
            })
            .map(mappers::get)
            .peek(new Consumer<Mapper>() {
                @Override
                public void accept(Mapper mapper) {
                    System.out.println("With mapper:\n" + mapper);
                }
            })
            .forEachOrdered(mapper -> {
                System.out.println("Start mapping ranges:\n" + out);
                Set<Range> original = new HashSet<>(out);
                out.clear();
                for(Range r : original) {
                    System.out.println("Range:\n" + r);
                    Set<Range> temp = mapper.mapRange(r);
                    System.out.println("Mapped to:\n" + temp);
                    temp = combineRanges(temp);
                    System.out.println("Combined to:\n" + temp);
                    out.addAll(temp);
                }
                Set<Range> temp = combineRanges(out);
                out.clear();
                out.addAll(temp);
                System.out.println("Ranges mapped and combined:\n" + out);
            });
        return out.stream().min(Comparator.comparingLong(Range::start)).toString();
    }

    private Set<Range> combineRanges(Set<Range> ranges) {
        Set<Range> combined = new HashSet<>();
        Range current = null;
        for(Range r : ranges
                .stream()
                .sorted(Comparator.comparingLong(Range::start))
                .toArray(Range[]::new)) {
            if(current == null) {
                current = r;
                continue;
            }
            if(current.end > r.start) {
                if(current.end < r.end) {
                    current = new Range(current.start, r.end);
                }
            } else {
                combined.add(current);
                current = r;
            }
        }
        if(current != null) {
            combined.add(current);
        }
        return combined;
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

    private class Mapper {
        private final Map<Range,Long> ranges;

        private Mapper(List<long[]> map) {
            ranges = map
                    .stream()
                    .collect(Collectors.toMap(
                            arr -> new Range(arr[1], arr[1] + arr[2]),
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

        public Set<Range> mapRange(Range range) {
            Set<Range> in = new HashSet<>(Set.of(range));
            Set<Range> out = new HashSet<>();
            for(Range check : ranges.keySet()) {
                for(Range r : new HashSet<>(in)) {
                    List<Range> list = splitMap(r, check);
                    Range mapped = list.get(0);
                    if(mapped != null) out.add(mapped);
                    list.remove(0);
                    in.clear();
                    in.addAll(list);
                }
            }
            out.addAll(in);
            return combineRanges(out);
        }

        private List<Range> splitMap(Range split, Range other) {
            List<Range> out = new ArrayList<>();
            if(!split.collision(other)) {
                out.add(null);
                out.add(split);
                return out;
            }
            System.out.println("Range: " + split + ", Other: " + other);
            if(split.start >= other.start && split.end <= other.end) {
                System.out.println("Range is completely within other");
                Range r = new Range(other.convert(split.start, ranges.get(other)),
                        other.convert(split.end, ranges.get(other)));
                System.out.println("Range split and converted to:\n" + r);
                out.add(r);
                return out;
            }
            if(split.start < other.start) {
                System.out.println("Range starts before other");
                out.add(new Range(split.start, other.start));
                split = new Range(other.start, split.end);
            }
            if(split.end > other.end) {
                System.out.println("Range ends after other");
                out.add(new Range(other.end, split.end));
                split = new Range(split.start, other.end);
            }
            out.add(0, new Range(other.convert(split.start, ranges.get(other)),
                    other.convert(split.end, ranges.get(other))));
            System.out.println("Range split and converted to:\n" + out);
            return out;
        }

        @Override
        public String toString() {
            return ranges.toString();
        }
    }

    private record Range(long start, long end) {
        public boolean isInRange(long i) {
            return i >= start && i < end;
        }

        public long convert(long in, long firstOut) {
            return in - start + firstOut;
        }

        public boolean collision(Range other) {
            return !(start >= other.end || end <= other.start);
        }
    }
}
