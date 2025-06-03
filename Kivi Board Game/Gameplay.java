import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Gameplay {
    int[][] playerPositions;
    Board board;
    Player[] players;
    int totalPlayers;
    int rounds;
    Random random;
    int currentPlayerIndex = 0;
    boolean waitingForHumanMove = false;
    int currentRound = 1;
    int BOARDSIZE = 7;

    public Gameplay(Board gameboard) {
        playerPositions = new int[7][7]; // Initialize with zeros

        board = gameboard;
        random = new Random();

        // Collect non-null players from the board
        ArrayList<Player> playerList = new ArrayList<>();
        if (board.player1 != null) playerList.add(board.player1);
        if (board.player2 != null) playerList.add(board.player2);
        if (board.player3 != null) playerList.add(board.player3);
        if (board.player4 != null) playerList.add(board.player4);

        // Convert list to array
        players = playerList.toArray(new Player[0]);
        totalPlayers = players.length;
        rounds = 10;

        board.setRound(String.valueOf(currentRound)); // Initialize round display

        // Start first player's turn
        startNextTurn();
    }

    public void startNextTurn() {
        if (currentPlayerIndex >= totalPlayers * rounds) {
            announceWinner();
            return;
        }

        Player currentPlayer = players[currentPlayerIndex % totalPlayers];
        board.setTurn(currentPlayer.name);
        board.resetGameRound();
        if (!(board.invalidAlert.getText().equals(""))){
            board.invalidAlert.setText("");
        }

        if (currentPlayer.type.equals("Human")) {
            waitingForHumanMove = true;
        } else {
            makeComputerMove(currentPlayer);
        }
    }

    public void handleHumanMove(int row, int col) {
        if (!waitingForHumanMove) return; // Ignore clicks if not waiting for a human move


        Player player = players[currentPlayerIndex % totalPlayers];

        if ((row >= 0) && (col >= 0)) {
            if ((playerPositions[row][col] == 0) && board.isValidMove(board.getSquareNames()[row][col], board.getDiceButtons())) {
                playerPositions[row][col] = player.number;
                board.squares[row][col].setText(String.valueOf(player.number));
                board.squares[row][col].setFont(new Font("Arial", Font.BOLD, 24)); // Bold and bigger text
                board.squares[row][col].setHorizontalAlignment(SwingConstants.CENTER); // Centers the text
                board.squares[row][col].setVerticalAlignment(SwingConstants.CENTER);

                if (!(board.invalidAlert.getText().equals(""))){
                    board.invalidAlert.setText("");
                }
                waitingForHumanMove = false; // Allow game to proceed
                currentPlayerIndex++;
                


            // Check if a full round has been completed
                if (currentPlayerIndex % totalPlayers == 0) {
                    currentRound++;
                    board.setRound(String.valueOf(currentRound));
                }

                startNextTurn(); // Move to next player
            }
            else {
                board.invalidAlert.setText("Invalid move");
            }
        }
        else {
            
            currentPlayerIndex++;
            // Check if a full round has been completed
            if (currentPlayerIndex % totalPlayers == 0) {
                currentRound++;
                board.setRound(String.valueOf(currentRound));
            }
            board.invalidAlert.setText("");
            startNextTurn(); // Move to next player
        }
    }

    private void makeComputerMove(Player player) {

        SwingWorker<Void, Void> computerMoveWorker = new SwingWorker<>() {
            int row, col;

            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(1000); // Simulate thinking time
                do {
                    row = random.nextInt(7);
                    col = random.nextInt(7);
                } while (playerPositions[row][col] != 0);

                playerPositions[row][col] = player.number;
                return null;
            }

            @Override
            protected void done() {
                board.squares[row][col].setText(String.valueOf(player.number));
                board.squares[row][col].setFont(new Font("Arial", Font.BOLD, 24)); // Bold and bigger text
                board.squares[row][col].setHorizontalAlignment(SwingConstants.CENTER); // Centers the text
                board.squares[row][col].setVerticalAlignment(SwingConstants.CENTER);

                currentPlayerIndex++;

                // Check if a full round has been completed
                if (currentPlayerIndex % totalPlayers == 0) {
                    currentRound++;
                    board.setRound(String.valueOf(currentRound));
                }

                board.resetGameRound();
                startNextTurn(); // Move to next player
            }
        };

        computerMoveWorker.execute();
    }

    public void announceWinner() {
        String winner = determineWinner(); // Placeholder for actual winner determination logic
        int choice = JOptionPane.showOptionDialog(
                null,
                winner + " wins the game!\nWould you like to Quit or Restart?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Quit", "Restart"},
                "Quit"
        );

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0); // Quit the game
        } else if (choice == JOptionPane.NO_OPTION) {
            restartGame(); // Restart the game
        }
    }

    private void restartGame() {
        System.out.println("Restarting game");
        board.getFrame().dispose();

        // Restart the game
        SwingUtilities.invokeLater(Game::new);
    }

    private String determineWinner() {
        // Placeholder logic: return a random player as the winner
        if (players.length == 0) {
            return "No one";
        }
        int[] scores = new int[totalPlayers]; // Array to track scores for each player

        // Calculate scores by iterating through player positions
        for (int row = 0; row < BOARDSIZE; row++) {
            for (int col = 0; col < BOARDSIZE; col++) {
                int playerNumber = playerPositions[row][col];
                if (playerNumber > 0) {
                    scores[playerNumber - 1] += board.scoreBoard[row][col]; // Add position indices to score
                }
            }
        }
        int maxScore = -1;
        int winningPlayerIndex = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                winningPlayerIndex = i;
            }
        }
            if (winningPlayerIndex == -1) {
            return "No one"; // If no valid moves are made
        }

        return players[winningPlayerIndex].name;
    }

    public int[][] getPlayerPositions(){
        return playerPositions;
    }
    public int getCurrentRound(){
        return currentRound;
    }
    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }
    public Player[] getPlayers(){
        return players;
    }

    public void setPlayerPositions(int[][] positions) {
        this.playerPositions = positions;
    }
    
    public void setCurrentRound(int round) {
        this.currentRound = round;
        board.setRound(String.valueOf(round)); // Update the round label on the board
    }
    
    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
        // Optionally update the turn label on the board
        board.setTurn(players[index % players.length].name);
    }
    
    
}
