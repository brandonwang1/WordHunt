/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.util.Random;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * circle of a specified color.
 */
public class LetterGrid extends GameObj {
    public static final int INIT_POS_X = 170;
    public static final int INIT_POS_Y = 170;
    public static final int DIMENSIONS = 4; // To make a 4x4 grid
    public static final int SIZE = DIMENSIONS * Constants.LETTER_SIZE;


    public Letter[][] getLetterContainer() {
        return letterContainer;
    }

    public void setLetterContainer(Letter[][] letterContainer) {
        this.letterContainer = letterContainer;
    }

    private  Letter[][] letterContainer = new Letter[DIMENSIONS][DIMENSIONS];

    public LetterGrid() {
        super(0, 0, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, Constants.COURT_WIDTH, Constants.COURT_HEIGHT);
        Random r = new Random();
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = 0; j < DIMENSIONS; j++) {
                // Generate a random character and add it to the array
                char c = (char)(r.nextInt(26) + 'a');
                letterContainer[i][j] = new Letter(INIT_POS_X + i * Constants.LETTER_SIZE,
                        INIT_POS_Y + j * Constants.LETTER_SIZE, c);
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = 0; j < DIMENSIONS; j++) {
                letterContainer[i][j].draw(g);
            }
        }
    }
}