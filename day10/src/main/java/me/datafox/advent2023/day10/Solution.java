package me.datafox.advent2023.day10;

import me.datafox.advent2023.SolutionBase;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Solution for advent of code 2023 day 10.
 *
 * @see <a href=https://adventofcode.com/2023/day/10>https://adventofcode.com/2023/day/10</a>
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    private static final Map<Character,Set<Coord>> pipes = Map.of('.', Set.of(),
            '|', Set.of(new Coord(0, -1), new Coord(0, 1)),
            '-', Set.of(new Coord(-1, 0), new Coord(1, 0)),
            'L', Set.of(new Coord(0, -1), new Coord(1, 0)),
            'J', Set.of(new Coord(0, -1), new Coord(-1, 0)),
            '7', Set.of(new Coord(0, 1), new Coord(-1, 0)),
            'F', Set.of(new Coord(0, 1), new Coord(1, 0)));
    private static final Set<Coord> directions = Set.of(
            new Coord(0, -1),
            new Coord(0, 1),
            new Coord(-1, 0),
            new Coord(1, 0));

    public static void main(String[] args) {
        new Solution().run();
    }

    @Override
    protected String solution1(String input) {
        char[][] map = input.lines().map(String::toCharArray).toArray(char[][]::new);
        Coord start = getChar(map, 'S');
        changeToPipe(map, start);
        Set<Coord> visited = new HashSet<>();
        visited.add(start);
        Coord[] current = new Coord[] { start, start };
        int counter = 0;
        while(true) {
            counter++;
            for(int i = 0; i < 2; i++) {
                Optional<Coord> o =  pipes
                        .get(current[i].getChar(map))
                        .stream()
                        .map(current[i]::add)
                        .filter(Predicate.not(visited::contains))
                        .findAny();
                if(o.isEmpty()) {
                    return String.valueOf(counter);
                }
                current[i] = o.get();
                visited.add(o.get());
            }
        }
    }

    @Override
    protected String solution2(String input) {
        char[][] map = input.lines().map(String::toCharArray).toArray(char[][]::new);
        Coord start = getChar(map, 'S');
        changeToPipe(map, start);
        Set<Coord> visited = new HashSet<>();
        visited.add(start);
        Coord[] current = new Coord[] { start, start };
        wh: while(true) {
            for(int i = 0; i < 2; i++) {
                Optional<Coord> o =  pipes
                        .get(current[i].getChar(map))
                        .stream()
                        .map(current[i]::add)
                        .filter(Predicate.not(visited::contains))
                        .findAny();
                if(o.isEmpty()) {
                    break wh;
                }
                current[i] = o.get();
                visited.add(o.get());
            }
        }
        for(int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[y].length; x++) {
                if(!visited.contains(new Coord(x, y))) {
                    map[y][x] = '.';
                }
            }
        }
        return String.valueOf(countInside(map));
    }

    private Coord getChar(char[][] map, char c) {
        for(int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[y].length; x++) {
                if(map[y][x] == c) {
                    return new Coord(x, y);
                }
            }
        }
        return new Coord(0, 0);
    }

    private void changeToPipe(char[][] map, Coord coord) {
        if(coord.isOutOfBounds(map)) {
            return;
        }
        if(pipes.containsKey(coord.getChar(map))) {
            return;
        }
        Set<Coord> neighbors = new HashSet<>();
        for(Coord dir :directions) {
            Coord c = coord.add(dir);
            if(c.isOutOfBounds(map)) {
                continue;
            }
            if(pipes.get(c.getChar(map)).stream().map(c::add).anyMatch(coord::equals)) {
                neighbors.add(c.sub(coord));
            }
        }
        pipes.entrySet()
                .stream()
                .filter(e -> neighbors.equals(e.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(c -> coord.setChar(map, c));
    }

    private int countInside(char[][] map) {
        int counter = 0;
        boolean inside = false;
        char lastCorner = ' ';
        for(char[] chars : map) {
            for(char c : chars) {
                if(c == '.' && inside) {
                    counter++;
                    continue;
                }
                if(c == '|') {
                    inside = !inside;
                }
                if(c == 'L' || c == 'F') {
                    lastCorner = c;
                }
                if(c == 'J' && lastCorner == 'F' || c == '7' && lastCorner == 'L') {
                    inside = !inside;
                }
            }
        }
        return counter;
    }

    private record Coord(int x, int y) {
        private Coord add(Coord c) {
            return new Coord(x + c.x, y + c.y);
        }

        private Coord sub(Coord c) {
            return new Coord(x - c.x, y - c.y);
        }

        private boolean isOutOfBounds(char[][] map) {
            return y < 0 || x < 0 || y >= map.length || x >= map[y].length;
        }

        private char getChar(char[][] map) {
            return map[y][x];
        }

        private void setChar(char[][] map, char c) {
            map[y][x] = c;
        }
    }
}
