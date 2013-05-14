package de.unikoblenz.stpr.ScoredArrayTrie;

import de.unikoblenz.stpr.ArrayTrie.*;
import de.renepickhardt.utils.IOHelper;
import de.unikoblenz.stpr.interfaces.trie.TrieNodeInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class ScoredArrayTrieNode implements TrieNodeInterface, Comparable<ScoredArrayTrieNode> {
    // value of node is immutable signed byte in [-128 .. 127]
    // Character conversion is done via (byte) / (char)
    private final byte value;
    // Array for child nodes
    private ScoredArrayTrieNode[] children;
    
    public String word;
    
    // ASCII range covered by children
    public static int MIN_CHAR = 0;
    public static int MAX_CHAR = 256;
    private static final int CHILD_ARRAY_LEN = MAX_CHAR - MIN_CHAR;
    
    // Score
    public int score;
    public int maxScore; // maximum score of subtrie with root this.
    public static int TOP_K = 5;
    public List<ScoredArrayTrieNode> topChilds; // TOP_K children sorted by maxScore

    public ScoredArrayTrieNode(char c, int s) {
        this.value = (byte) c;
        this.score = s;

        // we do not want to initialize an array here to save memory
        this.children = null;

        this.maxScore = s;
        this.topChilds = new LinkedList<ScoredArrayTrieNode>();
    }

    /*
     * BARE TRIE METHODS - NO SCORE HANDLING AT ALL
     */
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
            //  IOHelper.log("Initializing child Array");
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
     * generates a new node with score 0 for this character and inserts it as a
     * child
     *
     * @param character
     * @return child
     */
    public ScoredArrayTrieNode addGetChild(char c) {
        ScoredArrayTrieNode n = this.getChild(c);
        if (n == null) {
            n = new ScoredArrayTrieNode(c, 0);
            this.setChild(n);
        }
        return n;
    }

    /*
     * SCORES AND TOP CHILDS
     */
    public int getScore() {
        return score;
    }

    /**
     * Setter for score variable. Warning: Does not update maxScore! Use
     * updateMaxScore();
     */
    public void setScore(int score) {
        this.score = score;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * Updates maxScore variable. Assumes topScore is set correctly for childs;
     * @Warning inefficient if prior knowledge present.
     */
    public void updateMaxScore() {
        int max = this.score;
        for (ScoredArrayTrieNode n : getChildren()) {
            if (n == null) {
                continue;
            }
            max = Math.max(max, n.getMaxScore());
        }
        this.maxScore = max;
    }

    /**
     * Recursively computes maximum score of the subtrie with root this. No
     * assumptions on children
     *
     * @return maxScore;
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
     * Recursively set maxScore for the subtrie with root this.
     *
     * @return maxScore
     */
    public int recSetMaxScore() {
        if (isLeaf()) {
            setMaxScore(score);
            return score;
        }

        int max = score;
        for (ScoredArrayTrieNode n : getChildren()) {
            if (n == null) {
                continue;
            }
            max = Math.max(max, n.recSetMaxScore());
        }
        setMaxScore(max);
        return max;
    }

    /**
     * Calculates topChild List. List of Top-k child nodes sorted by maxScore.
     * Assumes maxScores is properly set for children.
     */
    public List<ScoredArrayTrieNode> calcTopChilds() {
        //TODO: use interval Heaps
        PriorityQueue<ScoredArrayTrieNode> ranking = new PriorityQueue<ScoredArrayTrieNode>();
        
        for (ScoredArrayTrieNode c : getChildren()) {
            if (c == null) {
                continue;
            }
            ranking.add(c);
        }

        LinkedList<ScoredArrayTrieNode> myTopChilds = new LinkedList<ScoredArrayTrieNode>();
        for (int i = 0; i < TOP_K; i++) {
            if (ranking.size() > 0) {
                // Priority Queue has least element first
                myTopChilds.add(ranking.poll());
            } else {
                break;
            }
        }
        return myTopChilds;
    }

    public void updateTopChilds() {
        this.topChilds = calcTopChilds();
    }

    public void recSetTopChilds() {
        updateTopChilds();
        if (isLeaf()) {
            return;
        }
        for (ScoredArrayTrieNode c : getChildren()) {
            if (c == null) {
                continue;
            }
            c.recSetTopChilds();
        }
    }

    public int compareTo(ScoredArrayTrieNode o) {
        return o.getMaxScore() - this.getMaxScore();
    }


    /*
     * AUTOMATIC UPDATING OF SCORES ON INSERT
     */
    /**
     * Inserts node n as child. Assumes: this.maxScore, this.topChilds are
     * correctly set. Assumes: n.getMaxScore() works properly Updates: maxScore,
     * topChilds to correct values.
     *
     * @param n
     */
    public void insertNewChild(ScoredArrayTrieNode n) {
        if (getChild(n.getChar()) != null) {
            throw new IllegalArgumentException("Child node already present for character " + n.getChar());
        }
        setChild(n);
        maxScore = Math.max(maxScore, n.getMaxScore());
        insertTopChild(n);
    }

    public void insertTopChild(ScoredArrayTrieNode n) {
        int i = 0;
        // Search position for e
        for (ScoredArrayTrieNode t : topChilds) {
            if (n.getMaxScore() > t.getMaxScore()) {
                break;
            }
            i++;
        }

        // Insert e if not worse than last element
        if (i < TOP_K - 1) {
            topChilds.add(i, n);
        }

        // Truncate topChilds
        while (topChilds.size() > TOP_K) {
            topChilds.remove(topChilds.size() - 1);
        }
    }

    /**
     * Sets child c to score s. Updates: maxScore, topChilds of this and child.
     * Assumes: There is a child with character c.
     *
     * @param c
     * @param s
     */
    public void updateChildNode(char c, int s) {
        ScoredArrayTrieNode n = getChild(c);
        n.setScore(s);
        n.updateMaxScore();

        updateMaxScore();
        while (topChilds.contains(n)) {
            topChilds.remove(n);
        }
        insertTopChild(n);
    }

    @Override
    public String toString() {
        String out = "";
        for (String line : this.recString()) {
            out += line + "\n";
        }
        return out;
    }

    public String topChildString() {
        String topC = "";
        for (ScoredArrayTrieNode t : topChilds) {
            topC += t.getChar() + "";
        }
        return topC;
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
                    lines.add(String.format("%s{%d}[%d|%s]\t", getChar(), getScore(), getMaxScore(), topChildString()) + subline);
                } else {
                    lines.add("\t\t" + subline);
                }
                fisrtLine = false;
            }
        }
        if (fisrtLine) {
            lines.add(String.format("%s{%d}[%d]", getChar(), getScore(), getMaxScore()));
        }
        return lines;
    }
}
