package de.unikoblenz.stpr.ScoredArrayTrie;

public class TopScoreEntry implements Comparable<TopScoreEntry> {
	public ScoredArrayTrieNode node;
	public int topTreeScore;
	public String myName;

	public TopScoreEntry(ScoredArrayTrieNode node, int score) {
		this.node = node;
		this.topTreeScore = score;
	}

	public int compareTo(TopScoreEntry o) {
		return - this.topTreeScore + o.topTreeScore;
	}
}