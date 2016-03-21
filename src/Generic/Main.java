package Generic;

import Einstein.Einstein;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Paul Cloete on 3/19/2016.
 */

public class Main {

    public static void main(String [] args) {
        Random random = new Random(System.currentTimeMillis());
        Vector<Solution> solutions = new Vector<>(100);
        System.out.println("Setting up...");
        for(int i = 0; i < 100; i++){
            Einstein einstein = new Einstein(random, 675, 900);
            solutions.add(einstein);
        }

        System.out.println("Setup complete!");
        Breeder breeder = new Breeder(random, solutions, 100, 5);
    }

}
