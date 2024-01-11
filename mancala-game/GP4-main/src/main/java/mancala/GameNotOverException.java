package mancala;

public class GameNotOverException extends Exception {
    
    private static final long serialVersionUID = 1L;

    public GameNotOverException() {
        super("The game is not over.");
    }
}