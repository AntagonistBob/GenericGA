/**
 * Created by Paul Cloete on 3/20/2016.
 */

import java.util.*;

abstract class Solution {

    private final double maximumMutationChance = 75.0;
    private final double minimumMutationChance = 1;
    private final double mutationIncrement = 2;
    private double mutationChance = this.maximumMutationChance; //Start off with max exploration
    private static Solution mostFitSolution = null;

    abstract Vector<Solution> breedWith(Solution mate);
    abstract Comparator<Solution> fitnessComparator();
    abstract void age();
    abstract void mutate();

    Queue<Solution> breedWith(Solution mate, Random randomNumberGenerator) { //Assumes 2 parent breeding atm. Can be expanded at a later stage
        Vector<Solution> children = this.breedWith(mate);
        if(mostFitSolution == null) {
            mostFitSolution = children.firstElement();
        }

        Queue<Solution> childrenToReturn = new PriorityQueue<Solution>(children.size());

        //Determine if mutation should occur
        boolean shouldMutate = (randomNumberGenerator.nextDouble() * 100) < this.mutationChance;

        boolean foundBetterCandidate = false;
        for(Solution solution : children){
            //Mutate if necessary
            if(shouldMutate){
                solution.mutate();
            }

            //Check if better solution has been found
            if(this.fitnessComparator().compare(solution, mostFitSolution) > 0) {
                foundBetterCandidate = true;
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
            this.mutationChance += mutationIncrement;
        } else {
            this.mutationChance -= mutationIncrement;
        }

        //Check that current mutation chance is within bounds
        if(this.mutationChance > this.maximumMutationChance){
            this.mutationChance = this.maximumMutationChance;
        } else if(this.mutationChance < this.minimumMutationChance) {
            this.mutationChance = this.minimumMutationChance;
        }
    }

}
