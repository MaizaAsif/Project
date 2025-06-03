import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    String type; // "Human" or "Computer"
    String name;
    int[][] squaresOccupied;
    int number;

    public Player(){}

    public void humanMove(){
        System.out.println("Human make move");
    }

    public void computerMove(){
        System.out.println("Computer made move");
    }
}
