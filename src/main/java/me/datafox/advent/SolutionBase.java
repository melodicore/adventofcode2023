package me.datafox.advent;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author datafox
 */
public abstract class SolutionBase {
    public void run() {
        String in;
        try(InputStream input = getClass().getResourceAsStream("/input.txt")) {
            if(input == null) {
                System.out.println("No input.txt in resources");
                return;
            }
            in = new String(input.readAllBytes());
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Solution 1: \n" + solution1(in));
        System.out.println("Solution 2: \n" + solution2(in));
    }

    protected abstract String solution1(String input);

    protected abstract String solution2(String input);
}
