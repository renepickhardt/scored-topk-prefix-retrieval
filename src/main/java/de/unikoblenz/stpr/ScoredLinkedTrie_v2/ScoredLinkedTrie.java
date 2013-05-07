package de.unikoblenz.stpr.ScoredLinkedTrie_v2;

import de.unikoblenz.stpr.LinkedTrie.*;
import de.unikoblenz.stpr.interfaces.trie.TrieInterface;
import java.util.ArrayList;
import java.util.TreeMap;

public class ScoredLinkedTrie implements TrieInterface {
	// Variables
	public ScoredLinkedTrieNode root;

	// Constructor
	public ScoredLinkedTrie() {
		root = new ScoredLinkedTrieNode('.',0);
	}

	// Methods
	public void add(String s, Integer score){
		ScoredLinkedTrieNode last = root;
		for (int i = 0; i < s.length(); i++ ){
			last = last.addGetChild(s.charAt(i),  i == s.length()-1 ? score : 0 );
		}
	}

        public ScoredLinkedTrieNode get(String s){
		ScoredLinkedTrieNode last = root;
		for (int i = 0; i < s.length(); i++ ){
                    last = last.getChild(s.charAt(i));
		}
                return last;
	}
        
        public void add(String s){
            add(s,0);
        }
        
        public void insertScored(String s, Integer score) {
            ScoredLinkedTrieNode last = root;
            ScoredLinkedTrieNode cur;
            for (int i = 0; i < s.length() - 1; i++ ){
                // Iterate through internalNodes
                cur = last.getInsertChild(s.charAt(i),0);
                last.updateTop(cur,cur.getTopScore());
                last = cur;
            }
            last.getInsertChild(s.charAt(s.length()-1),score);
	}
        
	public String toString(){
		return root.toString();
	}
}
