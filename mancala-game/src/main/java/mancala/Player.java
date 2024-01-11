package mancala;

public class Player implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    // initalize private instance variables 
    private String playerName;
    private Store playerStore;

    // constructor to initalize a new player
    public Player() {
        playerName = "";
        playerStore = new Store();
    }
    
    // constructor to initalize a new player with a name 
    public Player(final String setName){
        playerName = setName;
        playerStore = new Store();
    }

    // returns player name
    public String getName(){
        return playerName;
    }

    public void setName(final String setName) {
        this.playerName = setName;
    }

    // gets the player's store where they collect stones
    public Store getStore(){
        return playerStore;
    }

    public void setStore(final Store setStore) {
        this.playerStore = setStore;
    }

    public int getStoreCount() {
        return playerStore.getTotalStones();
    }

   
   @Override
    public String toString() {
        return "Player{"+"name='" + playerName + '\'' +", store=" + playerStore +'}';
    }
}