package Generic; /**
 * Created by Paul Cloete on 3/20/2016.
 */

import java.util.*;

public abstract class Solution implements Comparable{

    private static final double maximumMutationChance = 75.0;
    private static final double minimumMutationChance = 1;
    private static final double mutationIncrement = 2;
    private static double mutationChance = 75.0; //Start off with max exploration
    private static Solution mostFitSolution = null;

    public abstract  Vector<? extends Solution> breed(Solution mate, Random randomNumberGenerator);
    public abstract Comparator<Solution> fitnessComparator();
    public abstract void age();
    public abstract void mutate();
    public void print() {
        System.out.println("\tCurrent mutation chance: " + mutationChance);
    }

    Queue<Solution> breedWith(Solution mate, Random randomNumberGenerator) { //Assumes 2 parent breeding atm. Can be expanded at a later stage
        Vector<? extends Solution> children = this.breed(mate, randomNumberGenerator);
        if(mostFitSolution == null) {
            mostFitSolution = children.firstElement();
        }

        Queue<Solution> childrenToReturn = new PriorityQueue<>(children.size());

        //Determine if mutation should occur
        boolean shouldMutate = (randomNumberGenerator.nextDouble() * 100) < mutationChance;

        boolean foundBetterCandidate = false;
        for(Solution solution : children){
            //Mutate if necessary
            if(shouldMutate){
                solution.mutate();
            }

            //Check if better solution has been found
            if(this.fitnessComparator().compare(solution, mostFitSolution) > 0) {
                foundBetterCandidate = true;
                mostFitSolution = solution;
            }

            childrenToReturn.add(solution);

        }

        handleMutationChance(foundBetterCandidate);
        return childrenToReturn;
    }

    private void handleMutationChance(boolean foundBetterCandidate) {
        //Mutation adjustment.
        //This is an Adaptive GA technique which auto-adjusts the mutation chance. Every time we find a better
        //solution we INCREASE the mutation chance, because that maximises the exploration early on in the GA's
        //exploration phase. When we don't find a better solution (during exploitation phase) we DECREASE the mutation
        //rate, so during the exploitation phase where we very rarely find better solutions we don't mutate very
        //often.
        if(foundBetterCandidate){
            mutationChance += mutationIncrement;
        } else {
            mutationChance -= mutationIncrement;
        }

        //Check that current mutation chance is within bounds
        if(mutationChance > maximumMutationChance){
            mutationChance = maximumMutationChance;
        } else if(mutationChance < minimumMutationChance) {
            mutationChance = minimumMutationChance;
        }
    }

    @Override
    public int compareTo(Object sol) {
        return this.fitnessComparator().compare(this, (Solution)sol);
    }
}
