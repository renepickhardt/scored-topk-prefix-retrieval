package de.unikoblenz.stpr.ScoredLinkedTrie;

public class TopScoreEntry implements Comparable<TopScoreEntry> {
	public ScoredLinkedTrieNode n;
	public int topScore;
	public String myName;

	public TopScoreEntry(ScoredLinkedTrieNode n, int s) {
		this.topScore = s;
		this.n = n;
	}

	public int compareTo(TopScoreEntry o) {
		return this.topScore - o.topScore;
	}
}