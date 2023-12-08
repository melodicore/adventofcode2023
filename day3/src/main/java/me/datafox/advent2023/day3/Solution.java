package me.datafox.advent2023.day3;

import me.datafox.advent2023.SolutionBase;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Solution for advent of code day 3.
 *
 * @see <a href=https://adventofcode.com/2023/day/3>https://adventofcode.com/2023/day/3</a>
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    public static void main(String[] args) {
        new Solution().run();
    }

    @Override
    protected String solution1(String input) {
        List<String> lines = pad(input.lines().toList());
        Map<Area,Integer> numbers = getNumbers(lines);
        Set<Area> removed = getRemoved(lines, numbers.keySet());
        removed.forEach(numbers::remove);
        return String.valueOf(numbers.values().stream().mapToInt(Integer::intValue).sum());
    }

    @Override
    protected String solution2(String input) {
        List<String> lines = pad(input.lines().toList());
        Map<Area,Integer> numbers = getNumbers(lines);
        Set<Coord> gears = getGears(getStars(lines), numbers.keySet());
        return String.valueOf(gears.stream().mapToInt(coord -> getGearValue(coord, numbers)).sum());
    }

    /**
     * Pads the List of Strings to hack around out-of-bounds array indices.
     */
    private List<String> pad(List<String> input) {
        List<String> lines = new ArrayList<>(input);
        String dots = ".".repeat(lines.get(0).length());
        lines.add(0, dots);
        lines.add(dots);
        lines.replaceAll(s -> "." + s + ".");
        return lines;
    }

    /**
     * Finds all numbers in each line and associate them into a map with the immediate area around the number as the
     * key.
     */
    private Map<Area,Integer> getNumbers(List<String> input) {
        Map<Area,Integer> map = new HashMap<>();
        for(int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            int xd = -1;
            for(int x = 0; x < line.length(); x++) {
                if(Character.isDigit(line.charAt(x))) {
                    if(xd == -1) {
                        xd = x;
                    }
                } else {
                    if(xd != -1) {
                        map.put(new Area(xd - 1, y - 1, x, y + 1),
                                Integer.parseInt(line.substring(xd, x)));
                        xd = -1;
                    }
                }
            }
        }
        return map;
    }

    /**
     * Finds the areas of numbers that do not have a symbol in the immediate area around them.
     */
    private Set<Area> getRemoved(List<String> lines, Set<Area> numbers) {
        return numbers.stream()
                .filter(Predicate.not(area -> hasSymbol(lines, area)))
                .collect(Collectors.toSet());
    }

    /**
     * Returns true if the given area contains a symbol that is not a dot or a digit.
     */
    private boolean hasSymbol(List<String> lines, Area area) {
        for(int y = area.y1; y < area.y2 + 1; y++) {
            for(int x = area.x1; x < area.x2 + 1; x++) {
                char c = lines.get(y).charAt(x);
                if(c != '.' && !Character.isDigit(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds coordinates of all stars in the input.
     */
    private Set<Coord> getStars(List<String> input) {
        Set<Coord> stars = new HashSet<>();
        for(int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            for(int x = 0; x < line.length(); x++) {
                if(line.charAt(x) == '*') {
                    stars.add(new Coord(x, y));
                }
            }
        }
        return stars;
    }

    /**
     * Finds coordinates of all stars that have exactly two numbers in the immediate area around them.
     */
    private Set<Coord> getGears(Set<Coord> stars, Set<Area> numbers) {
        Set<Coord> gears = new HashSet<>();
        for(Coord coord : stars) {
             if(numbers.stream().filter(area -> containsCoord(area, coord)).count() == 2) {
                 gears.add(coord);
             }
        }
        return gears;
    }

    /**
     * Multiplies the two numbers that are in the immediate area around a coordinate.
     */
    private int getGearValue(Coord coord, Map<Area,Integer> numbers) {
        return numbers.entrySet().stream()
                .filter(entry -> containsCoord(entry.getKey(), coord))
                .mapToInt(Map.Entry::getValue)
                .reduce(1, (i, j) -> i * j);
    }

    /**
     * Returns true if a coordinate is within an area.
     */
    private boolean containsCoord(Area area, Coord coord) {
        return area.x1 <= coord.x &&
                area.y1 <= coord.y &&
                area.x2 >= coord.x &&
                area.y2 >= coord.y;
    }

    private record Area(int x1, int y1, int x2, int y2) {}

    private record Coord(int x, int y) {}
}
