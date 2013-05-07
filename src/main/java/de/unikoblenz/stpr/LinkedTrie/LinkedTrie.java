package de.unikoblenz.stpr.LinkedTrie;

import de.unikoblenz.stpr.interfaces.trie.TrieInterface;
import java.util.ArrayList;
import java.util.TreeMap;


public class LinkedTrie implements TrieInterface {
	// Variables
	LinkedTrieNode root;

	// Constructor
	public LinkedTrie() {
		root = new LinkedTrieNode('.');
	}

	// Methods
	public void add(String s){
		LinkedTrieNode last = root;
		for (int i = 0; i < s.length(); i++ ){
			last = last.addGetChild(s.charAt(i));
		}
	}

	public String toString(){
		return root.toString();
	}
}
