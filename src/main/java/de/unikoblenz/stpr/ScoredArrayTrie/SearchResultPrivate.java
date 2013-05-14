/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unikoblenz.stpr.ScoredArrayTrie;

/**
 *
 * @author hartmann
 */
public class SearchResultPrivate {

    public SearchResultPrivate() {
        name = new StringBuilder(80);
        index = 0;
    }
    
    public StringBuilder name;
    public int score;
    public ScoredArrayTrieNode node;
    public int index;
    
    public String getName(){
        return name.toString();
    }
    
    public void addChar(char c){
        name.setCharAt(index, c);
        index++;
    }
}
