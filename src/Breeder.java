/**
 * Created by Paul Cloete on 3/19/2016.
 */

import java.util.*;

public class Breeder  {

    private int populationSize;
    private int numberOfEliteSolutions;
    private Vector<Solution> solutions;
    private final Random random = new Random(System.currentTimeMillis());


    public Breeder(Vector<Solution> solutions, int populationSize, int numberOfEliteSolutions) {
        //TODO parent selection configuration

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

        do {
            //Parent selection
            int firstParentIndex = this.random.nextInt(this.populationSize);
            int secondParentIndex = this.random.nextInt(this.populationSize);
            while(firstParentIndex == secondParentIndex){
                secondParentIndex = this.random.nextInt(this.populationSize);
            }

            Solution firstParent = this.solutions.get(firstParentIndex);
            Solution secondParent = this.solutions.get(secondParentIndex);

            //Breeding
            Queue<Solution> children = firstParent.breedWith(secondParent, this.random);

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
