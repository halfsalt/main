package ui;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;

import mancala.MancalaGame;
import mancala.Player;
import mancala.Store;
import mancala.Pit;
import mancala.Saver;
import mancala.KalahRules;
import mancala.UserProfile;
import mancala.MancalaGame.GameType;
import mancala.InvalidMoveException;
import mancala.GameNotOverException;
import java.io.IOException;

public class TextUI extends JFrame {

    // instance variables used to store references to GUI components
    private JButton submitButton;
    private JButton playKalahButton;
    private JButton playAyoButton;
    private JTextField username;
    private JLabel statusLabel;
    private JLabel currentPlayer;

    private JLabel storeP1;
    private JLabel storeP2;

    // instance variables used to keep track of and managing/storing states
    private boolean isButtonEnabled;
    private String userInputText;
    private int selectedComboBoxIndex;

    // instance variables used to create custom GUI components
    private PositionAwareButton[][] buttons;

    private JPanel gameContainer;
    private JPanel boardPanel;
    private JLabel messageLabel;
    private JMenuBar menuBar;
    private MancalaGame game;

    private UserProfile playerOneProfile;
    private UserProfile playerTwoProfile;

    private final String kalahRules =   "Kalah Rules:\n\n" +
                                        "1. If you run into your own store, deposit one piece in it. If you run into your opponent’s store, skip it.\n" +
                                        "2. If the last piece you drop is in your own store, you get a free turn.\n" +
                                        "3. If the last piece you drop is in an empty hole on your side, you capture that piece and any pieces in the hole directly opposite.\n" +
                                        "4. Always place all captured pieces in your store.\n";

    private final String ayoRules = "Ayo (Oware) Rules:\n\n" +
                                    "1. The Ayo board consists of two rows of six pits. Each player controls one row.\n" +
                                    "2. The game starts with four stones in each hole.\n" +
                                    "3. Players take turns to pick up all stones from a hole on their side and 'sow' them, one in each hole, in a clockwise direction.\n" +
                                    "4. If the last sown seed lands in a hole with two or three stones, these seeds are captured and removed from the board.\n" +
                                    "5. The game ends when one player cannot move (i.e., all holes on their side are empty), or when only single seeds are left on the board.\n" +
                                    "6. The player who captures the most stones wins the game.";

    private String getStoreButtonText (int playerNum){
        return "Player" + playerNum + "s Store [" + game.getGameBoardDataStructure().getStoreCount(playerNum) + "]";
    }

    // superclass constructor 
    public TextUI(String title) {
        super();
        gameSetup(title);
        setupGameContainer(true);

        // initialize and add the current player label
        currentPlayer = new JLabel("Current Player: ");
        gameContainer.add(currentPlayer, BorderLayout.PAGE_START);

        playKalahButton = makeKalahButton();
        playAyoButton = makeAyoButton();

        makeMenu(); 
        setJMenuBar(menuBar); 
        add(gameContainer, BorderLayout.CENTER);
        add(makeButtonPanel(), BorderLayout.EAST);
        pack();
        setVisible(true);
    }


    // used by the UI to initialize the frame and set the basic properties
    private void gameSetup(String title) {
        this.setTitle(title); // sets the title of the GUI window
        gameContainer = new JPanel(); //  creates a new JPanel object 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when the user closes window, application will exit
        setLayout(new BorderLayout()); // sets the layout manager
    }

