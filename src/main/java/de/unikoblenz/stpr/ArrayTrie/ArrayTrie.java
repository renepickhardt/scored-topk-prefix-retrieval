package de.unikoblenz.stpr.ArrayTrie;

import de.unikoblenz.stpr.interfaces.trie.TrieInterface;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArrayTrie implements TrieInterface {
	// Variables
        ArrayTrieNode root;

	// Constructor
	public ArrayTrie() {
		root = new ArrayTrieNode('.');
	}

	// Methods
        
        /**
         * Adds a string to the trie.
         * @param s 
         */
	public void add(String s){
		ArrayTrieNode last = root;
		for (int i = 0; i < s.length(); i++ ){
			last = last.addGetChild(s.charAt(i));
		}
	}
        
        /**
         * Looks up the String s in the trie and returns
         * the list of nodes traversed on the way.
         * All lists start with the root node (".")
         * E.g. s = "" a with the root node is returned.
         * If the string is not in the trie null is returned.
         * 
         * @param s
         * @return path
         */
        public List<ArrayTrieNode> find(String s){
            LinkedList<ArrayTrieNode> path = new LinkedList<ArrayTrieNode>();
            path.add(root);
            for (int i = 0; i < s.length(); i++ ){
                ArrayTrieNode child = path.getLast().getChild(s.charAt(i));
                if (child == null) return null;
		path.add(child);
            }
            return path;
        }

	public String toString(){
		return root.toString();
	}
}
