import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

class Dice extends JButton {
    private int diceNumber;
    private int value;
    private Random random;
    private boolean locked; 

    public Dice(int dn) {
        this.diceNumber = dn;
        this.value = 0;
        this.random = new Random();
        this.locked = false; // Dice starts as unlocked
        setText("Dice Number: " + diceNumber);

        addActionListener(new DiceClickListener());

    }

    public void roll() {
        if (!locked) { 
            value = random.nextInt(6) + 1; 
            setText("Dice " + diceNumber + ": " + value); 
        } else {
            System.out.println("Dice " + diceNumber + " is locked and did not roll.");
        }
    }

    
    public void toggleLock() {
        if (value == 0) { 
            return;
        }
        if (locked) {
            return; 
        }
        locked = true; 
        updateAppearance();
    }

    public void clearValue() {
        value = 0;
        setText("Dice Number: " + diceNumber); 
    }
    

    private void updateAppearance() {
        if (locked) {
            setEnabled(false); 
        } else {
            setEnabled(true); 
        }
    }

    public boolean isLocked() {
        return locked;
    }


    public int getValue() {
        return value;
    }

    public void unlock() {
        locked = false; // Reset locked state
    }

    public void setValue(int newValue) {
        this.value = newValue;
        // Update the button text so that the UI reflects the new value.
        setText("Dice " + diceNumber + ": " + newValue);
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
        // Update the button enabled state: if locked, disable it.
        setEnabled(!locked);
    }
    

    public void allowLocking() {
        addActionListener(new DiceClickListener()); // Enable locking
    }

    private class DiceClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            toggleLock();
        }
    }

    
}