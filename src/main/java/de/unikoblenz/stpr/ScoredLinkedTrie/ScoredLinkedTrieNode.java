package de.unikoblenz.stpr.ScoredLinkedTrie;

import de.unikoblenz.stpr.ScoredLinkedTrie_v2.*;
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
    public static final int TOP_K = 10;
    
    public ScoredLinkedTrieNode[] topChilds;
    public int[] topScores;
    
    public ScoredLinkedTrieNode(char c, int s) {
        children = new LinkedList<ScoredLinkedTrieNode>();

        setChar(c);
        setScore(s);

        topScores = new int[0];
        topChilds =  new ScoredLinkedTrieNode[0];
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
    
    public int[] getTopScores() {
        return this.topScores;
    }
    
    public ScoredLinkedTrieNode[] getTopChilds() {
        return this.topChilds;
    }
    
    public void setTopChildScore(int[] s){
        this.topScores = s;
    }
    
    public void setTopChildNodes(ScoredLinkedTrieNode[] n){
        this.topChilds = n;
    }

    private void enlargeTopChilds(){
        int LEN = topChilds.length;
        ScoredLinkedTrieNode[] newTopChilds = new ScoredLinkedTrieNode[LEN + 1];
        System.arraycopy(topChilds, 0, newTopChilds, 0, LEN);
        topChilds = newTopChilds;
    }
    
    private void enlargeTopScores(){
        int LEN = topScores.length;
        int[] newTopScores = new int[LEN + 1];
        System.arraycopy(topScores, 0, newTopScores, 0, LEN);
        topScores = newTopScores;
    }
    
    public void pushTop(ScoredLinkedTrieNode topNode, int topScore){
        if (topScore == 0) { return; }
        if (topChilds.length < TOP_K){
            enlargeTopChilds();
            enlargeTopScores();
        }
        int tempScore = 0;
        ScoredLinkedTrieNode tempNode = null;
        int tempScore2 = 0;
        ScoredLinkedTrieNode tempNode2 = null;
        int STATUS = 0;
        for(int i = 0; i < topChilds.length; i++){
            // UPDATE VALUE OF topScores[i], topChilds[i]
            if (STATUS == 0 && topScore >= topScores[i]) {
                STATUS = 1;
                
                tempScore = topScores[i];
                tempNode  = topChilds[i];
                
                topScores[i] = topScore;
                topChilds[i] = topNode;
            } else if (STATUS == 1 ) {
                // SHIFT ENTRIES; ASSUMES tempScore, tempNode is Set
                tempScore2 = topScores[i];
                tempNode2  = topChilds[i];
                
                topScores[i] = tempScore;
                topChilds[i] = tempNode;
                
                tempScore = tempScore2;
                tempNode  = tempNode2;
            }
        }
    }
    
    public void updateTop(ScoredLinkedTrieNode childNode, int score) throws Exception {
        for(int i = 0; i < topChilds.length; i++ ){
            if (topChilds[i] == childNode){
                topScores[i] = score;
                return;
            }
        } 
        // notFound -> push
        pushTop(childNode, score);
    }

    public int getMinTopScore(){
        for (int i = topScores.length - 1; i >= 0; i--){
            if (topScores[i] > 0) {
                return topScores[i];
            }
        }
        return 0;
    }

    public int getMaxTopScore(){
        return topScores[0];
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
    
    public String printTopChildScores() {
        if (topScores == null) {
            return "";
        }
        String out = "";
        for (int s : topScores) {
            out += s + " ";
        }
        return out.trim();
    }

}