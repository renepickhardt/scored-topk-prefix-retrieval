package de.unikoblenz.stpr.ScoredLinkedTrie;

import de.renepickhardt.utils.IOHelper;
import de.unikoblenz.stpr.ScoredLinkedTrie.*;
import de.unikoblenz.stpr.LinkedTrie.*;
import de.unikoblenz.stpr.interfaces.trie.TrieInterface;
import java.util.ArrayList;
import java.util.LinkedList;
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
        
        public void insertScored(String s, Integer score) throws Exception {
            LinkedList<ScoredLinkedTrieNode> path = new LinkedList<ScoredLinkedTrieNode>();
            path.push(root);
            for (int i = 0; i < s.length(); i++){
                // Iterate through internalNodes
                path.push(path.peek().addGetChild(s.charAt(i),0));
            }
                        
            // Update EndNode
            ScoredLinkedTrieNode endNode = path.pop();
            endNode.setScore(score);
            endNode.pushTop(endNode,score);
            
            ScoredLinkedTrieNode childNode = endNode;
            ScoredLinkedTrieNode parentNode = path.pop();
            // Propagate Top Scores
            while(true){
                if (childNode.getMaxTopScore() > score ) { break; }
                parentNode.updateTop(childNode, score);
                
                childNode = parentNode;
                if (path.size() == 0){ break; } 
                parentNode = path.pop();                
            }
            
	}
        
	public String toString(){
		return root.toString();
	}
}
