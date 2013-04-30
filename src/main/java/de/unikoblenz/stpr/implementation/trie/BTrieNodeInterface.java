package de.unikoblenz.stpr.implementation.trie;

public interface BTrieNodeInterface {
	/**
	 * Get character represented by Node
	 * @return c
	 */
	public char getChar();
	
	/**
	 * Returns child node with character c.
	 * Returns null if no child is present.
	 * @param c
	 * @return
	 */
	public BTrieNodeInterface getChild(char c);
	
	/**
	 * Set child node n for character c
	 * @param c
	 * @param n
	 */
	public void setChild(char c, BTrieNode n);
	
	/**
	 * Returns child node with character c.
	 * Generates a new node if it does not yet exist.
	 * @param c
	 * @return n
	 */
	public BTrieNodeInterface addGetChild(char c);
	
}
