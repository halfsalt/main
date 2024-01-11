package mancala;

import java.util.List;

public class AyoRules extends GameRules {
    
    private static final long serialVersionUID = 1L;

    private final MancalaDataStructure gameBoard; // holds game's data structure
    private final List<Countable> listStonesPits; // list of pits and stores
    private int finalPit; // final pit where a stone ends
    private int skipPit; // skips pit based of Ayo Rules

    private static final int MIN_PIT_NUMBER = 1;
    private static final int MAX_PIT_NUMBER = 12;
    private static final int SAFETY_COUNTER = 16;
    private static final int SPECIAL_PIT_ONE = 6;
    private static final int SPECIAL_PIT_TWO = 13;

    public AyoRules(){
        super();
        gameBoard = getDataStructure();
        listStonesPits = gameBoard.getCountables();
    }

    @Override
    public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException {
        // validate starting pit
        int currentPit = startPit;
        skipPit = startPit;
        if (currentPit < MIN_PIT_NUMBER  || currentPit > MAX_PIT_NUMBER) { 
            throw new InvalidMoveException();
        } else {
            boolean isTurnOver = false; // indicate if turn is ober
            setPlayer(playerNum); // sets current player for the move
            int safetyCounter = 0;

            // main loop for distributing stones
            while (!isTurnOver) {
                safetyCounter++;
                if (safetyCounter == SAFETY_COUNTER) { // prevent infinite loops
                    return -1;
                }
                // distribute stones starting from the chosen pit
                distributeStones(currentPit);

                // checks if the turn should continue or not
                if (finalPit != SPECIAL_PIT_ONE  && finalPit != SPECIAL_PIT_TWO) { 
                    if (gameBoard.getNumStones(finalPit) != MIN_PIT_NUMBER) {
                        // continue distributing if the final location was not empty
                        currentPit = finalPit;
                    } else {
                        isTurnOver = true; // end the turn if the final location was empty
                    }
                }

            }
       
            // handling captures if the final stone lands in an empty pit on the player's side
            if (finalPit != 6 && finalPit != 13) {
               // finalPit++;
                if (gameBoard.getNumStones(finalPit) == MIN_PIT_NUMBER) {
                    if (finalPit > 6 && playerNum == 2) {
                        gameBoard.addToStore(playerNum, captureStones(finalPit));
                    } else if (finalPit <= 6 && playerNum == 1) {
                        gameBoard.addToStore(playerNum, captureStones(finalPit));
                    }
                }
            }
        }

            return gameBoard.getStoreCount(playerNum);
    }
    
    @Override
    public int distributeStones(final int startPit) {
        int currentPit = startPit;
        // distribute stones into pits and stores, skipping the opponent's store.
        if (currentPit <= SPECIAL_PIT_ONE) {
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
        return gameBoard.removeStones(oppositePit);
    }

    @Override
    public boolean isFreeTurn() {
        // since free turn does not apply to AyoRules, always return false
        return false;
    }

    @Override
    public int checkOwnStore(final int index) {
        int currentIndex = index;
        // method that checks if its a store and adds stone to that store
        if (currentIndex == skipPit - 1) {
            currentIndex += 1;
        }

        // if the location is player 1's store, add a stone
        if (currentIndex == 6 && getCurrentPlayer() == 1) {     
            listStonesPits.get(currentIndex).addStone();   
        } else if (currentIndex == 13 && getCurrentPlayer() == 2){
            listStonesPits.get(currentIndex).addStone();
        } else {
            if (currentIndex == SPECIAL_PIT_ONE){
                currentIndex = 7;
            } else if (currentIndex == SPECIAL_PIT_TWO){
                currentIndex = 0;
            }

            listStonesPits.get(currentIndex).addStone();
        }

        return currentIndex;
    }

}
