package me.datafox.advent.day8;

import me.datafox.advent.SolutionBase;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Solution for advent of code day ?.
 *
 * @see <a href=https://adventofcode.com/2023/day/?>https://adventofcode.com/2023/day/?</a>
 *
 * @author datafox
 */
public class Solution extends SolutionBase {
    public static void main(String[] args) {
        new Solution().run();
    }

    @Override
    protected String solution1(String input) {
        String[] split = input.split("\n\n");
        char[] guide = split[0].toCharArray();
        Map<String,Node> nodes = new HashMap<>(split[1]
                .lines()
                .collect(Collectors.toMap(
                        this::getKey,
                        this::getNode)));
        int i = 0;
        String current = "AAA";
        while(!current.equals("ZZZ")) {
            if(guide[i % guide.length] == 'L') {
                current = nodes.get(current).left;
            } else {
                current = nodes.get(current).right;
            }
            i++;
        }
        return String.valueOf(i);
    }

    private String getKey(String line) {
        return line.substring(0, 3);
    }

    private Node getNode(String line) {
        return new Node(line.substring(7, 10), line.substring(12, 15));
    }

    @Override
    protected String solution2(String input) {
        return "";
    }

    private record Node(String left, String right) {}
}
