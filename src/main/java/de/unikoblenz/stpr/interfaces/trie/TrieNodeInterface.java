package de.unikoblenz.stpr.interfaces.trie;

import de.unikoblenz.stpr.ArrayTrie.ArrayTrieNode;

/**
 * Limited use due to the fact that 
 * @author hartmann
 */
public interface TrieNodeInterface {
	/**
	 * Get character represented by Node
	 * @return c
	 */
	public char getChar();
	
        public void setChar(char c);
        
	/**
	 * Returns child node with character c.
	 * Returns null if no child is present.
	 * @param c
	 * @return
	 */
	public TrieNodeInterface getChild(char c);
	
	/**
	 * Set child node n for character c
	 * @param c
	 * @param n
	 */
        //public void setChild(char c, <Node implements TrieNodeInterface> n);
	
	/**
	 * Returns child node with character c.
	 * Generates a new node if it does not yet exist.
	 * @param c
	 * @return n
	 */
	//public TrieNodeInterface addGetChild(char c);
}
