package mancala;

import java.io.Serializable;

public class MancalaGame implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private final MancalaDataStructure gameBoard;
    private GameRules rules;
    private boolean moveOver;
    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;
    private boolean gameStarted = false; 
    private final GameType gameType;

    // to distinguish between Kalah and Ayo
    public enum GameType {
        KALAH,
        AYO
    }

    // constructor to initalize a new Kalah/Ayo game
    public MancalaGame(final GameType gameType) {
        this.gameType = gameType;
        switch (gameType) {
            case KALAH:
                this.rules = new KalahRules();
                break;
            case AYO:
                this.rules = new AyoRules();
                break;
            default:
                throw new IllegalArgumentException("Invalid game type");
        }

        this.gameBoard = rules.giveGameBoard();
        this.playerOne = new Player("");
        this.playerTwo = new Player("");
        this.currentPlayer = playerOne;
        this.moveOver = false;
    }


    // method that provides access to gameBoard 
    public MancalaDataStructure getGameBoardDataStructure() {
        return this.rules.giveGameBoard();
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    // method to set players for game
    public void setPlayers(final Player onePlayer, final Player twoPlayer) {
        if (!onePlayer.getName().isBlank() && !twoPlayer.getName().isBlank()) {
            playerOne = onePlayer;
            playerTwo = twoPlayer;
            currentPlayer = playerOne;
            rules.registerPlayers(onePlayer, twoPlayer); // registers the players with the rules
            
        }
    }

    // method to get the current player
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // method that sets current player 
    public void setCurrentPlayer(final Player player) {
        // Check if the player is one of the two players in the game
        if (player != null && (player.equals(playerOne) || player.equals(playerTwo))) {
            currentPlayer = player;
            rules.setPlayer(player.equals(playerOne) ? 1 : 2);
        }
    }

    // method that sets the board for the game
    public void setBoard(final GameRules theBoard) {
        rules = theBoard;
    }

    // method that gets the board for the game 
    public GameRules getBoard() {
        return rules;
    }

    // method that gets the number of stones in a specific pit
    public int getNumStones(final int pitNum){

        // call the corresponding method in the Board class
        return gameBoard.getNumStones(pitNum);
    }

    // method that makes a move for the current player
    public int move(final int startPit) throws InvalidMoveException {

        int numStones;
        try {
            final int currNum = currentPlayer.equals(playerTwo) ? 2 : 1;
            numStones = rules.moveStones(startPit, currNum); // executes the move based on the rules

            // switch players if no bonus turn is awarded
            if (!rules.isFreeTurn()) {
                switchPlayer();
            }

        } catch (Exception e) {
            numStones = -1; // in case of error, returns -1
        }

        return numStones;
    }

    // method that gets the winner of the game
    public Player getWinner() throws GameNotOverException {
        if (!isGameOver()) {
            throw new GameNotOverException();
        }

        Player winner = null; // Initialize winner to null

        // calculates the stone count in each player's store
        final int stonesPlayer1 = this.gameBoard.getStoreCount(1);
        final int stonesPlayer2 = this.gameBoard.getStoreCount(2);


        // determines the winner based on the stone count
        if (stonesPlayer1 > stonesPlayer2) {
            winner = playerOne;
        } else if (stonesPlayer2 > stonesPlayer1) {
            winner = playerTwo;
        }

        return winner; // return the winner (or null if it's a tie)
    }

    // method that checks if the game is over
    public boolean isGameOver() {
        try {

            if (rules.isSideEmpty(1) || rules.isSideEmpty(7)) {
                moveOver = true;
            }
        } catch (Exception e){
            moveOver = false;
        }
        
        return moveOver;
    }

    // method that starts a new game by resetting the board 
    public void startNewGame() {
        if (gameType == GameType.KALAH) {
            rules.resetBoard(); 
        } else if (gameType == GameType.AYO) {
            getGameBoardDataStructure().clearPits();
            getGameBoardDataStructure().setUpAyoBoard();
        }
        gameStarted = true;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

     // method that gets the total number of stones in a player's store
    public int getStoreCount(final Player player) {
        return player.getStoreCount();
    }

    // Switches the current player
    private void switchPlayer() {
        if (currentPlayer.equals(playerOne)) {
            currentPlayer = playerTwo;
            rules.setPlayer(2); // Assuming setPlayer(int) sets the current player in the rules
        } else {
            currentPlayer = playerOne;
            rules.setPlayer(1);
        }
    }

    // method to get the current game type
    public GameType getGameType() {
        return gameType;
    }

    @Override
    public String toString() {
        return "Game:\n" + rules.toString(); 
    }

}