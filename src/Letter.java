/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 *
 * @version 2.1, Apr 2017
 */

import javax.swing.*;
import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * circle of a specified color.
 */
public class Letter extends JComponent {

    private char letter;
    private boolean selected = false;

    /**
     * Constructor for a single letter
     * @param letter the letter displayed
     */
    public Letter(char letter) {
        this.letter = letter;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (selected) {
            g.setColor(new Color(232, 130, 118));
        } else {
            g.setColor(new Color(220, 188, 143));
        }
        g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 15, 15);
        g.setColor(Color.BLACK); // temp for now
        g.setFont(new Font("Courier New", Font.PLAIN, 24));
        g.drawString(String.valueOf(letter).toUpperCase(),
                this.getWidth() / 2, this.getHeight() / 2);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public char getLetter() {
        return letter;
    }

}