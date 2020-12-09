import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.Timer;

/**
 * LetterGrid class contains the main logic for the game
 *
 * @author Brandon Wang
 */
@SuppressWarnings("serial")
public class LetterGrid extends JPanel {

    // View/controller constants
    public static final int DIMENSIONS = 5;
    public static final int COURT_WIDTH = 600;
    public static final int COURT_HEIGHT = 600;
    private boolean playing = false; // whether the game is running
    private final JTextArea gameTime; // Current status text, i.e. "Running..."
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    private boolean mouseDown = false;      // Stores mouseState
    private long startTime; // When the game started
    private String lastWordStatus = ""; // String to display if the last word was valid or not.

    // Model constants
    // Stores the letters
    private final Letter[][] letterContainer = new Letter[DIMENSIONS][DIMENSIONS];
    // Current word the user has selected. We don't want to use a simple array since
    // we don't know the length of the word the user will make.
    private final java.util.List<Character> currentWord = new ArrayList<>();
    private int userScore = 0; // Current score for the user
    // Master wordlist. Using a HashSet for O(1) insertion and lookups,
    // compared to O(log(n)) with binary search.
    private final Set<String> wordList = new HashSet<>();
    private final String boardFile = "files/savedBoard.txt";
    // Set of words that have already been found
    private final Set<String> foundWords = new HashSet<>();

    /**
     * mouseDown Getter
     *
     * @return true if mouse down
     */
    public boolean isMouseDown() {
        return mouseDown;
    }

    /**
     * mouseDown Setter
     *
     * @param mouseDown mouseDown
     */
    public void setMouseDown(boolean mouseDown) {
        this.mouseDown = mouseDown;
    }

    /**
     * currentWord Getter
     *
     * @return currentWord
     */
    public java.util.List<Character> getCurrentWord() {
        return currentWord;
    }

    //================================================================================
    // Constructor
    //================================================================================

    public LetterGrid(JTextArea status) {
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

        // Set status
        this.gameTime = status;

        // Use a grid layout for the letters
        this.setLayout(new GridLayout(DIMENSIONS, DIMENSIONS, 20, 20));

        // Generate grid, load our word dictionary, start the clock
        generateLetters();
        loadDict();
        startTime = System.currentTimeMillis();

    }


    //================================================================================
    // Game Logic
    //================================================================================


    /**
     * Helper method to get time elapsed, in seconds
     *
     * @return time elapsed, in seconds
     */
    public long getRemainingTime() {
        return Math.max(0, 80 - ((System.currentTimeMillis() - startTime) / 1000));
    }


