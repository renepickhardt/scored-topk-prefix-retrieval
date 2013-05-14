package de.unikoblenz.stpr.ScoredArrayTrie;

public class TopScoreEntry implements Comparable<TopScoreEntry> {
	public ScoredArrayTrieNode node;
	public int score;
	public String myName;

	public TopScoreEntry(ScoredArrayTrieNode node, int score) {
		this.node = node;
		this.score = score;
	}

	public int compareTo(TopScoreEntry o) {
		return - this.score + o.score;
	}
}