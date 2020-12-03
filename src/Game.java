/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Word Hunt");
        frame.setLocation(600, 600);

        // Top status panel to display score
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.NORTH);
        final JTextArea status = new JTextArea("Running...");
        status_panel.add(status);

        // Main playing area
        final LetterGrid grid = new LetterGrid(status);
        frame.add(grid, BorderLayout.CENTER);

        // Bottom panel containing load, reset, and solve buttons
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.SOUTH);

        final JButton howTo = new JButton("Instructions");
        howTo.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "This is a clone of the iMessage game Word Hunt. \n" +
                        " To play, use your mouse to drag through letters to make words. \n" +
                        "Try to score as many points as possible in 80 seconds. \n" +
                        "Remember, longer words will receive more points! \n" +
                        "If you get stuck, click reset for a new board, or solve to \n" +
                        "see all possible words. You can also save and load boards to \n" +
                        "replay at a later time."));
        control_panel.add(howTo);

        final JButton load = new JButton("Load Board");
        load.addActionListener(e -> grid.loadBoard());
        control_panel.add(load);

        final JButton save = new JButton("Save Board");
        save.addActionListener(e -> grid.saveBoard());
        control_panel.add(save);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> grid.reset());
        control_panel.add(reset);

        final JButton solve = new JButton("Solve");
        solve.addActionListener(e -> grid.solve());
        control_panel.add(solve);


        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        grid.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}