    // add a main method to create an instance of game and make it visible
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TextUI startGUI = new TextUI("HxJ Games");
                startGUI.promptForUserProfile();
                startGUI.setVisible(true);
            }
        });
    }


    // add a method to display a message
    private JPanel startUpMessage() {
        JPanel temp = new JPanel();
        temp.add(new JLabel("Welcome to Kalah and Ayo!"));
        return temp;
    }


    // panel for the startup message to the game container
    public void setupGameContainer(boolean showStartupMessage) {
        gameContainer.setLayout(new BorderLayout());
        if (showStartupMessage) {
            gameContainer.add(startUpMessage(), BorderLayout.NORTH);
        }
        gameContainer.add(createBoardPanel(), BorderLayout.CENTER);
    }

    // method uses to create and configure a JPanel to hold buttons
    private JPanel makeButtonPanel() {
        JPanel buttonPanel = new JPanel(); // holds our buttons 
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // set layout of button panel
        buttonPanel.add(playKalahButton);
        buttonPanel.add(playAyoButton); 
        return buttonPanel;
    }

    // makes the kalah button 
    private JButton makeKalahButton() {
        JButton button = new JButton("Play Kalah");
        button.addActionListener(e -> {
            showGameRules("Kalah", kalahRules);
            int option = JOptionPane.showConfirmDialog(null, "Do you want to start a new Kalah game?", "Start Game", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                startGame();
            }
        });
            return button;
    }

    // makes the ayo button
    private JButton makeAyoButton() {
        JButton button = new JButton("Play Ayo");
        button.addActionListener(e -> {
            showGameRules("Ayo", ayoRules);
            int option = JOptionPane.showConfirmDialog(null, "Do you want to start a new Ayoayo game?", "Start Game", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                startAyoGame();
            }
        });
        
        return button;
    }

    // displays the game rules
    private void showGameRules(String gameName, String gameRules) {
        JOptionPane.showMessageDialog(null, gameRules, gameName + " Rules", JOptionPane.INFORMATION_MESSAGE);
    }


    // method responsible for creating/configuring the grid 
    private JPanel makeKalahGrid(int rows, int columns) {
        JPanel panel = new JPanel(); 
        buttons = new PositionAwareButton[rows][columns];
        panel.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int pitNumber = calculatePitNum(i, j);
                buttons[i][j] = new PositionAwareButton("P(" + pitNumber + ")", j + 1, i + 1);

                buttons[i][j].addActionListener(e -> {
                    // check if game is not initialized or not started
                    if (game == null || !game.isGameStarted()) {
                        JOptionPane.showMessageDialog(this, "Please start a game before making a move.", "Game Not Started", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // check if pit is empty
                    if (game.getNumStones(pitNumber) == 0) {
                        JOptionPane.showMessageDialog(this, "Invalid move: You cannot select an empty pit.", "Empty Pit", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (checkPlayerPit(pitNumber)) {
                        handlePitButtonClick(pitNumber);
                    }
                });

                panel.add(buttons[i][j]);
            }
        }
        return panel;
    }


    // creates the board panel 
    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new BorderLayout());

        // create and add the store for Player 2
        storeP2 = new JLabel("Player 2 Store", SwingConstants.CENTER); 
        boardPanel.add(storeP2, BorderLayout.WEST);

        // add the grid of pits
        boardPanel.add(makeKalahGrid(2, 6), BorderLayout.CENTER);

        // create and add the store for Player 1
        storeP1 = new JLabel("Player 1 Store", SwingConstants.CENTER); 
        boardPanel.add(storeP1, BorderLayout.EAST);

        return boardPanel;
    }


    // private method that moves the stones 
    private void handlePitButtonClick(int pitNumber) {
        try {
            game.move(pitNumber); // perform the move
            updateView();         // update the board and store counts

            // check for bonus turn
            if (game.getBoard().isFreeTurn()) {
                String currentPlayerName = game.getCurrentPlayer().getName();
                JOptionPane.showMessageDialog(this, "Bonus turn for " + currentPlayerName + "!", "Bonus Turn", JOptionPane.INFORMATION_MESSAGE);
            } else {
                checkGameOver();      // check if the game is over
            }

            // if the last move was a steal, display the message
            if (((KalahRules)game.getBoard()).isLastMoveSteal()) {
                JOptionPane.showMessageDialog(null, "Nice steal!", "Steal", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (InvalidMoveException e) {
            JOptionPane.showMessageDialog(this, "Invalid move: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void makeMenu() {
        menuBar = new JMenuBar();

        // creating the Game menu
        JMenu gameMenu = new JMenu("File");

        JMenuItem quitGame = new JMenuItem("Quit Game");
        quitGame.addActionListener(e -> quitGame());
        gameMenu.add(quitGame);

        
        JMenuItem saveGame = new JMenuItem("Save Game");
        saveGame.addActionListener(e -> saveGame());
        gameMenu.add(saveGame);
        
        JMenuItem loadGame = new JMenuItem("Load Game");
        loadGame.addActionListener(e -> loadGame());
        gameMenu.add(loadGame);

        JMenuItem exitApp = new JMenuItem("Exit Application");
        exitApp.addActionListener(e -> {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit the application?", "Exit Application", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            this.dispose(); // This will close the window and terminate the application if this is the only window
        }
    });

      

        gameMenu.add(quitGame);
        gameMenu.addSeparator(); // Adds a separator line
        gameMenu.add(saveGame);
        gameMenu.add(loadGame);
        gameMenu.addSeparator();
        gameMenu.add(exitApp);

       
        menuBar.add(gameMenu);

    }

    // starts the kalah varation of the game 
    protected void startGame() {
        playerOneProfile = getUserProfile("Enter name for Player 1:");
        playerTwoProfile = getUserProfile("Enter name for Player 2:");

        if (playerOneProfile != null && playerTwoProfile != null) {
            Player playerOne = new Player(playerOneProfile.getPlayerName());
            Player playerTwo = new Player(playerTwoProfile.getPlayerName());
            game = new MancalaGame(MancalaGame.GameType.KALAH);
            game.setPlayers(playerOne, playerTwo);
            game.startNewGame();
            playKalahButton.setVisible(false);
            playAyoButton.setVisible(false);
            updateView();
        } else {
            JOptionPane.showMessageDialog(this, "Game start canceled. Player names are required.", "Game Start Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // method that starts an ayo game 
    protected void startAyoGame(){

        playerOneProfile = getUserProfile("Enter name for Player 1:");
        playerTwoProfile = getUserProfile("Enter name for Player 2:");

        if (playerOneProfile != null && playerTwoProfile != null) {
            Player playerOne = new Player(playerOneProfile.getPlayerName());
            Player playerTwo = new Player(playerTwoProfile.getPlayerName());
            game = new MancalaGame(MancalaGame.GameType.AYO);
            game.setPlayers(playerOne, playerTwo);
            game.startNewGame();
            playKalahButton.setVisible(false);
            playAyoButton.setVisible(false);
            updateView();
        } else {
            JOptionPane.showMessageDialog(this, "Game start canceled. Player names are required.", "Game Start Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // updates the text after a button is selected
    protected void updateView() {
        // updates pit buttons
        int rows = 2; // fixed size 
        int columns = 6; // fixed size 
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int pitNum = calculatePitNum(i, j); 
                buttons[i][j].setText(String.valueOf(game.getNumStones(pitNum)));
            }
        }

        // update store labels
        storeP1.setText("Player 1 Store: " + game.getGameBoardDataStructure().getStoreCount(1));
        storeP2.setText("Player 2 Store: " + game.getGameBoardDataStructure().getStoreCount(2));

        // update the current player label
        String currentPlayerName = game.getCurrentPlayer().getName();
        currentPlayer.setText("Current Player: " + game.getCurrentPlayer().getName());
    }

    // gets the user profile 
    private UserProfile getUserProfile(String message) {
        String playerName = JOptionPane.showInputDialog(this, message);
        if (playerName != null && !playerName.isBlank()) {
            return new UserProfile(playerName); // Here you might want to load an existing profile if it exists
        } else {
            // returning null to indicate the process was canceled or invalid input was given
            return null;
        }
    }

    // private method that calculates pit number 
    private int calculatePitNum(int row, int column) {
        if (row == 0) { // top row (player 2's pits)
            return 12 - column;
        } else if (row == 1) { // bottom row (player 1's pits)
            return 1 + column;
        }
        return -1; 
    }

    // method that checks if a game is over
    private void checkGameOver() {
        if (game.isGameOver()) {
            try {
                Player winner = game.getWinner();
                String message;

                if (winner != null) {
                    message = winner.getName() + " wins!";
                    // increment wins based on the game type and the winner
                    if (winner.getName().equals(playerOneProfile.getPlayerName())) {
                        if (game.getGameType() == MancalaGame.GameType.KALAH) {
                            playerOneProfile.incrementKalahGamesWon();
                        } else if (game.getGameType() == MancalaGame.GameType.AYO) {
                            playerOneProfile.incrementAyoGamesWon();
                        }
                    } else {
                        // player two is the winner
                        if (game.getGameType() == MancalaGame.GameType.KALAH) {
                            playerTwoProfile.incrementKalahGamesWon();
                        } else if (game.getGameType() == MancalaGame.GameType.AYO) {
                            playerTwoProfile.incrementAyoGamesWon();
                        } 
                    }

                    // save the updated profiles
                    try {
                        Saver.saveUserProfile(playerOneProfile, playerOneProfile.getPlayerName() + ".profile");
                        Saver.saveUserProfile(playerTwoProfile, playerTwoProfile.getPlayerName() + ".profile");
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, "Error saving user profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    message = "It's a tie!";
                }

                // display the game over message
                JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);

                // ask if players want to play the same game again
                int playAgainOption = JOptionPane.showConfirmDialog(this, "Do you want to play the same game again?", "Play Again", JOptionPane.YES_NO_OPTION);
                if (playAgainOption == JOptionPane.YES_OPTION) {
                    resetGame(playerOneProfile, playerTwoProfile, game.getGameType()); // resets the game with the same type
                    playKalahButton.setVisible(false);
                    playAyoButton.setVisible(false);
                } else {
                    // ask if they want to switch to the other game type
                    int switchGameOption = JOptionPane.showConfirmDialog(this, "Do you want to switch to the other game type?", "Switch Game", JOptionPane.YES_NO_OPTION);
                    if (switchGameOption == JOptionPane.YES_OPTION) {
                        MancalaGame.GameType newGameType = game.getGameType() == MancalaGame.GameType.KALAH ? MancalaGame.GameType.AYO : MancalaGame.GameType.KALAH;
                        resetGame(playerOneProfile, playerTwoProfile, newGameType); // resets the game with the new type
                        playKalahButton.setVisible(false);
                        playAyoButton.setVisible(false);
                    }
                }
            } catch (GameNotOverException e) {
                // handle the exception by displaying an error message
                JOptionPane.showMessageDialog(this, "Attempted to get winner but game is not over.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // private method that checks if the pit belongs to a particular player
    private boolean checkPlayerPit(int pitNumber) {
        Player currentPlayer = game.getCurrentPlayer();
        boolean isPlayerOnePit = (pitNumber >= 1 && pitNumber <= 6);

        if ((isPlayerOnePit && currentPlayer.equals(game.getPlayerOne())) || 
            (!isPlayerOnePit && currentPlayer.equals(game.getPlayerTwo()))) {
            return true; // Valid pit for the current player
        } else {
            // Display error message if the pit is not valid
            JOptionPane.showMessageDialog(this, "Invalid move: Please choose your own pit.", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // Invalid pit
        }
    }

   // resets the game and initializes it based on the provided game type
    protected void resetGame(UserProfile playerOneProfile, UserProfile playerTwoProfile, MancalaGame.GameType gameType) {
        
        game = new MancalaGame(gameType); // initializes the game based on the selected game type
    
        Player playerOne = new Player(playerOneProfile.getPlayerName());
        Player playerTwo = new Player(playerTwoProfile.getPlayerName());
    
        game.setPlayers(playerOne, playerTwo); 
    
        game.startNewGame(); // start the new game
    
        updateView(); // update the UI to reflect the new game state

        // reset UI components to their initial state
        playKalahButton.setVisible(true);
        playAyoButton.setVisible(true);

        resetBoardUI(); // reset the board UI

        updateView(); // update the view again
    }

    // method that resets all the buttons
    private void resetBoardUI() {
    int initialStones = 4; 

    for (int i = 0; i < buttons.length; i++) {
        for (int j = 0; j < buttons[i].length; j++) {
            buttons[i][j].setText("P(" + calculatePitNum(i, j) + ") [" + initialStones + "]");
            buttons[i][j].setEnabled(true); // Re-enable all buttons
        }
    }

    }

    // method that saves a game state
    private void saveGame() {
        // Check if the game has been started or not
        if (game == null || !game.isGameStarted()) {
            // Game has not been started, show a message to the user
            JOptionPane.showMessageDialog(this, "There is no game to save.", "Save Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Game is active, proceed with saving
            try {
                Saver.saveObject(game, "savedGame.ser"); 
                JOptionPane.showMessageDialog(this, "Game saved successfully.", "Save Game", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving game: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // method that loads a game state
    private void loadGame() {
        try {
            MancalaGame loadedGame = (MancalaGame) Saver.loadObject("savedGame.ser");
            if (loadedGame != null) {
                this.game = loadedGame; // update the current game state
                setupGameContainer(false);
                updateView(); // refresh the UI with the loaded game state
                JOptionPane.showMessageDialog(this, "Game loaded successfully!", "Load Game", JOptionPane.INFORMATION_MESSAGE);

                // Make sure to show the game board and related components
                for (PositionAwareButton[] buttonRow : buttons) {
                    for (PositionAwareButton button : buttonRow) {
                        button.setVisible(true); // Show pit buttons
                    }
                }

                storeP1.setVisible(true); // show Player 1's store label
                storeP2.setVisible(true); // show Player 2's store label
                currentPlayer.setVisible(true); //sShow the current player label

                // hide the main menu components since we're now in game view
                playKalahButton.setVisible(false);
                playAyoButton.setVisible(false);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassCastException e) {
            JOptionPane.showMessageDialog(this, "Error loading game: Invalid game data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

      // method that resets all the buttons
    private void resetBoardUIQuitGame() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setText("P(" + calculatePitNum(i, j) + ")");
                buttons[i][j].setEnabled(true);
            }
        }

        // clear the store counts and player names
        storeP1.setText("Player 1 Store: ");
        storeP2.setText("Player 2 Store: ");
        currentPlayer.setText("Current Player: "); 

    }


   // method if the user chooses to quit a game
   private void quitGame() {
     // check if the game has been started or not
    if (game == null || !game.isGameStarted()) {
        JOptionPane.showMessageDialog(this, "No active game to quit.", "No Game", JOptionPane.INFORMATION_MESSAGE);
    } else {
        // prompt to save the user profile
        int saveProfile = JOptionPane.showConfirmDialog(this, "Do you want to save your profile before quitting?", "Save Profile", JOptionPane.YES_NO_OPTION);
        if (saveProfile == JOptionPane.YES_OPTION) {
            try {
                Saver.saveUserProfile(playerOneProfile, playerOneProfile.getPlayerName() + ".profile");
                JOptionPane.showMessageDialog(this, "Profile saved successfully.", "Profile Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save profile.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit the current game?", "Quit Game", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {

            for (PositionAwareButton[] buttonRow : buttons) {
                for (PositionAwareButton button : buttonRow) {
                    button.setVisible(true); 
                }
            }

            storeP1.setVisible(true); 
            storeP2.setVisible(true); 
            currentPlayer.setVisible(true); 

            // Show the main menu components
            playKalahButton.setVisible(true);
            playAyoButton.setVisible(true);

            resetBoardUIQuitGame();

            // Refresh the UI to reflect the changes
            this.revalidate();
            this.repaint();
        }

     }
        
    }


    // method to load up user's profile
    public void promptForUserProfile() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to load an existing profile?", "Load Profile", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
        // prompt for the profile name and load it
            String filename = JOptionPane.showInputDialog("Enter your profile name:");
            try {
             UserProfile loadedProfile = Saver.loadUserProfile(filename + ".profile");
                // set the loaded profile to the current user
                this.playerOneProfile = loadedProfile;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to load profile.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        } else {
            // proceed with creating a new user profile
            String playerName = JOptionPane.showInputDialog("Enter new profile name:");
            this.playerOneProfile = new UserProfile(playerName);
        }
    }
}