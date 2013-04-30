package de.unikoblenz.stpr.implementation.trie;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class TrieNode<ScoreType extends Comparable<? super ScoreType>> implements TrieNodeInterface<ScoreType>{
	// Node Attributes
	private Character  character;
	
	// Trie Structure
	private TreeMap<Character, TrieNode<ScoreType>> children;
	private TrieNode<ScoreType> 	parent;

	// Scored query structures
	private ScoreType 	score;
	private ScoreType 	maxChildScore;
	private LinkedList<TrieNode<ScoreType>> sortedChildren;	
	
	/**
	 * Construct Node with given label and score
	 * @param label
	 * @param score
	 */
	public TrieNode(Character character, ScoreType score){
		this.character = character;
		this.score = score;
	};
	
	public ScoreType getNodeScore() {
		return score;
	}

	public Boolean isKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public TrieNodeInterface traverse(Character nextChar) {
		// TODO Auto-generated method stub
		return null;
	}

	public ScoreType getMaxChildScore() {
		// TODO Auto-generated method stub
		return null;
	}

	public TrieNodeInterface getTopNode(Integer k) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getTopScore(Integer k) {
		// TODO Auto-generated method stub
		return null;
	}

	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

	public Character getCurrentChar() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getSortedNodeList() {
		// TODO Auto-generated method stub
		return null;
	}
}
