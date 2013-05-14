/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unikoblenz.stpr.ScoredArrayTrie;

/**
 * 
 * @author hartmann, rpickhardt
 */
public class SearchResult implements Comparable<SearchResult> {

	public SearchResult(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public SearchResult(InternalSearchResult curCandidate) {
		this.name = curCandidate.getName();
		this.score = curCandidate.score;
	}

	public String name;
	public int score;

	public int compareTo(SearchResult o) {
		return this.score - o.score;
	}

}
