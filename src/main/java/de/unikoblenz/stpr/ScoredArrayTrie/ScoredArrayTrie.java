package de.unikoblenz.stpr.ScoredArrayTrie;

import de.unikoblenz.stpr.ArrayTrie.*;
import de.unikoblenz.stpr.interfaces.trie.TrieInterface;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ScoredArrayTrie {
    // Variables

    public ScoredArrayTrieNode root;

    // Constructor
    public ScoredArrayTrie() {
        root = new ScoredArrayTrieNode('.', 0);
    }

    // Methods
    /**
     * Adds a string to the Trie.
     * If the string is not present new nodes are created.
     * If the string is already there, the score is overwritten with
     * the provided score.
     * The root node is not modified
     */
    public void add(String s, int score) {
        if (s.length() == 0) {
            return;
        }
        ScoredArrayTrieNode last = root;
        for (int i = 0; i < s.length(); i++) {
            last = last.addGetChild(s.charAt(i));
        }
        last.setScore(score);
        // last.updateMaxScore();
    }

    // Default score = 1
    public void add(String s) {
        add(s, 1);
    }

    /**
     * Looks up the String s in the trie and returns the list of nodes traversed
     * on the way. All lists start with the root node (".") E.g. s = "" a with
     * the root node is returned. If the string is not in the trie null is
     * returned.
     *
     * @param s
     * @return path
     */
    public List<ScoredArrayTrieNode> find(String s) {
        LinkedList<ScoredArrayTrieNode> path = new LinkedList<ScoredArrayTrieNode>();
        path.add(root);
        for (int i = 0; i < s.length(); i++) {
            ScoredArrayTrieNode child = path.getLast().getChild(s.charAt(i));
            if (child == null) {
                return null;
            }
            path.add(child);
        }
        return path;
    }

    public String toString() {
        return root.toString();
    }

    /**
     * Finds top-k words stored in the trie that match prefix.
     * Assumes: trie is properly setup with topChilds and maxScores
     * Returns: Sorted list of resuts. Each result contains a string (= retireved word)
     * and the corresponding score in an SearchResult object.
     * 
     * If no results are present null is returned.
     * 
     * @param prefix
     * @param k
     * @return resultList
     */
    public List<SearchResult> getTopK(String prefix, final int k){
        return null;
    }
    
    
}
