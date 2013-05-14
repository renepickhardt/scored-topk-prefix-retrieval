/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unikoblenz.stpr.ScoredArrayTrie;

/**
 * 
 * @author hartmann, rpickhardt
 */
public class InternalSearchResult implements Comparable<InternalSearchResult> {

	public InternalSearchResult() {
		this.name = new StringBuilder(16);
		this.index = 0;
	}

	public InternalSearchResult(ScoredArrayTrieNode potentialCandidate,
			InternalSearchResult curCandidate) {
		this.name = new StringBuilder(curCandidate.name.toString()
				+ potentialCandidate.getChar());
		this.index = curCandidate.index + 1;
		this.score = potentialCandidate.maxScore;
		this.node = potentialCandidate;
	}

	public StringBuilder name;
	public int score;
	public ScoredArrayTrieNode node;
	public int index;

	public String getName() {
		return this.name.toString();
	}

	public void addChar(char c) {
		if (this.index >= this.name.capacity()) {
			this.name.setLength(this.index * 2);
		}
		this.name.setCharAt(this.index, c);
		this.index++;
	}

	public int compareTo(InternalSearchResult o) {
		return this.score - o.score;
	}
}
