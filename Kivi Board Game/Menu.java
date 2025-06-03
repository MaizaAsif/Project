import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.*;

public class Menu {
    private JFrame frame;
    private JButton newGameButton, loadGameButton, rulesButton;
    private JLabel titleLabel, welcomeLabel, selectPlayerLabel, difficultyLabel;
    private JComboBox<String> player1Dropdown;
    private JComboBox<String> player2Dropdown;
    private JComboBox<String> player3Dropdown;
    private JComboBox<String> player4Dropdown;
    private JComboBox<String> difficultyDropdown;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    String difficultyMode;

    public Menu() {

        try
        {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        }
        catch (Exception e)
        {   
            e.printStackTrace();

        }
        // Create the frame
        frame = new JFrame("Kivi Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        
        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create welcome label
        welcomeLabel = new JLabel("Welcome To Kivi Game", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(welcomeLabel);
        
        // Create select player label
        selectPlayerLabel = new JLabel("Select Player", SwingConstants.CENTER);
        selectPlayerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        selectPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(selectPlayerLabel);
        
        // Create drop-down lists
        String[] playerOptions = {"None", "Human", "Computer"};
        player1Dropdown = new JComboBox<>(playerOptions);
        player2Dropdown = new JComboBox<>(playerOptions);
        player3Dropdown = new JComboBox<>(playerOptions);
        player4Dropdown = new JComboBox<>(playerOptions);
        
        JPanel playerPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        playerPanel.add(new JLabel("Player 1", SwingConstants.CENTER));
        playerPanel.add(new JLabel("Player 2", SwingConstants.CENTER));
        playerPanel.add(new JLabel("Player 3", SwingConstants.CENTER));
        playerPanel.add(new JLabel("Player 4", SwingConstants.CENTER));
        
        JPanel dropdownPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        dropdownPanel.add(player1Dropdown);
        dropdownPanel.add(player2Dropdown);
        dropdownPanel.add(player3Dropdown);
        dropdownPanel.add(player4Dropdown);
        
        mainPanel.add(playerPanel);
        mainPanel.add(dropdownPanel);
        
        // Create difficulty label and dropdown
        difficultyLabel = new JLabel("Difficulty:", SwingConstants.CENTER);
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String[] difficultyOptions = {"None", "Easy", "Hard"};
        difficultyDropdown = new JComboBox<>(difficultyOptions);
        
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.add(difficultyLabel);
        difficultyPanel.add(difficultyDropdown);
        
        mainPanel.add(difficultyPanel);
        
        // Create buttons
        newGameButton = new JButton("New Game");
        loadGameButton = new JButton("Load Game");
        rulesButton = new JButton("Rules");
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(newGameButton);
        buttonPanel.add(loadGameButton);
        
        JPanel rulesPanel = new JPanel();
        rulesPanel.add(rulesButton);
        
        mainPanel.add(buttonPanel);
        mainPanel.add(rulesPanel);

        // Add actionListeners for buttons;
        newGameButton.addActionListener(_ -> {
            System.out.println("New Game");
            if (checkValidConditions()){
                createPlayers();
                new Board(player1, player2, player3, player4, difficultyMode);
                frame.dispose();

            }

        });
        loadGameButton.addActionListener(_ -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                try (FileInputStream fis = new FileInputStream(fileToLoad);
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
             
                    // Deserialize the saved GameState
                    GameState state = (GameState) ois.readObject();
             
                    // Create a new Board instance using the loaded players and difficulty.
                    // (Assuming the saved GameState has an array of players.)
                    Board board = new Board(
                        state.players.length > 0 ? state.players[0] : null,
                        state.players.length > 1 ? state.players[1] : null,
                        state.players.length > 2 ? state.players[2] : null,
                        state.players.length > 3 ? state.players[3] : null,
                        state.difficultyMode
                    );
             
                    // Restore the rest of the game state into the Board.
                    board.restoreGameState(state);
             
                    // Optionally dispose the Menu window after loading.
                    frame.dispose();
             
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error loading game: " + e.getMessage());
                }
            }
        });
        rulesButton.addActionListener(_ -> {
            System.out.println("Rules");
            showGameOverview();
        });

        frame.add(mainPanel, BorderLayout.CENTER);
        
        // Make the frame visible
        frame.setVisible(true);


    }

