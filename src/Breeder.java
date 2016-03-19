/**
 * Created by Paul Cloete on 3/19/2016.
 */

import java.util.*;

public class Breeder  {

    private int populationSize;
    private int numberOfEliteCandidates;
    private Vector<Candidate> candidates;
    private final Random random = new Random(System.currentTimeMillis());

    public Breeder(Vector<Candidate> candidates, int populationSize, int numberOfEliteCandidates) {
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
            //TODO mutation
            int firstParentIndex = this.random.nextInt(this.populationSize);
            int secondParentIndex = this.random.nextInt(this.populationSize);
            Candidate firstParent = this.candidates.get(firstParentIndex);
            Candidate secondParent = this.candidates.get(secondParentIndex);

            Queue<Candidate> children = firstParent.breedWith(secondParent);
            int numberOfChildren = children.size();

            int indexOfFirstCandidateToRemove = this.populationSize - numberOfChildren;
            for (int i = this.numberOfEliteCandidates; i < this.populationSize; i++) { //Elite candidates don't age
                this.candidates.get(i).age();
                if(i == indexOfFirstCandidateToRemove){
                    this.candidates.setElementAt(children.poll(), i);
                }
            }

            sortCandidates(this.candidates);
        }while(true); //TODO end condition
    }

    private void sortCandidates(Vector<Candidate> candidates) {
        Comparator<Candidate> comparator = candidates.get(0).fitnessComparator();  //This assumes that the comparator each candidate returns is the same.
        candidates.sort(comparator);                                               //Alternative is to pass a comparator in when you create the breeder,
                                                                                    //But i prefer this method more
    }

}
