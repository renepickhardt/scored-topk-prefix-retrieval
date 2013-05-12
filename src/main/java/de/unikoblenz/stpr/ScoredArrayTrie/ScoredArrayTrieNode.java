package de.unikoblenz.stpr.ScoredArrayTrie;

import de.unikoblenz.stpr.ArrayTrie.*;
import de.renepickhardt.utils.IOHelper;
import de.unikoblenz.stpr.interfaces.trie.TrieNodeInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class ScoredArrayTrieNode implements TrieNodeInterface {

    // value of node is immutable signed byte in [-128 .. 127]
    // Character conversion is done via (byte) / (char)
    private final byte value;
    // Array for child nodes
    private ScoredArrayTrieNode[] children;
    // ASCII range covered by children
    public static int MIN_CHAR = 44;
    public static int MAX_CHAR = 123;
    private static final int CHILD_ARRAY_LEN = MAX_CHAR - MIN_CHAR;

    // Score
    public int score;
//    public int maxScore; // maximum score of subtrie
    public static int TOP_K = 5;
    public List<TopScoreEntry> topChilds;

    public ScoredArrayTrieNode(char c, int s) {
        this.value = (byte) c;
        this.score = s;

        // we do not want to initialize an array here to save memory
        this.children = null;
        this.topChilds = new LinkedList<TopScoreEntry>();
        topChilds.add(new TopScoreEntry(this, score));
    }

    public char getChar() {
        return (char) this.value;
    }

    /**
     * True if node has no children.
     *
     * @return isLeaf
     */
    public boolean isLeaf() {
        if (children == null) {
            return true;
        }
        for (ScoredArrayTrieNode n : children) {
            // Child found?
            if (n != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns array of children. Initializes variable if necessary.
     *
     * @return children
     */
    public ScoredArrayTrieNode[] getChildren() {
        if (children == null) {
            IOHelper.log("Initializing child Array");
            children = new ScoredArrayTrieNode[CHILD_ARRAY_LEN];
        }
        return children;
    }

    public static int childIndex(char c) {
        int b = (int) (byte) c; // byte is signed!
        if (b < 0) {
            b = 256 + b;
        }
        return b - MIN_CHAR;
    }

    /**
     * Inserts Node n as child.
     *
     * @param node
     */
    public void setChild(ScoredArrayTrieNode n) {
        try {
            getChildren()[childIndex(n.getChar())] = n;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Character " + n.getChar() + " out of bounds.");
        }
    }

    public ScoredArrayTrieNode getChild(char c) {
        if (children == null) {
            return null;
        }
        return getChildren()[childIndex(c)];
    }

    /**
     * Returns child node corresponding to character if it exists if not, it
     * generates a new node for this character and inserts it as a child
     *
     * @param character
     * @return child
     */
    public ScoredArrayTrieNode addGetChild(char c, int s) {
        ScoredArrayTrieNode n = this.getChild(c);
        if (n == null) {
            n = new ScoredArrayTrieNode(c, s);
            this.setChild(n);
        }
        return n;
    }

    public int getScore() {
        return score;
    }

    /**
     * Computes maximum score of the subtrie with root this.
     *
     * @return
     */
    public int calcMaxScore() {
        if (isLeaf()) {
            return score;
        }
        int max = score;
        for (ScoredArrayTrieNode n : getChildren()) {
            if (n == null) {
                continue;
            }
            max = Math.max(max, n.calcMaxScore());
        }
        return max;
    }

    /**
     * Calculates topChild array. Consisting of Entries (node, maxScore) where
     * node is a child node and maxSocre is the maximum score of the trie with
     * root n. The topChild list is sorted by maxScore and has a maximal lenght
     * of TOP_K
     */
    public List<TopScoreEntry> calcTopChilds() {
        PriorityQueue<TopScoreEntry> ranking = new PriorityQueue<TopScoreEntry>();
        ranking.add(new TopScoreEntry(this, this.score));
        for (ScoredArrayTrieNode n : getChildren()) {
            if (n == null) {
                continue;
            }
            ranking.add(new TopScoreEntry(n, n.calcMaxScore()));
        }

        topChilds = new LinkedList<TopScoreEntry>();
        for (int i = 0; i < TOP_K; i++) {
            if (ranking.size() == 0) {
                break;
            }
            topChilds.add(ranking.poll());
        }
        return topChilds;
    }

    /**
     * Recursively sets the topChilds Array for all nodes in the subtrie
     * with root this.
     * @return maxScore
     */
    public int recSetTopChilds() {
        // leafs have correct top scores
        if (isLeaf()) {
            return score;
        }

        PriorityQueue<TopScoreEntry> ranking = new PriorityQueue<TopScoreEntry>();
        ranking.add(new TopScoreEntry(this, this.score));
        for (ScoredArrayTrieNode c : getChildren()) {
            if (c == null) {
                continue;
            }
            // Here the recursion happens
            ranking.add(new TopScoreEntry(c, c.recSetTopChilds()));
        }

        topChilds = new LinkedList<TopScoreEntry>();
        for (int i = 0; i < TOP_K; i++) {
            if (ranking.size() == 0) {
                break;
            }
            topChilds.add(ranking.poll());
        }
        return topChilds.get(0).score;
    }

    public void insertTopChildEntry(TopScoreEntry e) {
        int i = 0;
        // Search position for e
        for (TopScoreEntry f : topChilds) {
            if (e.score > f.score) {
                break;
            }
            i++;
        }

        // Insert e if not worse than last element
        if (i < TOP_K - 1) {
            topChilds.add(i, e);
        }

        // Truncate topChilds
        while (topChilds.size() > TOP_K) {
            topChilds.remove(topChilds.size() - 1);
        }
    }

    public void removeTopEntry(ScoredArrayTrieNode n) {
        for (TopScoreEntry f : topChilds) {
            if (f.node == n) {
                topChilds.remove(f);
                return;
            }
        }
    }

    /**
     * Inserts n as child node and updates topChilds
     *
     * @param n
     */
    public void insertChid(ScoredArrayTrieNode n) {
        ScoredArrayTrieNode oldChild = getChild(n.getChar());
        if (oldChild != null) {
            removeTopEntry(oldChild);
        }

        insertTopChildEntry(new TopScoreEntry(n, n.getTopScore()));
        setChild(n);
    }

    public int getTopScore() {
        return topChilds.get(0).score;
    }

    @Override
    public String toString() {
        String out = "";
        for (String line : this.recString()) {
            out += line + "\n";
        }
        return out;
    }

    private ArrayList<String> recString() {
        ArrayList<String> lines = new ArrayList<String>();
        Boolean fisrtLine = true;
        for (ScoredArrayTrieNode n : getChildren()) {
            if (n == null) {
                continue;
            }
            for (String subline : n.recString()) {
                if (fisrtLine) {
                    lines.add(this.getChar() + subline);
                } else {
                    lines.add(" " + subline);
                }
                fisrtLine = false;
            }
        }
        if (fisrtLine) {
            lines.add("" + this.getChar());
        }
        return lines;
    }
}
