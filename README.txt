=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: brandw
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D arrays - the board state is modeled as a NxN array of Letters in the MouseHelper class,
  which is displayed to the user as a 2D grid of randomly generated chars (which is why it's an appropriate model).

  2. Collections - the current word that the user has dragged is stored as an ArrayList<Character> currentWord in the
  MouseHelper class. It stores the letters the user has moused over, in order. This is preferable to using a list since
  we don't know the size/length of the array the user will generate. ArrayList preserves amortized O(1) insertion so
  it's a better choice than LinkedList as well.

  3. File I/O - Used to load and save board states to a txt file. Each state consists of two lines. Line 1 has a name for
   the board, and line 2 contains the letters in the board compressed from a 2d array into a 1d array of length N^2.
   One example string for a 4x4 grid would be "abcdefghijklmnop". The I/O operations are implemented using BufferedReader/Writer.

  4. Recursive algorithm - Solutions and the Max Score are found using DFS through the grid. We need to use DFS and pruning
  with a trie in order to decrease the complexity to O(8^N^2). It's near-impossible to solve with only iteration for
  performance reasons, and since we have to look "backwards" as well.

  5. Data Structure - Implemented a trie to store strings in the Trie class, used for fast insertions and word lookups
   (O(S) where S is string length). I also used it to optimize the DFS with early pruning. If the currently considered
   string reaches a null node, then we can end the search. Since the number of words in the dictionary is relatively small,
   this is very efficient.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  Game - initializes the game frame and all of the buttons and text displays.
  Letter - class representing a single letter on the board grid, and code to draw it.
  LetterGrid - handles the main game display and logic, including the board, I/O, and solver.
  MouseHelper - updates the game state/view depending on the user's mouse actions (i.e. selecting letters).
  Trie - data structure for implementing a Trie, used in the solver code.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  I had to figure out what data structure I was going to DFS on, which took a bit of thinking. Also had to debug
  the DFS itself for a while.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  Overall clean/decent, although I don't separate into MVC very well for the LetterGrid Class (which I would refactor).
   MouseHelper handles most of the mouse controls, and Trie, LetterGrid, and Letter represent the model.
   All class variables in Letter and letterGrid are properly encapsulated.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.

  Used the Word Hunt game, and this link for Trie:
  http://web.stanford.edu/class/archive/cs/cs166/cs166.1146/lectures/09/Small09.pdf
