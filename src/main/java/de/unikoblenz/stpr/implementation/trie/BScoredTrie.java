package de.unikoblenz.stpr.implementation.trie;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class BScoredTrie {
	// Variables
	BTrieScoredNode root;

	// Constructor
	public BScoredTrie() {
		root = new BTrieScoredNode('.',0);
	}

	// Methods
	public void add(String s, int score){
		BTrieScoredNode last = root;
		for (int i = 0; i < s.length(); i++ ){
			last = last.addGetChild(s.charAt(i));
		}
		last.setScore(score);
	}

	public String toString(){
		return root.toString();
	}

}
