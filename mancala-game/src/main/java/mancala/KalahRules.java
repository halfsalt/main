package mancala;

import java.util.List;

public class KalahRules extends GameRules {

    private static final long serialVersionUID = 1L;
    private final MancalaDataStructure gameBoard; // holds game's data structure
    private final List<Countable> listStonesPits; // list of pits and stores
    private int finalPit; // final pit where a stone ends
    private Boolean bonusTurn; // whether a player gets a bonus turn or not
    private boolean lastMoveWasSteal; // whether a steal occured or not
    private static final int MIN_PIT_NUM = 1;
    private static final int MAX_PIT_NUM = 12;
    private static final int SPECIAL_PIT_TWO = 13;
    private static final int PLAYER_ONE_PIT = 6;
    

    public KalahRules(){
        super();
        gameBoard = getDataStructure();
        listStonesPits = gameBoard.getCountables();
    }

    @Override
    public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException {
        // validate starting pit
        if (startPit < MIN_PIT_NUM || startPit > MAX_PIT_NUM) { 
            throw new InvalidMoveException();
        }

        bonusTurn = false;  // resets bonus turn flag
        lastMoveWasSteal = false; // resets steal flag
        setPlayer(playerNum); // calls method to set the current player for the move
        distributeStones(startPit); // distributes stones from the chosen pit

        if (finalPit == PLAYER_ONE_PIT || finalPit == SPECIAL_PIT_TWO) {
            bonusTurn = true;
        } else {
            finalPit++;
            // to check if there is a steal
            if (gameBoard.getNumStones(finalPit) == MIN_PIT_NUM) {
                if (finalPit > 6 && playerNum == 2) {
                    gameBoard.addToStore(playerNum, captureStones(finalPit));
                    lastMoveWasSteal = true;
                } else if (finalPit <= 6 && playerNum == 1) {
                    gameBoard.addToStore(playerNum, captureStones(finalPit));
                    lastMoveWasSteal = true;
                }
            }
        }

        return gameBoard.getStoreCount(playerNum);
    }

    @Override
    public int distributeStones(final int startPit) {
        // distribute stones into pits and stores, skipping the opponent's store
        int currentPit = startPit;
        if (currentPit <= PLAYER_ONE_PIT) {
                currentPit--;
        }
        
        // removes stones from the starting pit and track the count of stones distributed.
        final int capturedStones = listStonesPits.get(currentPit).removeStones();
        int count = 0;
        int index = currentPit;
        
        // distributes stones one by one until all stolen stones are distributed.
        while (count != capturedStones) {
            count += 1;
            index += 1;
            index = checkOwnStore(index); // checks if valid location for stone distribution
        }

        finalPit = index;
        return capturedStones;
    }

    @Override
    public int captureStones(final int stoppingPoint) {

        // calculate opposite pit number
        final int oppositePit = 13 - stoppingPoint;

        // calculates captured stones from both the stopping point and opposite pit
        return gameBoard.removeStones(oppositePit) + gameBoard.removeStones(stoppingPoint);

    }

    // return the current state of bonusTurn
    @Override
    public boolean isFreeTurn() {
        return bonusTurn;
    }

    @Override
    public int checkOwnStore(final int index) {
        int currentIndex = index;
         if (currentIndex == 6 && getCurrentPlayer() == 1) {
            //if the location is player 1's store, add a stone
            listStonesPits.get(currentIndex).addStone();
            } else if (currentIndex == 13 && getCurrentPlayer() == 2) {
                // if the location is player 2's store, add a stone
                listStonesPits.get(currentIndex).addStone();
            } else {
                if (currentIndex == PLAYER_ONE_PIT) {
                    // skips opponent's store if it's player 2's turn
                    currentIndex = 7;
                } else if (currentIndex == SPECIAL_PIT_TWO) {
                    // loops back to the start if it's player 1's turn and location is at the end
                    currentIndex = 0;
                } 
                    
                // for any other location, add a stone and move to the next location
                listStonesPits.get(currentIndex).addStone();  
        }
        
        return currentIndex;
    }

    public boolean isLastMoveSteal() {
        return lastMoveWasSteal;
    }

}