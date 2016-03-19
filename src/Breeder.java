import java.util.ArrayList;

/**
 * Created by Paul Cloete on 3/19/2016.
 */
public class Breeder  {

    private int populationSize;
    private int numberOfEliteCandidates;
    private ArrayList<Candidate>candidates;

    public Breeder() {
        //Initialize with population + size + number of elite units
        //Initialize population
        //Evaluate current population (sort)
        //Breed loop
            //Select parents
            //Breed parents
            //Mutate if necessary
            //Evaluate child/ren
            //Replace random/weakest unit in population with child
            //Age?
        //Repeat
    }

    public Breeder(ArrayList<Candidate> candidates, int populationSize, int numberOfEliteCandidates) {
        this.candidates = candidates;
        this.populationSize = populationSize;
        this.numberOfEliteCandidates = numberOfEliteCandidates;
    }



}
