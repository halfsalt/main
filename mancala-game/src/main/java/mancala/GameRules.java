package mancala;

import java.io.Serializable;

/**
 * Abstract class representing the rules of a Mancala game.
 * KalahRules and AyoRules will subclass this class.
 */
public abstract class GameRules implements Serializable {
    private static final long serialVersionUID = 1L;
    private final MancalaDataStructure gameBoard;
    private static final int FRST_PLYR_START_PIT = 1;
    private static final int SCND_PLYR_START_PIT = 7;
    private static final int PITS_PER_PLAYER = 6;
    private int currentPlayer = 1; // Player number (1 or 2)

    /* Constructor to initialize the game board */
    public GameRules() {
        gameBoard = new MancalaDataStructure();
    }

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(final int pitNum) {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */
    MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

 
    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */

    // method to check if one side of the board is empty
     boolean isSideEmpty(final int pitNum) {
        int startPit; // determines starting pit

        if (pitNum <= PITS_PER_PLAYER) {
            startPit = FRST_PLYR_START_PIT; // if the pit number is on player 1's side, start checking from pit 1
        } else {
            startPit = SCND_PLYR_START_PIT; // if the pit number is on player 2's side, start checking from pit 7
        }

        for (int i = 0; i < PITS_PER_PLAYER; i++) {
            if (getNumStones(startPit + i) != 0) {
                return false; // return false as soon as a non-empty pit is found
            }
        }

        return true; // return true if no non-empty pits are found
    }


    /**
     * Set the current player.
     *
     * @param playerNum The player number (1 or 2).
     */
    public void setPlayer(final int playerNum) {
        currentPlayer = playerNum;
    }

    // retrieves current player 
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    public abstract int moveStones(int startPit, int playerNum) throws InvalidMoveException;

    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    abstract int distributeStones(int startPit);

    // new abstract method to check for a free turn
    public abstract boolean isFreeTurn();

    /*
        abstract method that checks who the store belongs to
    */
    abstract int checkOwnStore(int index);

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    abstract int captureStones(int stoppingPoint);

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    public void registerPlayers(final Player one, final Player two) {

        // makes a new store and sets the owner
        final Store storeOne = new Store(one);
        final Store storeTwo = new Store(two);
        one.setStore(storeOne);
        two.setStore(storeTwo);
        
        // then use the setStore method of the data structure
        gameBoard.setStore(storeOne,1);
        gameBoard.setStore(storeTwo,2);
    }

    // method for extending
    public MancalaDataStructure giveGameBoard() {
        return gameBoard;
    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
       // gameBoard.setUpPits();
        gameBoard.emptyStores();
    }

    public boolean shoulddSwitchPlayer() {
        return true; // default implementation
    }

   
    // Return a toString() representation of the board
    @Override
    public String toString() {
        String boardString = "\n"; 
        try {
            int i;
            for (i = 12; i > 6; i--) {
                boardString += getNumStones(i) + "  ";
            }
            boardString += "\n" + gameBoard.getStoreCount(2) + "\t\t" + gameBoard.getStoreCount(1) + "\n";
            for (i = 1; i <= 6; i++) {
                boardString += getNumStones(i) + "  ";
            }
        } catch (Exception e) {
        }

        return boardString;
    }

}