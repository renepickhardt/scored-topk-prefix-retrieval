package de.unikoblenz.stpr.implementation.trie;

/**
 * TrieNode Interface
 * 
 * @author hartmann, rpickhardt
 */
public interface TrieNodeInterface {
	/**
	 * Get label of current node.
	 * @return label
	 */
	public String getLabel();
	
	/**
	 * Does the node represent an actual key stored in the Trie,
	 * or is it a helper node for retireval.
	 * @return isKey
	 */
	public Boolean isKey();
	
	/**
	 * If the node represents a key, the score is returned.
	 * Else null is returned
	 * @return score
	 */
	public Integer getNodeScore();
	
	
	/**
	 * Returns child node for given character
	 * @param nextChar
	 * @return childNode
	 */
	public TrieNodeInterface traverse(Character nextChar);
	
	/**
	 * Returns maximum score of all child nodes
	 * @return score
	 */
	public Integer getMaxChildScore();
	
	/**
	 * Returns trie node with the k-th biggest maxChildScore
	 * Warning: if k > 5 null is returned.
	 * @param k
	 * @return childNode
	 */
	public TrieNodeInterface getTopNode(Integer k);
	
	/**
	 * Returns trie node with the k-th biggest maxChildScore
	 * Warning: if k > 5 null is returned.
	 * @param k
	 * @return childNode
	 */
	public Integer getTopScore(Integer k);
	
	/**
	 * Returns String representation of Node
	 */
	public String print();
}
