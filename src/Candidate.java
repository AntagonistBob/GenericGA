/**
 * Created by Paul Cloete on 3/19/2016.
 */

import java.util.Comparator;
import java.util.Queue;

interface Candidate {
    public Queue<Candidate> breedWith(Candidate mate); //Assumes 2 parent breeding atm. Can be expanded at a later stage
    public void mutate();
    public void age();
    public Comparator<Candidate> fitnessComparator();
}
