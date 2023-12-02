package me.datafox.advent;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author datafox
 */
public abstract class SolutionBase {
    public void run() {
        try(InputStream input = getClass().getResourceAsStream("/input.txt")) {
            if(input == null) {
                System.out.println("No input.txt in resources");
                return;
            }
            System.out.println(solution(new String(input.readAllBytes())));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String solution(String input);
}
