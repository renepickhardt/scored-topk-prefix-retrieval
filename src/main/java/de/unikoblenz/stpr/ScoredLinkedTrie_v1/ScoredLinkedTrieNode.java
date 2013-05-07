package de.unikoblenz.stpr.ScoredLinkedTrie_v1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class ScoredLinkedTrieNode {
    // Trie Variables
    private byte value; // signed byte in [-128 .. 127]
    private LinkedList<ScoredLinkedTrieNode> children;

    // Score Variables
    private int score = 0;
    
    public static final int TOP_K = 2;
    
    public ScoredLinkedTrieNode[] topChilds;
    public int[] topScores;
    
    public ScoredLinkedTrieNode(char c, int s) {
        setChar(c);
        setScore(s);
    }

    public void setScore(int s){
        this.score = s;
    }

    public int getScore(){
        return score;
    }
    
    public void setChar(char c) {
        this.value = (byte) c;
    }

    public char getChar() {
        return (char) value;
    }

    public boolean isLeaf() {
        return children == null || children.size() == 0;
    }
        
    public ScoredLinkedTrieNode getChild(char c) {
        if (children == null) {
            return null;
        }
        for (ScoredLinkedTrieNode n : children) {
            if (n.getChar() == c) {
                return n;
            }
        }
        return null;
    }

    public void setChild(ScoredLinkedTrieNode n) {
        if (children == null) {
            children = new LinkedList<ScoredLinkedTrieNode>();
        }
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
        if (children != null) { 
        for (ScoredLinkedTrieNode n : children) {
            if (n == null) {
                continue;
            }
            for (String subline : n.recString()) {
                if (fisrtLine) {
                    lines.add(getChar() + "{" + getScore() +  "}["+ printTopChildScores() +"]\t" + subline);
                } else {
                    lines.add(getChar() + "{" + getScore() +  "}["+ printTopChildScores() +"]\t" + subline);
                }
                fisrtLine = false;
            }
        }}
        if (fisrtLine) {
            lines.add("" + getChar() + "{" + getScore() +  "}["+ printTopChildScores() +"]" );
        }
        return lines;
    }

    /**
     * Returns a list of pairs (child_node_i, top_score_i),
     * where top_scores_i ranges over the Top-K scores in the __whole subtrie__
     * based at the node. And child node points to the corresponding child
     * which has to be retrieved first in order to arrive at this node.
     * 
     * @return 
     */
           
    public Queue<TopScoreEntry> getSetTopChildScores() {
        PriorityQueue<TopScoreEntry> top = new PriorityQueue<TopScoreEntry>();
        
        // Add this node to topQueue
        top.add(new TopScoreEntry(this, this.score));

        if (!isLeaf()) {
            // Descend
            for (ScoredLinkedTrieNode child : children) {
                for (TopScoreEntry childTopEntry : child.getSetTopChildScores()) {
                    top.add(new TopScoreEntry(child, childTopEntry.topScore));
                }
            }
        }
        
        // TruncateQueue and SetVariables
        Queue<TopScoreEntry> ret  = new LinkedList<TopScoreEntry>();
        int LIM = Math.min(top.size(), TOP_K);
        this.topChilds = new ScoredLinkedTrieNode[LIM];
        this.topScores = new int[LIM];
        for(int i = 0; i < LIM; i++){
            TopScoreEntry e = top.poll();
            this.topChilds[i] = e.n;
            this.topScores[i] = e.topScore;
            ret.add(e);
        }
        return ret;
    }
    
    public String printTopChildScores(){
        if (topScores == null) {
            return "";
        }
        String out = "";
        for (int s : topScores) {
//            out += e.n.getChar() + ":" + e.topScore + " "; 
            out += s + " ";
        }
        return out.trim();
    }

    
    class TopScoreEntry implements Comparable<TopScoreEntry> {
        public ScoredLinkedTrieNode n;
        public int topScore;

        public TopScoreEntry(ScoredLinkedTrieNode n, int s) {
            this.topScore = s;
            this.n = n;
        }

        public int compareTo(TopScoreEntry o) {
            return o.topScore - this.topScore;
        }
    }

    
}
