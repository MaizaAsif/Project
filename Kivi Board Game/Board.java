import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Board {

    JButton[][] squares;
    int[][] scoreBoard;
    String[][] squareNames;
    JButton saveGame, rollDice, switchColor;
    JLabel turnLabel, roundLabel, rollsLabel, invalidAlert;
    JPanel boardPanel, topPanel, dicePanel, statusPanel;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    String difficulty;
    Dice[] diceButtons;
    JFrame frame;
    Gameplay gameplay;
    List<Point> lightSquares;
    List<Point> mediumSquares;
    List<Point> darkSquares;

    List<Point> twoPairs;
    List<Point> threeOfAKind;
    List<Point> fourOfAKind;
    List<Point> threePairs;
    List<Point> twoTimesThreeOfAKind;
    List<Point> littleStraight;
    List<Point> largeStraight;
    List<Point> allEven;
    List<Point> allOdd;
    List<Point> twelveOrFew;
    List<Point> thirtyOrMore;
    List<Point> fourOfAKindAndAPair;
    List<Point> fullHouse;
    private int rollCount = 0; // Tracks how many times dice have been rolled



    public Board(Player p1, Player p2, Player p3, Player p4, String difficultyMode){
        scoreBoard = new int[7][7];
        squareNames = new String[7][7];
        // Initialize squares
        setSquares();

        // Initialize Players
        player1 = p1;
        player2 = p2;
        player3 = p3;
        player4 = p4;
        difficulty = difficultyMode;
        // Initialize light, medium and dark sqaures
        // Main frame
        frame = new JFrame("Kivi Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(14000, 900);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center frame

        // Top panel with buttons
        topPanel = new JPanel(new BorderLayout());
        switchColor = new JButton("Choose Display Options");
        saveGame = new JButton("Save Game");
        topPanel.add(switchColor, BorderLayout.WEST);
        topPanel.add(new JLabel("Kivi Game", SwingConstants.CENTER), BorderLayout.CENTER);
        topPanel.add(saveGame, BorderLayout.EAST);

        // 7x7 Board Panel (wrapped inside a container to prevent resizing issues)
        boardPanel = new JPanel(new GridLayout(7, 7));
        squares = new JButton[7][7];
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                squares[row][col] = new JButton();
                squares[row][col].setPreferredSize(new Dimension(60, 60));
                // Add border around each square
                squares[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); 
                // Ensures opaque rendering
                squares[row][col].setOpaque(true);

                // Add action listener for human move
                int finalRow = row;
                int finalCol = col;
                squares[row][col].addActionListener(e -> {
                    if (gameplay != null) {
                        gameplay.handleHumanMove(finalRow, finalCol);
                    }
                });

                boardPanel.add(squares[row][col]);
            }
        }

        // Wrapping board panel in a container to control size
        JPanel boardContainer = new JPanel(new BorderLayout());
        boardContainer.add(boardPanel, BorderLayout.CENTER);
        boardContainer.setPreferredSize(new Dimension(700, 400)); // Limit board height
        // Set board to default view
        setBoardColor(Color.WHITE, Color.LIGHT_GRAY,Color.DARK_GRAY);
        setBoardSquareTypes();
        setBoardSymbols();
        // Bottom panel containing status and dice
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setPreferredSize(new Dimension(700, 150)); // Ensure visibility

        // Status panel (Turn, Rolls Left, Roll Dice)
        statusPanel = new JPanel(new GridLayout(1, 4, 20, 0)); // 3 columns with spacing
        turnLabel = new JLabel("Turn: " + (player1 != null ? player1.name : "Player 1"), SwingConstants.CENTER);
        roundLabel = new JLabel("Round: 0", SwingConstants.CENTER);
        invalidAlert = new JLabel("", SwingConstants.CENTER);
        rollsLabel = new JLabel("Rolls Left: 3", SwingConstants.CENTER);
        rollDice = new JButton("Roll Dice!");

        // Add padding to ensure visibility and proper spacing
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        statusPanel.setPreferredSize(new Dimension(700, 50));

        statusPanel.add(turnLabel);
        statusPanel.add(roundLabel);
        statusPanel.add(invalidAlert);
        statusPanel.add(rollsLabel);
        statusPanel.add(rollDice);


        // Dice panel (6 buttons)
        dicePanel = new JPanel(new GridLayout(2, 3, 5, 5));
        dicePanel.setPreferredSize(new Dimension(700, 100)); // Ensure dice panel has space

        diceButtons = new Dice[6];
        for (int i = 0; i < 6; i++) {
            final int index = i;
            diceButtons[i] = new Dice(i);
            diceButtons[i].setEnabled(false); 
            diceButtons[i].addActionListener(_ -> diceButtons[index].toggleLock());  
            dicePanel.add(diceButtons[i]);
        }
        
        // actionlisteners
        rollDice.addActionListener(new RollDiceListener());


        switchColor.addActionListener(_ -> chooseDisplayOptions());
        saveGame.addActionListener(_ -> saveGameOption());


        // Add panels to bottomPanel
        bottomPanel.add(statusPanel);
        bottomPanel.add(dicePanel);

        // Adding components to the frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(boardContainer, BorderLayout.CENTER); // Ensures the board has controlled height
        frame.add(bottomPanel, BorderLayout.SOUTH); // Ensures bottom panel stays visible

        frame.setVisible(true);
        
        gameplay = new Gameplay(this);
    }

    public int getRollCount(){
        return rollCount;
    }

    private class RollDiceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (rollCount <= 3) { // Only allow up to 3 rolls
                for (Dice dice : diceButtons) {
                    if (!dice.isLocked()) { 
                        dice.roll();
                    }
                }
                rollCount++;
                rollsLabel.setText("Rolls Left: " + (3 - rollCount));

                
                if (rollCount == 1) { 
                    for (Dice dice : diceButtons) {
                        dice.setEnabled(true); 
                        dice.unlock();
                    }
                }
                
    
                if (rollCount == 3) { // After 3rd roll, disable rolling
                    rollDice.setEnabled(false);
                    if (checkNoValidMoves(diceButtons)){
                        JOptionPane.showMessageDialog(frame, "No valid moves available");
                        gameplay.handleHumanMove(-1,-1);
                    }
                }
            }
        }
    }

    public void resetGameRound() {
        resetRound(); // Calls the private reset method
    }

    private void resetRound() {
        rollCount = 0; 
        rollDice.setEnabled(true); 
        rollsLabel.setText("Rolls Left: 3");

        for (Dice dice : diceButtons) {
            dice.setEnabled(false); 
            dice.unlock(); 
            dice.clearValue(); 

        }
    }
    
    public void chooseDisplayOptions() {
        String[] options = {"Gray and White", "Pink and White"};
        int choice = JOptionPane.showOptionDialog(
            frame,
            "Choose a color scheme:",
            "Switch Board Colors",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
    
        if (choice == 0) { // "Gray and White" selected
            setBoardColor(Color.WHITE, Color.LIGHT_GRAY, Color.DARK_GRAY);
        } else if (choice == 1) { // "Pink and White" selected
            Color lightPink = new Color(255, 182, 193); // Light Pink color
            setBoardColor(Color.WHITE, lightPink, Color.MAGENTA);
        }
    }
    
    public void saveGameOption() {
        // Create and populate the GameState object
        GameState state = new GameState();
    
        // Get the board/dice state
        state.diceValues = new int[diceButtons.length];
        state.diceLocked = new boolean[diceButtons.length];
        for (int i = 0; i < diceButtons.length; i++){
            state.diceValues[i] = diceButtons[i].getValue();
            state.diceLocked[i] = diceButtons[i].isLocked();
        }
    
        // Get the game state from Gameplay
        state.playerPositions = gameplay.getPlayerPositions();
        state.currentRound = gameplay.getCurrentRound();
        state.currentPlayerIndex = gameplay.getCurrentPlayerIndex();
        state.players = gameplay.getPlayers();
    
        // Also save the difficulty mode (or any other settings) from Board
        state.difficultyMode = this.difficulty;
    
        // Let the user choose where to save via a file chooser
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileOutputStream fos = new FileOutputStream(fileToSave);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
             
                oos.writeObject(state);
                JOptionPane.showMessageDialog(frame, "Game saved successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error saving game: " + e.getMessage());
            }
        }
    }

    public void loadGameOption() {
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showOpenDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(fileToLoad);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
             
                GameState state = (GameState) ois.readObject();
                // Now restore the game state using a helper method
                restoreGameState(state);
             
                JOptionPane.showMessageDialog(frame, "Game loaded successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error loading game: " + e.getMessage());
            }
        }
    }

    public void restoreGameState(GameState state) {
        // 1. Restore dice values and locked states.
        for (int i = 0; i < diceButtons.length; i++){
            diceButtons[i].setValue(state.diceValues[i]);
            diceButtons[i].setLocked(state.diceLocked[i]);
        }
        
        // 2. Reinitialize the board UI so that all empty squares get their default labels.
        // This resets colors, square types, and combination labels.
        setBoardColor(Color.WHITE, Color.LIGHT_GRAY, Color.DARK_GRAY);
        setBoardSquareTypes();
        setBoardSymbols();
        
        // 3. Restore game logic stored in the Gameplay object.
        gameplay.setPlayerPositions(state.playerPositions);
        gameplay.setCurrentRound(state.currentRound);
        gameplay.setCurrentPlayerIndex(state.currentPlayerIndex);
        
        // 4. Update the board with the moves that have been made.
        // For each square, if it was occupied, overwrite the default label with the player's token.
        for (int row = 0; row < 7; row++){
            for (int col = 0; col < 7; col++){
                int occupant = state.playerPositions[row][col];
                if (occupant > 0) {
                    squares[row][col].setText(String.valueOf(occupant));
                    squares[row][col].setFont(new Font("Arial", Font.BOLD, 24));
                    squares[row][col].setHorizontalAlignment(SwingConstants.CENTER);
                    squares[row][col].setVerticalAlignment(SwingConstants.CENTER);
                }
                // For empty squares, we keep the combination labels set by setBoardSymbols().
            }
        }
        
        // 5. Restore additional settings.
        this.difficulty = state.difficultyMode;
        setRound(String.valueOf(gameplay.getCurrentRound()));
        
        // Optionally, update the turn label based on the current player's turn.
        Player currentPlayer = gameplay.getPlayers()[gameplay.getCurrentPlayerIndex() % gameplay.getPlayers().length];
        setTurn(currentPlayer.name);
    }
    
    

    public void setTurn(String name){
        turnLabel.setText("Turn: " + name);
    }
    
    public void setRound(String roundNumber){
        roundLabel.setText("Round: " + roundNumber);
    }
    
    private void setBoardColor(Color light, Color medium, Color dark) {
        if (lightSquares == null || mediumSquares == null || darkSquares == null) {
            System.out.println("Error: Square lists are not initialized.");
            return;
        }
    
        // Set light color for corresponding squares
        for (Point position : lightSquares) {
            JButton button = squares[position.x][position.y];
            button.setBackground(light);
            button.setForeground(Color.BLACK); // Ensures contrast
            button.setOpaque(true);
        }
    
        // Set medium color for corresponding squares
        for (Point position : mediumSquares) {
            JButton button = squares[position.x][position.y];
            button.setBackground(medium);
            button.setForeground(Color.BLACK); // Ensures contrast
            button.setOpaque(true);

        }
    
        // Set dark color for corresponding squares
        for (Point position : darkSquares) {
            JButton button = squares[position.x][position.y];
            button.setBackground(dark);
            button.setForeground(Color.WHITE); // Ensures contrast
            button.setOpaque(true);

        }
            // Force UI to refresh
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public Dice[] getDiceButtons(){
        return diceButtons;
    }

    public String[][] getSquareNames(){
        return squareNames;
    }
    
    private void setSquares(){
        lightSquares = new ArrayList<>();
        mediumSquares = new ArrayList<>();
        darkSquares = new ArrayList<>();
    
        // Light Squares
        lightSquares.add(new Point(0,0));
        lightSquares.add(new Point(0,3));
        lightSquares.add(new Point(0,6));
        lightSquares.add(new Point(1,2));
        lightSquares.add(new Point(1,4));
        lightSquares.add(new Point(2,0));
        lightSquares.add(new Point(2,6));
        lightSquares.add(new Point(3,1));
        lightSquares.add(new Point(3,5));
        lightSquares.add(new Point(4,0));
        lightSquares.add(new Point(4,6));
        lightSquares.add(new Point(5,2));
        lightSquares.add(new Point(5,4));
        lightSquares.add(new Point(6,0));
        lightSquares.add(new Point(6,3));
        lightSquares.add(new Point(6,6));
    
        // Medium Squares 
        mediumSquares.add(new Point(0,1));
        mediumSquares.add(new Point(0,4));
        mediumSquares.add(new Point(0,5));
        mediumSquares.add(new Point(1,0));
        mediumSquares.add(new Point(1,6));
        mediumSquares.add(new Point(2,1));
        mediumSquares.add(new Point(2,2));
        mediumSquares.add(new Point(2,3));
        mediumSquares.add(new Point(2,5));
        mediumSquares.add(new Point(3,0));
        mediumSquares.add(new Point(3,2));
        mediumSquares.add(new Point(3,4));
        mediumSquares.add(new Point(3,6));
        mediumSquares.add(new Point(4,1));
        mediumSquares.add(new Point(4,3));
        mediumSquares.add(new Point(4,4));
        mediumSquares.add(new Point(4,5));
        mediumSquares.add(new Point(5,1));
        mediumSquares.add(new Point(5,6));
        mediumSquares.add(new Point(6,1));
        mediumSquares.add(new Point(6,2));
        mediumSquares.add(new Point(6,4));
        mediumSquares.add(new Point(6,5));
        mediumSquares.add(new Point(5,0));
        mediumSquares.add(new Point(0,2)); 
    
        // Dark Squares
        darkSquares.add(new Point(1,1));
        darkSquares.add(new Point(1,3));
        darkSquares.add(new Point(1,5));
        darkSquares.add(new Point(2,4));
        darkSquares.add(new Point(3,3));
        darkSquares.add(new Point(4,2));
        darkSquares.add(new Point(5,3));
        darkSquares.add(new Point(5,5));
    }

    public JFrame getFrame(){
        return frame;
    }

    private void setBoardSquareTypes(){
        twoPairs = new ArrayList<>(List.of(
            new Point(0,0), new Point(3,5), new Point(4,6), new Point(6,3)
        ));
        
        threeOfAKind = new ArrayList<>(List.of(
            new Point(0,3), new Point(0,6), new Point(1,2), new Point(3,5), new Point(4,0)
        ));
        
        littleStraight = new ArrayList<>(List.of(
            new Point(1,4), new Point(2,0), new Point(5,2), new Point(6,0)
        ));
        
        fullHouse = new ArrayList<>(List.of(
            new Point(2,6), new Point(3,1), new Point(5,4), new Point(6,6)
        ));
        
        fourOfAKind = new ArrayList<>(List.of(
            new Point(2,1), new Point(4,4), new Point(6,2), new Point(6,5)
        ));
        
        largeStraight = new ArrayList<>(List.of(
            new Point(0,1), new Point(2,3), new Point(3,6), new Point(4,1)
        ));
        
        allEven = new ArrayList<>(List.of(
            new Point(0,5), new Point(1,0), new Point(3,2)
        ));
        
        allOdd = new ArrayList<>(List.of(
            new Point(0,4), new Point(2,5), new Point(4,3), new Point(5,0), new Point(6,4)
        ));
        
        twelveOrFew = new ArrayList<>(List.of(
            new Point(0,2), new Point(3,0), new Point(3,4), new Point(5,6)
        ));
        
        thirtyOrMore = new ArrayList<>(List.of(
            new Point(1,6), new Point(2,2), new Point(4,5), new Point(6,1)
        ));
        
        threePairs = new ArrayList<>(List.of(
            new Point(1,3), new Point(4,2), new Point(5,5)
        ));
        
        twoTimesThreeOfAKind = new ArrayList<>(List.of(
            new Point(1,5), new Point(3,3), new Point(5,1)
        ));
        
        fourOfAKindAndAPair = new ArrayList<>(List.of(
            new Point(1,1), new Point(2,4), new Point(5,3)
        ));
    }
    
    private void setBoardSymbols(){
        // Set text on corresponding squares
        for (Point p : twoPairs) {
            squares[p.x][p.y].setText("Two Pairs");
            squareNames[p.x][p.y] = "Two Pairs";
            scoreBoard[p.x][p.y] = 1;
        }
        for (Point p : threeOfAKind){ 
            squares[p.x][p.y].setText("Three of a Kind");
            squareNames[p.x][p.y] = "Three of a Kind";
            scoreBoard[p.x][p.y] = 1;
        }
        for (Point p : littleStraight) {
            squares[p.x][p.y].setText("Little Straight");
            squareNames[p.x][p.y] = "Little Straight";
            scoreBoard[p.x][p.y] = 1;
        }
        for (Point p : fullHouse){ 
            squares[p.x][p.y].setText("Full House");
            squareNames[p.x][p.y] = "Full House";
            scoreBoard[p.x][p.y] = 1;
        }
        for (Point p : fourOfAKind) {
            squares[p.x][p.y].setText("Four of a Kind");
            squareNames[p.x][p.y] = "Four of a Kind";
            scoreBoard[p.x][p.y] = 2;
        }
        for (Point p : largeStraight) {
            squares[p.x][p.y].setText("Large Straight");
            squareNames[p.x][p.y] = "Large Straight";
            scoreBoard[p.x][p.y] = 2;
        }
        for (Point p : allEven){
            squares[p.x][p.y].setText("All Even");
            squareNames[p.x][p.y] = "All Even";
            scoreBoard[p.x][p.y] = 2;
        }
        for (Point p : allOdd){ 
            squares[p.x][p.y].setText("All Odd");
            squareNames[p.x][p.y] =  "All Odd";
            scoreBoard[p.x][p.y] = 2;
        }
        for (Point p : twelveOrFew) {
            squares[p.x][p.y].setText("Twelve or Fewer");
            squareNames[p.x][p.y] = "Twelve or Fewer";
            scoreBoard[p.x][p.y] = 2;
        }
        for (Point p : thirtyOrMore) {
            squares[p.x][p.y].setText("Thirty or More");
            squareNames[p.x][p.y] = "Thirty or More";
            scoreBoard[p.x][p.y] = 2;
        }
        for (Point p : threePairs){ 
            squares[p.x][p.y].setText("Three Pairs");
            squareNames[p.x][p.y] = "Three Pairs";
            scoreBoard[p.x][p.y] = 2;
        }
        for (Point p : twoTimesThreeOfAKind) {
            squares[p.x][p.y].setText("Two Times Three of a Kind");
            squareNames[p.x][p.y] = "Two Times Three of a Kind";
            scoreBoard[p.x][p.y] = 3;
        }
        for (Point p : fourOfAKindAndAPair) {
            squares[p.x][p.y].setText("Four of a Kind and a Pair");
            squareNames[p.x][p.y] = "Four of a Kind and a Pair";
            scoreBoard[p.x][p.y] = 3;
        }
    }

    public boolean isValidMove(String squareType, Dice[] dice){
        // Get the integer values of each dice
        int diceValues[] = new int[6];
        int i = 0;
        for (Dice d : dice){
            diceValues[i] = d.getValue();
            i++;
        }
        // Build a frequency map for the dice values.
        Map<Integer, Integer> freq = new HashMap<>();
        for (int d : diceValues) {
            freq.put(d, freq.getOrDefault(d, 0) + 1);
        }
        
        // Create a set of dice values to help check for straights.
        Set<Integer> diceSet = new HashSet<>();
        for (int d : diceValues) {
            diceSet.add(d);
        }
        
        // Compute the total sum of the dice.
        int total = 0;
        for (int d : diceValues) {
            total += d;
        }
        
        // Use a switch on the lower-case version of the square type.
        switch (squareType.toLowerCase()) {
            case "two pairs":
                // Expect exactly 4 distinct numbers with exactly two of them appearing twice and two singles.
                int pairCount = 0;
                for (int count : freq.values()) {
                    if (count >= 2) {
                        pairCount++;
                    }
                }
                return pairCount >= 2;
                
            case "large straight":
                // Valid if the dice contain 5 consecutive numbers.
                // For standard dice, valid sequences are either {1,2,3,4,5} or {2,3,4,5,6}.
                Set<Integer> seq1 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
                Set<Integer> seq2 = new HashSet<>(Arrays.asList(2, 3, 4, 5, 6));
                return diceSet.containsAll(seq1) || diceSet.containsAll(seq2);
                
            case "little straight":
                // Valid if the dice contain any sequence of 4 consecutive numbers.
                // Possible sequences: {1,2,3,4}, {2,3,4,5}, or {3,4,5,6}.
                Set<Integer> seqA = new HashSet<>(Arrays.asList(1, 2, 3, 4));
                Set<Integer> seqB = new HashSet<>(Arrays.asList(2, 3, 4, 5));
                Set<Integer> seqC = new HashSet<>(Arrays.asList(3, 4, 5, 6));
                return diceSet.containsAll(seqA) || diceSet.containsAll(seqB) || diceSet.containsAll(seqC);
                
            case "twelve or fewer":
                return total < 12;
                
            case "three of a kind":
                // Valid if one number appears exactly three times and the remaining dice are all distinct
                // (i.e. frequencies: 3,1,1,1).
                if (freq.containsValue(3) || freq.containsValue(4) || freq.containsValue(5) || freq.containsValue(6)) {
                    return true;
                }
                return false;
            
            case "four of a kind":
                // Valid if one number appears at least four times
                if (freq.containsValue(4) || freq.containsValue(5) || freq.containsValue(6)) {
                    return true;
                }
                return false;
                
            case "all odd":
                // Check that every die is odd.
                for (int d : diceValues) {
                    if (d % 2 == 0) {
                        return false;
                    }
                }
                return true;
                
            case "all even":
                // Check that every die is even.
                for (int d : diceValues) {
                    if (d % 2 != 0) {
                        return false;
                    }
                }
                return true;
                
            case "four of a kind and a pair":
                // Valid if there are exactly 2 distinct numbers: one appears 4 times and the other 2 times.
                if (freq.size() == 2) {
                    boolean hasFour = false, hasTwo = false;
                    for (int count : freq.values()) {
                        if (count == 4) {
                            hasFour = true;
                        } else if (count == 2) {
                            hasTwo = true;
                        }
                    }
                    return hasFour && hasTwo;
                }
                return false;
                
            case "three pairs":
                // Valid if there are exactly 3 distinct pairs (i.e. three numbers each appearing exactly 2 times).
                if (freq.size() == 3) {
                    for (int count : freq.values()) {
                        if (count != 2) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
                
            case "two times three of a kind":
                // Valid if there are exactly 2 distinct numbers, each appearing exactly 3 times.
                if (freq.size() == 2) {
                    for (int count : freq.values()) {
                        if (count != 3) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
                
            case "thirty or more":
                return total >= 30;
                
            case "full house":
                // For 6 dice, we define a full house as one number appearing three times,
                // another number appearing two times, and a third number appearing once (3+2+1).
                boolean foundThree = false, foundTwo = false;
                for (int count : freq.values()) {
                    if (count == 3) {
                        foundThree = true;
                    } else if (count == 2) {
                        foundTwo = true;
                    }
                }
                return foundThree && foundTwo;
            
            default:
                return false;
        }
            
    }

    public boolean checkNoValidMoves(Dice[] dice){
        for (int row=0; row<7; row++){
            for (int col=0; col<7; col++){
                JButton currentSquare = squares[row][col];
                String currentSquareText = currentSquare.getText();
                if (isValidMove(currentSquareText, dice)){
                    invalidAlert.setText(currentSquareText + " " + row + "," + col);
                    return false;
                }
            }
        }
        invalidAlert.setText("No Valid Moves");
        return true;
    }

    // Board.java
    public int[] getDiceValues() {
        int[] values = new int[diceButtons.length];
        for (int i = 0; i < diceButtons.length; i++) {
            values[i] = diceButtons[i].getValue();
        }
        return values;
    }

    public boolean[] getDiceLockedStates() {
        boolean[] locked = new boolean[diceButtons.length];
        for (int i = 0; i < diceButtons.length; i++) {
            locked[i] = diceButtons[i].isLocked();
        }
        return locked;
    }

    
}
