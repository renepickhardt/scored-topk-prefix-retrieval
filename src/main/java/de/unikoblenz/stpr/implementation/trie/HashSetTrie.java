/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unikoblenz.stpr.implementation.trie;

import de.unikoblenz.stpr.interfaces.trie.TrieInterface;
import java.util.HashSet;

/**
 *
 * @author hartmann
 */
public class HashSetTrie implements TrieInterface {
    public HashSet<String> set;
    
    public void add(String s) {
        set.add(s);
    }

    public HashSetTrie() {
        this.set = new HashSet<String>();
    }
    
    
}
