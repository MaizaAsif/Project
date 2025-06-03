import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Game logic state (from Gameplay)
    public int[][] playerPositions;      // The 7x7 board positions indicating which player occupies a square.
    public int currentRound;
    public int currentPlayerIndex;
    
    // Dice state (from Board)
    public int[] diceValues;             // Length 6: one per dice.
    public boolean[] diceLocked;         // Length 6: locked state for each dice.
    
    // Player state – we can save the players directly if we make Player serializable.
    public Player[] players;
    
    // Other options you might want to save:
    public String difficultyMode;
}
