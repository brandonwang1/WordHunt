/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * circle of a specified color.
 */
public class Letter extends JComponent {

    public final char LETTER;

    /**
     * Constructor for a single letter
     * @param x Starting X position
     * @param y Starting Y position
     * @param letter the letter displayed
     */
    public Letter(int x, int y, char letter) {
        super(0, 0, x, y,
                Constants.LETTER_SIZE, Constants.LETTER_SIZE,
                Constants.COURT_WIDTH, Constants.COURT_HEIGHT);
        LETTER = letter;


        });
    }


    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.CYAN); // temp for now
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight()); // Round Rect?
        g.setColor(Color.BLACK); // temp for now
        g.drawString(String.valueOf(LETTER), getPx() + Constants.LETTER_SIZE/2, getPy() + Constants.LETTER_SIZE/2);
    }
}