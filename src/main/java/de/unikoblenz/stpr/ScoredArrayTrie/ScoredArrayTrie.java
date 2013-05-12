package de.unikoblenz.stpr.ScoredArrayTrie;

import de.unikoblenz.stpr.ArrayTrie.*;
import de.unikoblenz.stpr.interfaces.trie.TrieInterface;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ScoredArrayTrie {
    // Variables

    ScoredArrayTrieNode root;

    // Constructor
    public ScoredArrayTrie() {
        root = new ScoredArrayTrieNode('.', 0);
    }

    // Methods
    /**
     * Adds a string to the Trie.
     */
    public void add(String s, int score) {
        if (s.equals("")) {
            return;
        }
        ScoredArrayTrieNode last = root;
        for (int i = 0; i < s.length() - 1; i++) {
            last = last.addGetChild(s.charAt(i), 0);
        }
        last.setChild(new ScoredArrayTrieNode(s.charAt(s.length() - 1), score));
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

    public void insert(String s, int score){
        LinkedList<ScoredArrayTrieNode> path = new LinkedList<ScoredArrayTrieNode>();
        path.add(root);
        for (int i = 0; i < s.length() - 1; i++) {
            path.push(path.peek().addGetChild(s.charAt(i),0));
        }
        
        char c = s.charAt(s.length() - 1);
        ScoredArrayTrieNode endNode = new ScoredArrayTrieNode(c, score);
        
        if (path.peek().getChild(c)== null){
            path.peek().insertChid(endNode);
        }
        
        path.peek().insertChid(endNode);
        
        
        
    }

}
