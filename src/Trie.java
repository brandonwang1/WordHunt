import java.util.Arrays;

/**
 * Class to create a Trie for storing words. Used since we have O(S) lookup where S is the length of the string,
 * and since it allows us to prune the search if we reach a null Trie node.
 */
public class Trie {
    public static int DICT_SIZE = 26; // only using 26 lowercase letters

    TrieNode rootNode = new TrieNode();

    class TrieNode {
        TrieNode[] children = new TrieNode[DICT_SIZE];
        boolean isLast; // is this a leaf?
        public TrieNode() {
            // Initialize to all empty
            Arrays.fill(children, null);
            this.isLast = false;
        }
    }

    /**
     * Inserts a string into the Trie, iterating through char by char.
     * @param s string to insert
     */
    public void add(String s) {
        TrieNode currentNode = rootNode;
        for (int i = 0; i < s.length(); i++) {
            int cIndex = s.charAt(i) - 'a';
            // if the child node is null, make a new trieNode, otherwise just assign the child
            currentNode = currentNode.children[cIndex] == null ?
                    (currentNode.children[cIndex] = new TrieNode()) : currentNode.children[cIndex];
        }
        currentNode.isLast = true;
    }

    /**
     * Check if a string is in our Trie
     * @param s string to look for
     * @return true if it exists
     */
    public boolean contains(String s) {
        TrieNode currentNode = rootNode;
        for (int i = 0; i < s.length(); i++) {
            int cIndex = s.charAt(i) - 'a';
            // if the child node is null, make a new trieNode, otherwise just assign the child
            if (currentNode.children[cIndex] == null) {
                return false;
            } else {
                currentNode = currentNode.children[cIndex];
            }
        }
        // Are we at a valid node, and do we have a string that ends here?
        if (currentNode != null) {
            return currentNode.isLast;
        } else {
            return false;
        }
    }

    /**
     * Getter for rootNode
     * @return rootNode
     */
    public TrieNode getRootNode() {
        return rootNode;
    }
}
