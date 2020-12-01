/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
    private LetterGrid letterGrid; // Holds the letter grid

    private boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int COURT_WIDTH = Constants.COURT_WIDTH;
    public static final int COURT_HEIGHT = Constants.COURT_HEIGHT;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                System.out.println("Mouse pressed");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                System.out.println("Mouse released");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
            }
        });

        this.status = status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        letterGrid = new LetterGrid();

        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
            // advance the square and snitch in their current direction.
            letterGrid.move();


            // check for the game end conditions
//            if (square.intersects(poison)) {
//                playing = false;
//                status.setText("You lose!");
//            } else if (square.intersects(snitch)) {
//                playing = false;
//                status.setText("You win!");
//            }

            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        letterGrid.draw(g);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}