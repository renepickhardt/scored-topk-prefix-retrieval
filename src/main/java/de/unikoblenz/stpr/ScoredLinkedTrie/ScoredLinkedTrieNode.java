package de.unikoblenz.stpr.ScoredLinkedTrie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class ScoredLinkedTrieNode {
	// Trie Variables
	private byte value; // signed byte in [-128 .. 127]
	private LinkedList<ScoredLinkedTrieNode> children;

	// Score Variables
	private int score = 0;

	public static final int TOP_K = 2;
	public ScoredLinkedTrieNode[] topChilds;
	public int[] topScores;

	public ScoredLinkedTrieNode(char c, int s) {
		this.setChar(c);
		this.setScore(s);
	}

	public void setScore(int s) {
		this.score = s;
	}

	public int getScore() {
		return this.score;
	}

	public void setChar(char c) {
		this.value = (byte) c;
	}

	public char getChar() {
		return (char) this.value;
	}

	public boolean isLeaf() {
		return this.children == null || this.children.size() == 0;
	}

	public ScoredLinkedTrieNode getChild(char c) {
		if (this.children == null) {
			return null;
		}
		for (ScoredLinkedTrieNode n : this.children) {
			if (n.getChar() == c) {
				return n;
			}
		}
		return null;
	}

	public void setChild(ScoredLinkedTrieNode n) {
		if (this.children == null) {
			this.children = new LinkedList<ScoredLinkedTrieNode>();
		}
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
		if (this.children != null) {
			for (ScoredLinkedTrieNode n : this.children) {
				if (n == null) {
					continue;
				}
				for (String subline : n.recString()) {
					if (fisrtLine) {
						lines.add(this.getChar() + "{" + this.getScore() + "}["
								+ this.printTopChildScores() + "]\t" + subline);
					} else {
						lines.add(this.getChar() + "{" + this.getScore() + "}["
								+ this.printTopChildScores() + "]\t" + subline);
					}
					fisrtLine = false;
				}
			}
		}
		if (fisrtLine) {
			lines.add("" + this.getChar() + "{" + this.getScore() + "}["
					+ this.printTopChildScores() + "]");
		}
		return lines;
	}

	public Queue<TopScoreEntry> getSetTopChildScores() {
		PriorityQueue<TopScoreEntry> top = new PriorityQueue<TopScoreEntry>();

		// Add this node to topQueue
		top.add(new TopScoreEntry(this, this.score));

		if (!this.isLeaf()) {
			// Descend
			for (ScoredLinkedTrieNode child : this.children) {
				for (TopScoreEntry childTopEntry : child.getSetTopChildScores()) {
					top.add(new TopScoreEntry(child, childTopEntry.topScore));
				}
			}
		}

		// TruncateQueue and SetVariables
		Queue<TopScoreEntry> ret = new LinkedList<TopScoreEntry>();
		int LIM = Math.min(top.size(), TOP_K);
		this.topChilds = new ScoredLinkedTrieNode[LIM];
		this.topScores = new int[LIM];
		for (int i = 0; i < LIM; i++) {
			TopScoreEntry e = top.poll();
			this.topChilds[i] = e.n;
			this.topScores[i] = e.topScore;
			ret.add(e);
		}
		return ret;
	}

	public String printTopChildScores() {
		if (this.topScores == null) {
			return "";
		}
		String out = "";
		for (int s : this.topScores) {
			// out += e.n.getChar() + ":" + e.topScore + " ";
			out += s + " ";
		}
		return out.trim();
	}

}
