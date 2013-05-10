package de.unikoblenz.stpr.ScoredLinkedTrie;

import java.util.ArrayList;
import java.util.LinkedList;

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

	public void insertScored(String s, Integer score) throws Exception {
		LinkedList<ScoredLinkedTrieNode> path = new LinkedList<ScoredLinkedTrieNode>();
		path.push(this.root);
		for (int i = 0; i < s.length(); i++) {
			// Iterate through internalNodes
			path.push(path.peek().addGetChild(s.charAt(i), 0));
		}

		// Update EndNode
		ScoredLinkedTrieNode endNode = path.pop();
		endNode.setScore(score);
		// TODO: why endnode.pushTop(endNode) within function this and argument
		// seem to be treated differently
		endNode.pushTop(endNode, score);

		ScoredLinkedTrieNode childNode = endNode;
		ScoredLinkedTrieNode parentNode = path.pop();
		// Propagate Top Scores
		while (true) {
			if (childNode.getMaxTopScore() > score) {
				break;
			}
			parentNode.updateTop(childNode, score);

			childNode = parentNode;
			if (path.size() == 0) {
				break;
			}
			parentNode = path.pop();
		}
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
		ScoredLinkedTrieNode startNode = null;
		if (prefix == "") {
			startNode = this.root;
		} else {
			startNode = this.get(prefix);
		}
		if (startNode == null) {
			return null;
		}

		IntervalHeap<TopScoreEntry> candidateSet = new IntervalHeap<TopScoreEntry>();
		ArrayList<TopScoreEntry> resultSet = new ArrayList<TopScoreEntry>();

		// TODO: it should be getTopScore (which is not implemented yet)
		TopScoreEntry tmp = new TopScoreEntry(startNode,
				startNode.getMaxTopScore());
		tmp.myName = prefix;

		candidateSet.add(tmp);
		System.out.println("added : " + tmp.myName
				+ " as a potential candidate \t score: " + tmp.topScore);

		// TODO: maintain paths in the data structure or in the TopScoreEntries
		int maxQueueLength = k;
		while (candidateSet.size() > 0 && resultSet.size() < k) {
			TopScoreEntry curEntry = candidateSet.dequeueMax();
			System.out.println("removing and processing : " + curEntry.myName
					+ " as a potential candidate \t score: "
					+ curEntry.topScore);

			ScoredLinkedTrieNode curNode = curEntry.n;

			if (curEntry.topScore == curNode.getScore()) {
				maxQueueLength--;
				resultSet.add(curEntry);
				// IOHelper.log("found result:\t" + curEntry.myName + "\t"
				// + curEntry.topScore);
				if (maxQueueLength == 0) {
					return resultSet;
				}
				continue;
			}

			for (int i = 0; i < Math.min(curNode.topChilds.length,
					curNode.TOP_K); i++) {
				ScoredLinkedTrieNode potentialCandidate = curNode.topChilds[i];
				if (candidateSet.size() < maxQueueLength) {
					this.addToQueue(candidateSet, curEntry,
							curNode.topScores[i], potentialCandidate);
				} else if (candidateSet.min().topScore < curNode.topScores[i]) {
					this.addToQueue(candidateSet, curEntry,
							curNode.topScores[i], potentialCandidate);
					// need to remove min to maintain max size of candidate set
					TopScoreEntry min = candidateSet.dequeueMin();
					System.out.println("removed: " + min.myName
							+ " as a potential candidate \t score: "
							+ min.topScore);
				} else {
					// TODO: check if this is really a valid break (cold also be
					// that at some point I need to maintain this loop)
					// if so: set also a flag and use this for the next TODO
					break;
				}
				// TODO: what happens if queue not full or last topscore was >
				// min ==> have to do a manual search outsie of index! or accept
				// that algorithm only has a high propability to be correct
			}
		}
		return resultSet;
	}

	private void addToQueue(IntervalHeap<TopScoreEntry> candidateSet,
			TopScoreEntry current, int score,
			ScoredLinkedTrieNode potentialCandidate) {
		TopScoreEntry entry = new TopScoreEntry(potentialCandidate, score);
		// TODO: expensive
		entry.myName = current.myName + potentialCandidate.getChar();
		candidateSet.add(entry);
		System.out.println("added: " + entry.myName
				+ " as a potential candidate \t score: " + entry.topScore);
	}

	@Override
	public String toString() {
		return this.root.toString();
	}
}
