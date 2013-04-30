package de.unikoblenz.stpr.implementation.trie;

import java.util.List;


/**
 * TrieNode Interface
 * 
 * @author hartmann, rpickhardt
 */
public interface TrieNodeInterface<ScoreType extends Comparable<? super ScoreType>> {
	/**
	 * Get label of current node.
	 * @return label
	 */
	public Character getCurrentChar();
	
	/**
	 * If the node represents a key, the score is returned.
	 * Else null is returned
	 * @return score
	 */
	public ScoreType getNodeScore();

	/**
	 * Does the node represent an actual key stored in the Trie,
	 * or is it a helper node for retireval.
	 * @return isKey
	 */
	public Boolean isKey();
		
	/**
	 * Returns child node for given character
	 * @param nextChar
	 * @return childNode
	 */
	public TrieNodeInterface<ScoreType> traverse(Character nextChar);
	
	/**
	 * Returns maximum score of all child nodes
	 * @return score
	 */
	public ScoreType getMaxChildScore();
	
	/**
	 * Returns List of child nodes sorted by MaxChildScore
	 * @param k
	 * @return childNode
	 */
	public List<TrieNodeInterface<ScoreType>> getSortedNodeList();
		
	/**
	 * Returns String representation of Node
	 */
	public String print();
}
