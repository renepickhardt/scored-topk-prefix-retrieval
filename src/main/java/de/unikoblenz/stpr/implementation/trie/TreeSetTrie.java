/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unikoblenz.stpr.implementation.trie;

import de.unikoblenz.stpr.interfaces.trie.TrieInterface;
import java.util.TreeSet;

/**
 *
 * @author hartmann
 */
public class TreeSetTrie implements TrieInterface {
    public TreeSet<String> set;
    
    public void add(String s) {
        set.add(s);
    }

    public TreeSetTrie() {
        this.set = new TreeSet<String>();
    }
    
    
}
