package de.unikoblenz.stpr.ScoredLinkedTrie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ScoredLinkedTrieNode {
	// Trie Variables
	private byte value; // signed byte in [-128 .. 127]
	private LinkedList<ScoredLinkedTrieNode> children;

	// Score Variables
	private int score = 0;

	// TopScoreMaintenance
	public static final int TOP_K = 10;

	public ScoredLinkedTrieNode[] topChilds;
	public int[] topScores;

	public ScoredLinkedTrieNode(char c, int s) {
		this.children = new LinkedList<ScoredLinkedTrieNode>();

		this.setChar(c);
		this.setScore(s);

		this.topScores = new int[0];
		this.topChilds = new ScoredLinkedTrieNode[0];
	}

	/*
	 * TRIE METHODS
	 */
	public void setChar(char c) {
		this.value = (byte) c;
	}

	public char getChar() {
		return (char) this.value;
	}

	public boolean isLeaf() {
		return this.children == null || this.children.size() == 0;
	}

	public List<ScoredLinkedTrieNode> getChildren() {
		return this.children;
	}

	public ScoredLinkedTrieNode getChild(char c) {
		for (ScoredLinkedTrieNode n : this.getChildren()) {
			if (n.getChar() == c) {
				return n;
			}
		}
		return null;
	}

	public void setChild(ScoredLinkedTrieNode n) {
		this.children.addFirst(n);
	}

	/**
	 * Returns child node corresponding to character if it exists if not, it
	 * generates a new node
	 * 
	 * @param character
	 * @return child
	 */
	public ScoredLinkedTrieNode addGetChild(char c, int s) {
		ScoredLinkedTrieNode n = this.getChild(c);
		if (n == null) {
			n = new ScoredLinkedTrieNode(c, s);
			this.setChild(n);
		}
		return n;
	}

	/*
	 * SCORE HANDLING
	 */
	public int getScore() {
		return this.score;
	}

	public void setScore(int s) {
		this.score = s;
	}

	public int[] getTopScores() {
		return this.topScores;
	}

	public ScoredLinkedTrieNode[] getTopChilds() {
		return this.topChilds;
	}

	public void setTopChildScore(int[] s) {
		this.topScores = s;
	}

	public void setTopChildNodes(ScoredLinkedTrieNode[] n) {
		this.topChilds = n;
	}

	private void enlargeTopChilds() {
		int LEN = this.topChilds.length;
		ScoredLinkedTrieNode[] newTopChilds = new ScoredLinkedTrieNode[LEN + 1];
		System.arraycopy(this.topChilds, 0, newTopChilds, 0, LEN);
		this.topChilds = newTopChilds;
	}

	private void enlargeTopScores() {
		int LEN = this.topScores.length;
		int[] newTopScores = new int[LEN + 1];
		System.arraycopy(this.topScores, 0, newTopScores, 0, LEN);
		this.topScores = newTopScores;
	}

	public void pushTop(ScoredLinkedTrieNode topNode, int topScore) {
		if (topScore == 0) {
			return;
		}
		if (this.topChilds.length < TOP_K) {
			this.enlargeTopChilds();
			this.enlargeTopScores();
		}
		int tempScore = 0;
		ScoredLinkedTrieNode tempNode = null;
		int tempScore2 = 0;
		ScoredLinkedTrieNode tempNode2 = null;
		int STATUS = 0;
		for (int i = 0; i < this.topChilds.length; i++) {
			// UPDATE VALUE OF topScores[i], topChilds[i]
			if (STATUS == 0 && topScore >= this.topScores[i]) {
				STATUS = 1;

				tempScore = this.topScores[i];
				tempNode = this.topChilds[i];

				this.topScores[i] = topScore;
				this.topChilds[i] = topNode;
			} else if (STATUS == 1) {
				// SHIFT ENTRIES; ASSUMES tempScore, tempNode is Set
				tempScore2 = this.topScores[i];
				tempNode2 = this.topChilds[i];

				this.topScores[i] = tempScore;
				this.topChilds[i] = tempNode;

				tempScore = tempScore2;
				tempNode = tempNode2;
			}
		}
	}

	public void updateTop(ScoredLinkedTrieNode childNode, int score)
			throws Exception {
		for (int i = 0; i < this.topChilds.length; i++) {
			if (this.topChilds[i] == childNode) {
				this.topScores[i] = score;
				return;
			}
		}
		// notFound -> push
		this.pushTop(childNode, score);
	}

	public int getMinTopScore() {
		for (int i = this.topScores.length - 1; i >= 0; i--) {
			if (this.topScores[i] > 0) {
				return this.topScores[i];
			}
		}
		return 0;
	}

	public int getMaxTopScore() {
		return this.topScores[0];
	}

	/*
	 * SERIALIZATION
	 */
	@Override
	public String toString() {
		String out = "";
		for (String line : this.recString()) {
			out += line + "\n";
		}
		return out;
	}

	private ArrayList<String> recString() {
		ArrayList<String> lines = new ArrayList<String>();
		Boolean fisrtLine = true;
		for (ScoredLinkedTrieNode n : this.getChildren()) {
			if (n == null) {
				continue;
			}
			for (String subline : n.recString()) {
				if (fisrtLine) {
					lines.add(this.getChar() + "{" + this.getScore() + "}["
							+ this.printTopChildScores() + "]\t" + subline);
				} else {
					lines.add("\t" + subline);
				}
				fisrtLine = false;
			}
		}
		if (fisrtLine) {
			lines.add("" + this.getChar() + "{" + this.getScore() + "}["
					+ this.printTopChildScores() + "]");
		}
		return lines;
	}

	public String printTopChildScores() {
		if (this.topScores == null) {
			return "";
		}
		String out = "";
		for (int s : this.topScores) {
			out += s + " ";
		}
		return out.trim();
	}

}
