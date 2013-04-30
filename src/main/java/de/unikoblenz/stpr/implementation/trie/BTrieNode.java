package de.unikoblenz.stpr.implementation.trie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class BTrieNode implements BTrieNodeInterface {
	private byte value; // signed byte in [-128 .. 127]
	private BTrieNode[] children;

	// ASCII range covered by children
	public static final int MAX_CHAR = 256;
	public static final int MIN_CHAR = 0;

	public BTrieNode(char c) {
		this.setChar(c);
		this.children = new BTrieNode[this.MAX_CHAR - this.MIN_CHAR];
	}

	public void setChar(char c){
		this.value = (byte)c;
	}

	public char getChar() {
		return (char) this.value;
	}

	public BTrieNode getChild(char c) {
		return children[(byte)c + 128 - MIN_CHAR];
	}

	public void setChild(char c, BTrieNode n) {
		children[(byte)c + 128 - MIN_CHAR] = n;
	}

	public List<Pair<Character, BTrieNode>> getChildren() {
		LinkedList<Pair<Character, BTrieNode>> out = new LinkedList<Pair<Character, BTrieNode>>();
		for (int i = this.MIN_CHAR; i < this.MAX_CHAR; i++) {
			if (this.children[i] == null) {
				continue;
			}
			out.add(Pair.of((char) (i + this.MIN_CHAR), this.children[i]));
		}
		return out;
	}

	/**
	 * Returns child node corresponding to character if it exists if not, it
	 * generates a new node
	 * 
	 * @param character
	 * @return child
	 */
	public BTrieNode addGetChild(char c) {
		BTrieNode n = this.getChild(c);
		if (n == null) {
			n = new BTrieNode(c);
			this.setChild(c, n);
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
		for (BTrieNode n : this.children) {
			if (n == null) {
				continue;
			}
			for (String subline : n.recString()) {
				if (fisrtLine) {
					lines.add(this.getChar() + subline);
				} else {
					lines.add(" " + subline);
				}
				fisrtLine = false;
			}
		}
		if (fisrtLine) {
			lines.add("" + this.getChar());
		}
		return lines;
	}

}
