package me.datafox.advent2023.day8;

import me.datafox.advent2023.SolutionBase;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Solution for advent of code 2023 day 8.
 *
 * @see <a href=https://adventofcode.com/2023/day/8>https://adventofcode.com/2023/day/8</a>
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

    @Override
    protected String solution2(String input) {
        String[] split = input.split("\n\n");
        char[] guide = split[0].toCharArray();
        Map<String,Node> nodes = new HashMap<>(split[1]
                .lines()
                .collect(Collectors.toMap(
                        this::getKey,
                        this::getNode)));
        Collection<MappedNode> mappedNodes = mapNodes(nodes);
        MappedNode[] currents = mappedNodes
                .parallelStream()
                .filter(node -> node.id.endsWith("A"))
                .toArray(MappedNode[]::new);
        BigInteger[] repeats = new BigInteger[currents.length];
        for(int i = 0; i < currents.length; i++) {
            long counter = 0;
            while(true) {
                currents[i] = guide[(int) (counter % guide.length)] == 'L' ? currents[i].left : currents[i].right;
                counter++;
                if(currents[i].id.endsWith("Z")) {
                    repeats[i] = BigInteger.valueOf(counter);
                    break;
                }
            }
        }
        BigInteger result = BigInteger.ZERO;
        for(BigInteger bi : repeats) {
            if(result.equals(BigInteger.ZERO)) {
                result = bi;
            } else {
                BigInteger gcd = result.gcd(bi);
                BigInteger abs = result.multiply(bi).abs();
                result = abs.divide(gcd);
            }
        }
        
        return result.toString();
    }

    private String getKey(String line) {
        return line.substring(0, 3);
    }

    private Node getNode(String line) {
        return new Node(line.substring(7, 10), line.substring(12, 15));
    }

    private Collection<MappedNode> mapNodes(Map<String,Node> map) {
        Map<String,MappedNode> temp = map
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        MappedNode::new));
        map.forEach((id, node) -> {
            MappedNode mapped = temp.get(id);
            mapped.left = temp.get(node.left);
            mapped.right = temp.get(node.right);
        });
        return temp.values();
    }

    private record Node(String left, String right) {}

    private static class MappedNode {
        private final String id;
        private MappedNode left, right;

        private MappedNode(String id) {
            this.id = id;
        }
    }
}
