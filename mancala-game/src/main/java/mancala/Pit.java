package mancala;

public class Pit implements Countable, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    // initalize a private instance variable to store number of stones in pit
    private int numStones;

    // initalizes a pit with zero stones
    public Pit(){
        numStones = 4;
    }

    // add a stone to pit
    @Override
    public void addStone(){
        numStones += 1;
    }

    // adds n amount of stones to the pit
    @Override
    public void addStones(final int stonesToAdd){
        numStones += stonesToAdd;
    }

    // sets a stone to the pit 
    public void setStone(final int newStone){
        numStones = newStone;
    }

    // get the number of stones in the pit
    @Override
    public int getStoneCount() {
        return numStones;
    }

    // removes and returns the stones from the pit
    @Override
    public int removeStones() {
        final int emptyPit = 0 + numStones;
        numStones = 0; 
        return emptyPit;
    }

    // returns a string representation of the object
    @Override
    public String toString() {
        return "Pit{" + numStones + "}";
    }

}