    /**
     * Generate letters for the grid
     */
    public void generateLetters() {
        Random r = new Random();
        MouseHelper mh = new MouseHelper(this);
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = 0; j < DIMENSIONS; j++) {
                // Generate a random character and add it to the array
                char c = (char) (r.nextInt(26) + 'a');
                Letter l = new Letter(c);
                letterContainer[i][j] = l;
                l.addMouseListener(mh);
                this.add(l);
            }
        }
    }

    /**
     * Check if a word is valid, and assign the proper credit if so
     */
    public void evaluateWord() {
        if (!currentWord.isEmpty()) {
            // Convert from List<Character> to String
            String word = currentWord.stream().map(Object::toString).collect(Collectors.joining());
            // If it's valid and not already been used
            if (wordList.contains(word) && !foundWords.contains(word) && word.length() >= 3) {
                lastWordStatus = word + " is valid (+" +  getWordScore(word) + ")";
                userScore += getWordScore(word);
                foundWords.add(word); // Add to list of used words
            } else {
                lastWordStatus = word + " is invalid";
            }
        }

        // Reset for a new word
        mouseDown = false;
        currentWord.clear();
        for (Letter[] i : letterContainer) {
            for (Letter lc : i) {
                lc.setSelected(false);
            }
        }
    }

    /**
     * Helper function to score a word
     *
     * @param s word input
     * @return point value as an int
     */
    public int getWordScore(String s) {
        int[] wordValues = {0, 0, 0, 400, 800, 1200, 2000, 3000, 5000, 8000, 10000};
        return wordValues[Math.min(s.length(), 10)]; // Cap word points at 10 letters long
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {

        playing = true;
        gameTime.setText("Running...");
        userScore = 0;
        startTime = System.currentTimeMillis();
        foundWords.clear();

        // Re-randomize the grid
        Random r = new Random();
        for (Letter[] i : letterContainer) {
            for (Letter lc : i) {
                lc.setLetter((char) (r.nextInt(26) + 'a'));
                lc.setSelected(false);
            }
        }
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
            gameTime.setText(getRemainingTime() +
                    " Seconds Remaining          Current score is " + userScore +
                    "\n" + lastWordStatus);
            // If time's up, end the game
            if (getRemainingTime() == 0) {
                playing = false;
                gameTime.setText("Game Over! Your final score is " + userScore);
            }
            // update the display
            repaint();
        }
    }


    /**
     * Solves the grid and displays a list of all the valid words
     */
    public void solve() {
        // Build a Trie with our word dictionary
        Trie wordTrie = new Trie();
        for (String s : this.wordList) {
            wordTrie.add(s);
        }

        // Convert to an int array.
        int[][] pGrid = new int[DIMENSIONS][DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = 0; j < DIMENSIONS; j++) {
                pGrid[i][j] = letterContainer[i][j].getLetter() - 'a';
            }
        }

        HashSet<String> foundWords = new HashSet<>(); // Words found so far
        // Grid to hold the indexes we have searched.
        boolean[][] searched = new boolean[DIMENSIONS][DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = 0; j < DIMENSIONS; j++) {
                int cl = pGrid[i][j]; // current letter representation as int
                Trie.TrieNode curNode = wordTrie.rootNode.children[cl];
                if (curNode != null) {
                    String curString = "" + ((char) (cl + 'a'));
                    searched[i][j] = true;
                    dfs(pGrid, searched, i, j, curNode, curString, foundWords);
                    searched[i][j] = false;
                }
            }
        }

        int maxScore = 0; // Max possible score
        for (String s : foundWords) {
            maxScore += getWordScore(s);
        }
        JOptionPane.showMessageDialog(null, "Max score is " + maxScore + ". " +
                "\n List of possible words are: " + foundWords.toString());
    }


    void dfs(int[][] pGrid, boolean[][] searched, int y, int x,
             Trie.TrieNode curNode, String curString, HashSet<String> foundWords) {

        // If the current string is a valid string that's long enough, add it to our list
        if (curNode.isLast && curString.length() >= 3) {
            foundWords.add(curString);
        }

        // dy, dx vectors
        int[][] dirVectors = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};
        for (int[] dir : dirVectors) {
            int y2 = y + dir[0];
            int x2 = x + dir[1];
            // Check if we are in a valid position we haven't been to yet
            if (y2 >= 0 && y2 < DIMENSIONS && x2 >= 0 && x2 < DIMENSIONS && !searched[y2][x2]) {
                int nextChar = pGrid[y2][x2];
                Trie.TrieNode nextNode = curNode.children[nextChar];
                // Does this character exist in the trie?
                if (nextNode != null) {
                    searched[y2][x2] = true; // mark this node as searched
                    // convert back to string
                    String nextString = curString + ((char) (nextChar + 'a'));
                    dfs(pGrid, searched, y2, x2, nextNode, nextString, foundWords);
                }
                searched[y2][x2] = false; // mark this node as un-searched once we finish it
            }
        }
    }


    //================================================================================
    // File I/O
    //================================================================================

    /**
     * Loads a board from the BOARD_FILE. The file format consists of two lines:
     * The first line is the board name, and the second line has 16 chars representing
     * the letter grid.
     */
    public void loadBoard() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(boardFile));
            String boardName = br.readLine();
            stringToBoard(br.readLine());
            JOptionPane.showMessageDialog(null, boardName + " loaded successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File Load Failed");
        }
    }

    /**
     * Saves the current board to the BOARD_FILE. The file format consists of two lines:
     * The first line is the board name, and the second line has 16 chars
     * representing the letter grid.
     */
    public void saveBoard() {
        String boardName = JOptionPane.showInputDialog("Please enter a name for this board: ");
        if (boardName == null) {
            return;
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(boardFile));
            bw.write(boardName);
            bw.newLine();
            bw.write(boardToString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File Save Failed");
        }
    }


    /**
     * Loads a dictionary from a txt file
     */
    public void loadDict() {
        try {
            // Where the word list is stored
            String dictionaryFile = "files/dictionary.txt";
            BufferedReader br = new BufferedReader(new FileReader(dictionaryFile));
            String l;
            while ((l = br.readLine()) != null) {
                wordList.add(l);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Word List Load Failed!");
            throw new IllegalArgumentException("Word List Load Failed!");
        }
    }

    /**
     * Helper method converts a board to a string
     *
     * @return String representation of length 16
     */
    public String boardToString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = 0; j < DIMENSIONS; j++) {
                s.append(letterContainer[i][j].getLetter());
            }
        }
        return s.toString();
    }

    /**
     * Helper method converts a string to a board grid state
     */
    public void stringToBoard(String s) {
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = 0; j < DIMENSIONS; j++) {
                letterContainer[i][j].setLetter(s.charAt(i * DIMENSIONS + j));
            }
        }
    }

    //================================================================================
    // Misc. Graphical
    //================================================================================

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(128, 168, 122));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (int i = 0; i < DIMENSIONS; i++) {
            for (int j = 0; j < DIMENSIONS; j++) {
                letterContainer[i][j].paintComponent(g);
            }
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}