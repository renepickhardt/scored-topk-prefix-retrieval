package de.unikoblenz.stpr.implementation.trie;

import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.lang3.tuple.Pair;

public class ScoredTrieNode<ScoreType extends Comparable<? super ScoreType>> extends BTrieNode {
	
	private ScoreType score;
	
	public final short K_TOP = 5;
	private short[] topScores;
	
	public ScoredTrieNode(char value, ScoreType score) {
		super(value);
		this.score = score;
		this.topScores = new short[K_TOP];
	}

	public ScoreType getScore(){
		return score;
	}
	
	public void setTopScores(){
		class Elt implements Comparable<Elt> {
			public char c;
			public ScoreType s;
			
			public Elt(char c, ScoreType s) {
				this.c = c; this.s = s;
			}
			
			public int compareTo(Elt o) {
				return s.compareTo(o.s);
			}
		}
		PriorityQueue<Elt> q = new PriorityQueue();
		for ( Pair<Character, BTrieNode> p: getChildren() ){
			char c = p.getLeft();
			BTrieNode n = p.getRight();
			
		}
	}
	
}
