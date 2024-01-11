package mancala;

public class Store implements Countable, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int totalStones;
    private Player owner;

     public Store() {
        totalStones = 0;
    }

    public Store(final Player player) {
        this.owner = player;
    }

    @Override
    public void addStones(final int amount) {
        totalStones += amount;
    }
    
    @Override
    public void addStone() {
        totalStones += 1;
    }

    @Override
    public int removeStones() {
        final int retStones = 0 + totalStones;
        totalStones = 0;
        return retStones;
    }

    public int emptyStore() {
        final int stonesInStore = this.totalStones;
        this.totalStones = 0;
        return stonesInStore;
    }

    public Player getOwner() {
        return this.owner;
    }

    @Override
    public int getStoneCount() {
        return totalStones;
    }

    public int getTotalStones() {
        return totalStones;
    }

    public void setOwner(final Player player) {
        owner = player;
    }

    // returns string representation of the object
    @Override
    public String toString() {
        return "Store{" + "Stones=" + totalStones + ", Player=" + owner + '}';
    }
}