    private boolean checkValidConditions() {
        int playerCount = 0;
        boolean hasHumanPlayer = false;
        boolean hasComputerPlayer = false;
    
        JComboBox<String>[] playerDropdowns = new JComboBox[]{player1Dropdown, player2Dropdown, player3Dropdown, player4Dropdown};
    
        for (JComboBox<String> dropdown : playerDropdowns) {
            String selected = dropdown.getSelectedItem().toString();
            if (!selected.equals("None")) {
                playerCount++;
                if (selected.equals("Human")) hasHumanPlayer = true;
                if (selected.equals("Computer")) hasComputerPlayer = true;
            }
        }
    
        if (playerCount < 2) {
            JOptionPane.showMessageDialog(frame, "You must select at least 2 players!", "Invalid Selection", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        if (!hasHumanPlayer) {
            JOptionPane.showMessageDialog(frame, "At least one player must be Human!", "Invalid Selection", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        if (hasComputerPlayer && difficultyDropdown.getSelectedItem().equals("None")) {
            JOptionPane.showMessageDialog(frame, "Please select a difficulty level since a Computer player is participating.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        return true;
    }
    
    

    
    private void createPlayers(){
        player1 = null;
        player2 = null;
        player3 = null;
        player4 = null;

        // Get difficulty mode
        difficultyMode = difficultyDropdown.getSelectedItem().toString();
        // Create Player 1
        if (!player1Dropdown.getSelectedItem().equals("None")){
            String type = player1Dropdown.getSelectedItem().toString();
            player1 = new Player();
            player1.type = type;
            player1.number = 1;
            if (type.equals("Human")){
                player1.name = JOptionPane.showInputDialog("Enter Player 1 Name");
                
            }
            else{
                player1.name = "Player 1";
            }
        }
        // Create player 2
        if (!player2Dropdown.getSelectedItem().equals("None")){
            String type = player2Dropdown.getSelectedItem().toString();
            player2 = new Player();
            player2.type = type;
            player2.number = 2;
            if (type.equals("Human")){
                player2.name = JOptionPane.showInputDialog("Enter Player 2 Name");
                
            }
            else{
                player2.name = "Player 2";
            }
        }
        // Create player 3
        if (!player3Dropdown.getSelectedItem().equals("None")){
            String type = player3Dropdown.getSelectedItem().toString();
            player3 = new Player();
            player3.type = type;
            player3.number = 3;
            if (type.equals("Human")){
                player3.name = JOptionPane.showInputDialog("Enter Player 3 Name");
                
            }
            else{
                player3.name = "Player 3";
            }
        }
        // Create player 4
        if (!player4Dropdown.getSelectedItem().equals("None")){
            String type = player4Dropdown.getSelectedItem().toString();
            player4 = new Player();
            player4.type = type;
            player4.number = 4;
            if (type.equals("Human")){
                player4.name = JOptionPane.showInputDialog("Enter Player 4 Name");
                
            }
            else{
                player4.name = "Player 4";
            }
        }

    }

    
    private void showGameOverview() {
        String overview = "Kivi is a tactical dice game designed for 2 to 4 players, blending elements of strategy and chance.\n"
                + "The game is played on a 7×7 grid, totaling 49 squares, each marked with specific dice combinations.\n"
                + "Players aim to place their stones on the board by rolling six dice to achieve these combinations, "
                + "strategically building lines of consecutive stones horizontally or vertically to maximize their scores.\n\n"
                
                + "🎲 Gameplay Overview:\n\n"
                
                + "🔹 Rolling the Dice: On a player's turn, they roll six dice, aiming to match the combination on a desired square.\n"
                + "They may re-roll some or all dice up to two additional times to achieve a favorable outcome.\n\n"
                
                + "🔹 Placing Stones: After finalizing the dice roll, the player places one of their ten stones on a square corresponding "
                + "to the achieved combination. Squares have different point values:\n"
                + "- Pink (3 points)\n"
                + "- Black (2 points)\n"
                + "- White (1 point)\n\n"
                
                + "🔹 Scoring Combinations: The game features various combinations, including:\n"
                + "- Two pairs\n"
                + "- Three of a kind\n"
                + "- Little straight (four consecutive numbers)\n"
                + "- Full house\n"
                + "- Four of a kind\n"
                + "- Large straight (five consecutive numbers)\n"
                + "- All even or all odd numbers\n"
                + "- Sum of 12 or fewer\n"
                + "- Sum of 30 or more\n"
                + "- Three pairs\n"
                + "- Two sets of three of a kind\n"
                + "- Four of a kind plus a pair\n\n"
                
                + "Special combinations like five or six of a kind allow placement on any free square, with six of a kind permitting displacement of an opponent's stone.\n\n"
                
                + "🏆 Game Duration and Objective:\n"
                + "The game consists of 10 rounds, aligning with the ten stones each player possesses.\n"
                + "After all rounds, players calculate scores based on the lines they've formed; longer lines yield higher points.\n"
                + "The player with the most points at the end wins.\n\n"
                
                + "Kivi's straightforward rules combined with strategic depth make it engaging and replayable, as players must balance immediate gains with long-term positioning to outmaneuver opponents.";
    
        JOptionPane.showMessageDialog(frame, overview, "Gameplay Overview", JOptionPane.INFORMATION_MESSAGE);
    }

}
