package mancala;

import java.io.Serializable;

public class UserProfile implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String playerName;
    private int kalahGamesPlayed;
    private int ayoGamesPlayed;
    private int kalahGamesWon;
    private int ayoGamesWon;
    private int kalahGamesLost;
    private int ayoGamesLost;


    // constructor to initialize a new user profile 
    public UserProfile(final String playerName) {
        this.playerName = playerName;
        this.kalahGamesPlayed = 0;
        this.ayoGamesPlayed = 0;
        this.kalahGamesWon = 0;
        this.ayoGamesWon = 0;
        this.ayoGamesLost = 0;
        this.kalahGamesLost = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getKalahGamesPlayed() {
        return kalahGamesPlayed;
    }

    public int getAyoGamesPlayed() {
        return ayoGamesPlayed;
    }

    public int getKalahGamesWon() {
        return kalahGamesWon;
    }

    public int getAyoGamesWon() {
        return ayoGamesWon;
    }

    public void setPlayerName(final String playerName) {
        this.playerName = playerName;
    }

    public void incrementKalahGamesPlayed() {
        this.kalahGamesPlayed++;
    }

    public void incrementAyoGamesPlayed() {
        this.ayoGamesPlayed++;
    }

    public void incrementKalahGamesWon() {
        this.kalahGamesWon++;
    }

    public void incrementAyoGamesWon() {
        this.ayoGamesWon++;
    }

     // Methods to increment the losses
    public void incrementKalahGamesLost() {
        this.kalahGamesLost++;
    }

    public void incrementAyoGamesLost() {
        this.ayoGamesLost++;
    }

    // Getters and setters for the losses
    public int getKalahGamesLost() {
        return kalahGamesLost;
    }

    public int getAyoGamesLost() {
        return ayoGamesLost;
    }

    @Override
    public String toString() {
        return "UserProfile {" +
               "Username = '" + this.playerName + '\'' +
               ", # of KalahGamesPlayed = " + this.kalahGamesPlayed +
               ", # of AyoGamesPlayed = " + this.ayoGamesPlayed +
               ", # of KalahGamesWon = " + this.kalahGamesWon +
               ", # of AyoGamesWon = " + this.ayoGamesWon +
               ", # of KalahGamesLost = " + this.kalahGamesLost +
               ", # of AyoGamesLost = " + this.ayoGamesLost +
               '}';
    }
}