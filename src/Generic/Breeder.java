package Generic; /**
 * Created by Paul Cloete on 3/19/2016.
 */

import java.util.*;

public class Breeder  {

    private int populationSize;
    private int numberOfEliteSolutions;
    private Vector<Solution> solutions;
    private Random randomNumberGenerator;

    public Breeder(Random random, Vector<Solution> solutions, int populationSize, int numberOfEliteSolutions) {
        //TODO parent selection configuration, and remove random number generator
        this.randomNumberGenerator = random;
        this.solutions = solutions;
        this.populationSize = populationSize;
        if(this.solutions.size() != populationSize){
            //TODO throw some sort of inconsistency exception
        }

        this.numberOfEliteSolutions = numberOfEliteSolutions;

        sortSolutions(this.solutions);
        breed();
    }

    private void breed() {
        //TODO threading, using thread safe data structures. requires some more thought
        //TODO optimization, requires some thought

        int numberOfBreedingLoops = 0;
        do {
            System.out.println("Breed loop " + numberOfBreedingLoops);
            //Parent selection
            int firstParentIndex = this.randomNumberGenerator.nextInt(this.populationSize);
            int secondParentIndex = this.randomNumberGenerator.nextInt(this.populationSize);
            while(firstParentIndex == secondParentIndex){
                secondParentIndex = this.randomNumberGenerator.nextInt(this.populationSize);
            }

            Solution firstParent = this.solutions.get(firstParentIndex);
            Solution secondParent = this.solutions.get(secondParentIndex);

            //Breeding
            Queue<Solution> children = firstParent.breedWith(secondParent, this.randomNumberGenerator);

            //Aging and adding children to population. Starts at the back of the list so as to replace worst
            //members of population with new children.
            for (int i = this.populationSize - 1; i >= this.numberOfEliteSolutions; i--) { //Elite solutions don't age
                this.solutions.get(i).age();
                if(children.size() > 0){
                    this.solutions.setElementAt(children.remove(), i);
                }
            }

            //Fitness re-evaluation
            sortSolutions(this.solutions);

            //Print the best solution if necessary
            if(numberOfBreedingLoops % 100 == 0) {
                System.out.println("Best candidate so far:");
                this.solutions.get(0).print();
            }

            numberOfBreedingLoops++;
        } while(true); //TODO end condition
    }

    private void sortSolutions(Vector<Solution> solutions) {
        //This assumes that the comparator each solution returns is the same.
        //An alternative is to pass a comparator in when you create the breeder,
        //but i prefer this method more
        Comparator<Solution> comparator = solutions.get(0).fitnessComparator();
        solutions.sort(comparator);
    }

}
