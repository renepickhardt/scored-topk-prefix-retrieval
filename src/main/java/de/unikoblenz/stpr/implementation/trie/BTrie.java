package de.unikoblenz.stpr.implementation.trie;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class BTrie implements BTrieInterface {
	// Variables
	BTrieLinkedNode root;

	// Constructor
	public BTrie() {
		root = new BTrieLinkedNode('.');
	}

	// Methods
	public void add(String s){
		BTrieLinkedNode last = root;
		for (int i = 0; i < s.length(); i++ ){
			last = last.addGetChild(s.charAt(i));
		}
	}

	public String toString(){
		return root.toString();
	}
}
