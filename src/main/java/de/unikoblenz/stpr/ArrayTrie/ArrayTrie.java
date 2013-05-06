package de.unikoblenz.stpr.ArrayTrie;

import de.unikoblenz.stpr.interfaces.trie.TrieInterface;
import de.unikoblenz.stpr.ArrayTrie.ArrayTrieNode;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class ArrayTrie implements TrieInterface {
	// Variables
        List<Integer> a;
        ArrayTrieNode root;

	// Constructor
	public ArrayTrie() {
		root = new ArrayTrieNode('.');
                a = new ArrayList<Integer>();
	}

	// Methods
	public void add(String s){
		ArrayTrieNode last = root;
		for (int i = 0; i < s.length(); i++ ){
			last = last.addGetChild(s.charAt(i));
		}
	}

	public String toString(){
		return root.toString();
	}
}
