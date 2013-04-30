package de.unikoblenz.stpr.implementation.trie;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class BLinkedTrie implements BTrieInterface {
	// Variables
	TLNode root;

	// Constructor
	public BLinkedTrie() {
		root = new TLNode('.');
	}

	// Methods
	public void add(String s){
		TLNode last = root;
		for (int i = 0; i < s.length(); i++ ){
			last = last.addGetChild(s.charAt(i));
		}
	}

	public String toString(){
		return root.toString();
	}
}
