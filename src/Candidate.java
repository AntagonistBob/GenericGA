/**
 * Created by Paul Cloete on 3/19/2016.
 */
interface Candidate {
    public Candidate[] breedWith(Candidate[] parents);
    public String fitness();
    public void mutate();
}
