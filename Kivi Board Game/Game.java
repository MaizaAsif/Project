import javax.swing.*;

public class Game {
    //Board boardPanel;


    public Game() {
        new Menu();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}
