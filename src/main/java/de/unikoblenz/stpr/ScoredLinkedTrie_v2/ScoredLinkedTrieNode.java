package de.unikoblenz.stpr.ScoredLinkedTrie_v2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class ScoredLinkedTrieNode {
    // Trie Variables
    private byte value; // signed byte in [-128 .. 127]
    private LinkedList<ScoredLinkedTrieNode> children;

    // Score Variables
    private int score = 0;

    // TopScoreMaintenance
    public static final int TOP_K = 2;
    private int topChildScore = 0;
    private ScoredLinkedTrieNode topChildNode;
//    public ScoredLinkedTrieNode[] topChilds;
//    public int[] topScores;
    
    public ScoredLinkedTrieNode(char c, int s) {
        children = new LinkedList<ScoredLinkedTrieNode>();

        setChar(c);
        setScore(s);

        setTopChildScore(s);
        setTopChildNode(this);
    }

    /*
     * TRIE  METHODS
     */
    public void setChar(char c) {
        this.value = (byte) c;
    }

    public char getChar() {
        return (char) value;
    }

    public boolean isLeaf() {
        return children == null || children.size() == 0;
    }
    
    public List<ScoredLinkedTrieNode> getChildren() {
        return children;
    }
    
    public ScoredLinkedTrieNode getChild(char c) {        
        for (ScoredLinkedTrieNode n : getChildren()) {
            if (n.getChar() == c) {
                return n;
            }
        }
        return null;
    }

    public void setChild(ScoredLinkedTrieNode n) {
         children.addFirst(n);
    }

    /**
     * Returns child node corresponding to character if it exists if not, it
     * generates a new node
     *
     * @param character
     * @return child
     */
    public ScoredLinkedTrieNode addGetChild(char c, int s) {
        ScoredLinkedTrieNode n = getChild(c);
        if (n == null) {
            n = new ScoredLinkedTrieNode(c,s);
            setChild(n);
        } else {
            n.updateScore(s);
        }
        return n;
    }

    /*
     * SCORE HANDLING
     */
    public int getScore(){
        return score;
    }

    public void setScore(int s){
        this.score = s;
    }
    
    public int getTopScore() {
        return this.topChildScore;
    }
    
    public ScoredLinkedTrieNode getTopScoreChild() {
        return this.topChildNode;
    }
    
    public void setTopChildScore(int s){
        this.topChildScore = s;
    }
    
    public void setTopChildNode(ScoredLinkedTrieNode n){
        this.topChildNode = n;
    }

    public void updateTop(ScoredLinkedTrieNode n, int score){
        if( score > this.topChildScore ){
            this.topChildScore = score;
            this.topChildNode  = n;
        }
    }

    public void updateScore(int s){
        if (s > this.score){
            this.score = s;
            this.topChildNode = this;
            this.topChildScore = s;
        }
    }
    
    /**
     * Adds childNode and updates top scores.
     * Returns childNode
     * 
     * @param childValue
     * @param childScore 
     */
    public ScoredLinkedTrieNode getInsertChild(char childValue, int childScore) {
        ScoredLinkedTrieNode childNode = addGetChild(childValue, childScore);
        if (childScore > this.topChildScore) {
            this.topChildScore = childScore;
            this.topChildNode  = childNode;
        }
        return childNode;
    }
        
    /**
     * Sets topChildScore to max{ this.score, n.topChildScore }
     * where n ranges though all children.
     * 
     * @return topChildScore
     */
    public int getSetTopScore() {
        topChildScore = score;
        for (ScoredLinkedTrieNode n: children){
            if (n.getSetTopScore() > topChildScore) { 
                topChildScore = n.getSetTopScore();
                topChildNode  = n;
            }
        }
        return topChildScore;
    }    

    /*
     * SERIALIZATION
     */
    public String toString() {
        String out = "";
        for (String line : recString()) {
            out += line + "\n";
        }
        return out;
    }

    private ArrayList<String> recString() {
        ArrayList<String> lines = new ArrayList<String>();
        Boolean fisrtLine = true;
        for (ScoredLinkedTrieNode n : getChildren()) {
            if (n == null) {
                continue;
            }
            for (String subline : n.recString()) {
                if (fisrtLine) {
                    lines.add(getChar() + "{" + getScore() +  "}["+ printTopChildScores() +"]\t" + subline);
                } else {
                    lines.add("\t" + subline);
                }
                fisrtLine = false;
            }
        }
        if (fisrtLine) {
            lines.add("" + getChar() + "{" + getScore() +  "}["+ printTopChildScores() +"]" );
        }
        return lines;
    }
    
    public String printTopChildScores(){
        return String.valueOf(topChildScore);
    }
}