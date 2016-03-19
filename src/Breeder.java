/**
 * Created by Paul Cloete on 3/19/2016.
 */

import java.util.*;

public class Breeder  {

    private int populationSize;
    private int numberOfEliteCandidates;
    private Vector<Candidate> candidates;
    private final Random random = new Random(System.currentTimeMillis());
    private final double maximumMutationChance = 75.0;
    private final double minimumMutationChance = 1;
    private final double mutationIncrement = 2;
    private double mutationChance = this.maximumMutationChance; //Start off with max exploration

    public Breeder(Vector<Candidate> candidates, int populationSize, int numberOfEliteCandidates) {
        //TODO decide on how to configure the breeder to handle mutation. Ideally I would like it if we never
        //have to subclass this breeder class, but instead pass it some sort of MutationConfiguration object
        //which will handle the mutation. That way whenever we create a new GA we should actually only have to create
        //two new files - one Candidate implementation and one MutationConfiguration.

        this.candidates = candidates;
        this.populationSize = populationSize;
        if(this.candidates.size() != populationSize){
            //TODO throw some sort of inconsistency exception
        }

        this.numberOfEliteCandidates = numberOfEliteCandidates;

        sortCandidates(this.candidates);
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

            Candidate firstParent = this.candidates.get(firstParentIndex);
            Candidate secondParent = this.candidates.get(secondParentIndex);

            //Breeding
            Queue<Candidate> children = firstParent.breedWith(secondParent);

            //Determine if mutation should occur
            boolean shouldMutate = (this.random.nextDouble() * 100) < this.mutationChance;
            boolean foundBetterCandidate = false;

            //Aging and adding children to population. Starts at the back of the list so as to replace worst
            //members of population with new children.
            for (int i = this.populationSize - 1; i >= this.numberOfEliteCandidates; i--) { //Elite candidates don't age
                this.candidates.get(i).age();
                if(children.size() > 0){
                    Candidate child = children.remove();

                    //Mutate
                    if(shouldMutate){
                        child.mutate();
                    }

                    this.candidates.setElementAt(child, i);
                    foundBetterCandidate = determineIfChildIsElite(child);
                }
            }

            //Update mutation chance
            handleMutationChance(foundBetterCandidate);

            //Fitness re-evaluation
            sortCandidates(this.candidates);
        } while(true); //TODO end condition
    }

    private void sortCandidates(Vector<Candidate> candidates) {
        //This assumes that the comparator each candidate returns is the same.
        //An alternative is to pass a comparator in when you create the breeder,
        //but i prefer this method more
        Comparator<Candidate> comparator = candidates.get(0).fitnessComparator();
        candidates.sort(comparator);
    }

    private boolean determineIfChildIsElite(Candidate child) {
        //Get the worst elite candidate and check if the child is better than or equal to the elite
        Candidate worstEliteCandidate = this.candidates.get(this.numberOfEliteCandidates - 1);
        if(child.fitnessComparator().compare(child, worstEliteCandidate) >= 0){
            return true;
        }

        return false;
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

        //Check that current mutaton chance is within bounds
        if(this.mutationChance > this.maximumMutationChance){
            this.mutationChance = this.maximumMutationChance;
        } else if(this.mutationChance < this.minimumMutationChance) {
            this.mutationChance = this.minimumMutationChance;
        }
    }

}
