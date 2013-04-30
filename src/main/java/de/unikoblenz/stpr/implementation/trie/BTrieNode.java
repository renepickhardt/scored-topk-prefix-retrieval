package de.unikoblenz.stpr.implementation.trie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class BTrieNode implements BTrieNodeInterface {
	private byte value;
	private BTrieNode[] children;

	public final int MAX_CHAR = 256;
	public final int MIN_CHAR = 0;

	public BTrieNode(char c) {
		setChar(c);
		this.children = new BTrieNode[MAX_CHAR - MIN_CHAR];
	}

	public void setChar(char c){
		this.value = (byte) ((short)c & 0xFF);
	}
	
	public char getChar() {
		return (char)value;
	}

	public BTrieNode getChild(char c) {
		return children[(short) c & 0xFF - MIN_CHAR];
	}

	public void setChild(char c, BTrieNode n) {
		children[(short) c & 0xFF - MIN_CHAR] = n;
	}

	public List<Pair<Character, BTrieNode>> getChildren() {
		LinkedList<Pair<Character, BTrieNode>> out = new LinkedList<Pair<Character, BTrieNode>>();
		for (int i = MIN_CHAR; i < MAX_CHAR; i++) {
			if (children[i] == null) {
				continue;
			}
			out.add(Pair.of((char) (i + MIN_CHAR), children[i]));
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
		BTrieNode n = getChild(c);
		if (n == null) {
			n = new BTrieNode(c);
			setChild(c, n);
		}
		return n;
	}

	public String toString() {
		String out = "";
		for (String line : recString()) {
			out += line + "\n";
		}
		return out;
	}

	private ArrayList<String> recString() {
		ArrayList<String> lines = new ArrayList<String>();
		Boolean fisrtLine = true;
		for (BTrieNode n : children) {
			if (n == null) continue;
			for (String subline : n.recString()) {
				if (fisrtLine) {
					lines.add(getChar() + subline);
				} else {
					lines.add(" " + subline);
				}
				fisrtLine = false;
			}
		}
		if (fisrtLine) {
			lines.add("" + getChar());
		}
		return lines;
	}

}
