package de.unikoblenz.stpr.ScoredArrayTrie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.renepickhardt.utils.IntervalHeap;

public class ScoredArrayTrie {
	// Variables

	public ScoredArrayTrieNode root;

	private IntervalHeap<InternalSearchResult> candidateSet = new IntervalHeap<InternalSearchResult>();

	// Constructor
	public ScoredArrayTrie() {
		this.root = new ScoredArrayTrieNode('.', 0);
	}

	// Methods
	/**
	 * Adds a string to the Trie. If the string is not present new nodes are
	 * created. If the string is already there, the score is overwritten with
	 * the provided score. The root node is not modified
	 */
	public void add(String s, int score) {
		if (s.length() == 0) {
			return;
		}
		ScoredArrayTrieNode last = this.root;
		for (int i = 0; i < s.length(); i++) {
			last = last.addGetChild(s.charAt(i));
		}
		last.setScore(score);
		// last.updateMaxScore();
	}

	// Default score = 1
	public void add(String s) {
		this.add(s, 1);
	}

	/**
	 * Looks up the String s in the trie and returns the list of nodes traversed
	 * on the way. All lists start with the root node (".") E.g. s = "" a with
	 * the root node is returned. If the string is not in the trie null is
	 * returned.
	 * 
	 * @param s
	 * @return path
	 */
	public List<ScoredArrayTrieNode> find(String s) {
		LinkedList<ScoredArrayTrieNode> path = new LinkedList<ScoredArrayTrieNode>();
		path.add(this.root);
		for (int i = 0; i < s.length(); i++) {
			ScoredArrayTrieNode child = path.getLast().getChild(s.charAt(i));
			if (child == null) {
				return null;
			}
			path.add(child);
		}
		return path;
	}

	@Override
	public String toString() {
		return this.root.toString();
	}

	/**
	 * Finds top-k words stored in the trie that match prefix. Assumes: trie is
	 * properly setup with topChilds and maxScores Returns: Sorted list of
	 * resuts. Each result contains a string (= retireved word) and the
	 * corresponding score in an SearchResult object.
	 * 
	 * If no results are present null is returned.
	 * 
	 * @param prefix
	 * @param k
	 * @return resultList
	 */
	public List<SearchResult> getTopK(String prefix, final int k) {

		if (prefix == null) {
			return null;
		}
		ScoredArrayTrieNode startNode = null;
		if (prefix == "") {
			startNode = this.root;
		} else {
			List<ScoredArrayTrieNode> path = this.find(prefix);
			if (path == null) {
				return null;
			}
			startNode = path.get(path.size() - 1);
		}

		//
		while (this.candidateSet.size() > 0) {
			this.candidateSet.dequeueMax();
		}
		ArrayList<SearchResult> resultSet = new ArrayList<SearchResult>();

		// add first candidate to candidate set
		InternalSearchResult tmp = new InternalSearchResult();
		tmp.name = new StringBuilder(prefix);
		tmp.node = startNode;
		tmp.score = startNode.maxScore;
		tmp.index = prefix.length();
		this.candidateSet.add(tmp);
		// TODO: enable debugging message

		// start the main algorithm loop.
		int maxQueueLength = k;
		while (this.candidateSet.size() > 0 && resultSet.size() < k) {
			InternalSearchResult curCandidate = this.candidateSet.dequeueMax();
			// IOHelper.log("removing and processing : " + curCandidate.name
			// + " as a potential candidate \t score: "
			// + curCandidate.score);

			ScoredArrayTrieNode curNode = curCandidate.node;

			// Check if the current node should be added to the result set
			if (curCandidate.score <= curNode.score && curCandidate.score > 0) {
				maxQueueLength--;
				resultSet.add(new SearchResult(curCandidate));
				// IOHelper.log("found result:\t" + curCandidate.name + "\t"
				// + curCandidate.score);
				if (maxQueueLength == 0) {
					return resultSet;
				}
				// TODO: Not clear what to do!. maybe Push curCandidate in other
				// loop to queue.
				// continue;
			}

			// add children to candidate set:

			boolean usedCompleteIndex = true;
			for (ScoredArrayTrieNode potentialCandidate : curNode.topChilds) {
				// System.out.println("!!!");
				if (this.candidateSet.size() < maxQueueLength) {
					InternalSearchResult nextCandidate = new InternalSearchResult(
							potentialCandidate, curCandidate);
					// add to queue
					this.candidateSet.add(nextCandidate);
					// IOHelper.log("potential candidate added: "
					// + nextCandidate.name + "\t score: "
					// + nextCandidate.score);
				} else if (this.candidateSet.min().score < potentialCandidate.maxScore) {
					InternalSearchResult nextCandidate = new InternalSearchResult(
							potentialCandidate, curCandidate);
					// IOHelper.log("potential candidate added: "
					// + nextCandidate.name + "\t score: "
					// + nextCandidate.score);
					this.candidateSet.add(nextCandidate);

					InternalSearchResult min = this.candidateSet.dequeueMin();
					// IOHelper.log("potential candidate removed: " + min.name
					// + "\t score: " + min.score);
					// TODO: could also here compare to candidateSet.min() and
					// change usedCompleteIndex Flag. If candidateSet.min() ==
					// potentialCandidate then usedCompleteIndex can be equals
					// false
				} else {
					usedCompleteIndex = false;
					break;
				}
			}
			if (usedCompleteIndex) {
				// TODO: what happens if queue not full or last topscore was >
				// min ==> have to do a manual search outsie of index! or accept
				// that algorithm only has a high propability to be correct

				// count global cound see how often this happens
				// or use one of the resolving strategies.
			}
		}
		return resultSet;
	}
}
