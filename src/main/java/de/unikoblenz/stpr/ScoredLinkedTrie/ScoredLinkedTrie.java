package de.unikoblenz.stpr.ScoredLinkedTrie;

import java.util.ArrayList;

import de.renepickhardt.utils.IntervalHeap;
import de.unikoblenz.stpr.interfaces.trie.TrieInterface;

public class ScoredLinkedTrie implements TrieInterface {
	// Variables
	public ScoredLinkedTrieNode root;

	// Constructor
	public ScoredLinkedTrie() {
		this.root = new ScoredLinkedTrieNode('.', 0);
	}

	// Methods
	public void add(String s, Integer score) {
		ScoredLinkedTrieNode last = this.root;
		for (int i = 0; i < s.length(); i++) {
			last = last.addGetChild(s.charAt(i), i == s.length() - 1 ? score
					: 0);
		}
	}

	/**
	 * retrieves the ScoredLinkedTrieNode that matches the String s
	 * 
	 * @param s
	 * @return null if there is no path in the trie matching the String s
	 */
	public ScoredLinkedTrieNode get(String s) {
		ScoredLinkedTrieNode last = this.root;
		for (int i = 0; i < s.length(); i++) {
			last = last.getChild(s.charAt(i));
			if (last == null) {
				return null;
			}
		}
		return last;
	}

	public void add(String s) {
		this.add(s, 0);
	}

	/**
	 * returns the top k Elements in a try matching a prefix. The prefix can be
	 * empty but not null
	 * 
	 * @param prefix
	 * @return returns null if the prefix cannot be matched.
	 */
	public ArrayList<TopScoreEntry> getTopKList(String prefix, int k) {
		if (prefix == null) {
			return null;
		}
		ScoredLinkedTrieNode startNode = this.get(prefix);
		if (startNode == null) {
			return null;
		}

		IntervalHeap<TopScoreEntry> candidateSet = new IntervalHeap<TopScoreEntry>();
		ArrayList<TopScoreEntry> resultSet = new ArrayList<TopScoreEntry>();

		// TODO: it should be getTopScore (which is not implemented yet)
		TopScoreEntry tmp = new TopScoreEntry(startNode, startNode.getScore());

		// TODO: maintain paths in the data structure or in the TopScoreEntries
		int maxQueueLength = k;
		while (candidateSet.size() > 0 && resultSet.size() < k) {
			TopScoreEntry current = candidateSet.dequeueMax();

			ScoredLinkedTrieNode curNode = current.n;

			if (current.topScore == curNode.getScore()) {
				maxQueueLength--;
				resultSet.add(current);
			}

			// candidateSet.min().topScore
			for (int i = 0; i < curNode.TOP_K; i++) {
				ScoredLinkedTrieNode potentialCandidate = curNode.topChilds[i];
				if (candidateSet.size() < maxQueueLength) {
					candidateSet.add(new TopScoreEntry(potentialCandidate,
							curNode.topScores[i]));
				} else if (candidateSet.min().topScore < curNode.topScores[i]) {
					candidateSet.add(new TopScoreEntry(potentialCandidate,
							curNode.topScores[i]));
					// need to remove min to maintain max size of candidate set
					candidateSet.dequeueMin();
				} else {
					// TODO: check if this is really a valid break
					// if so: set also a flag and use this for the next TODO
					break;
				}
				// TODO: what happens if queue not full or last topscore was >
				// min ==> have to do a manual search outsie of index! or accept
				// that algorithm only has a high propability to be correct

			}

		}

		// for (int i = 0; i < startNode.TOP_K; i++) {
		// ScoredLinkedTrieNode tmp = startNode.topChilds[i];
		// TopScoreEntry tse = new TopScoreEntry(tmp, startNode.topScores[i]);
		// topKSet.add(tse);
		// tse = new TopScoreEntry(startNode.topChilds[i], -1
		// * startNode.topScores[i]);
		// reversedTopKSet.add(tse);
		// }

		// i found a real word.
		if (startNode.getScore() != 0) {

		}

		return null;
	}

	@Override
	public String toString() {
		return this.root.toString();
	}
}
