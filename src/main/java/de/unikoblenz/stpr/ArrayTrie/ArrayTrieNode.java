package de.unikoblenz.stpr.ArrayTrie;

import de.unikoblenz.stpr.interfaces.trie.TrieNodeInterface;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class ArrayTrieNode implements TrieNodeInterface {
	private byte value; // signed byte in [-128 .. 127]
	private ArrayTrieNode[] children;

	// ASCII range covered by children
	public static final int MAX_CHAR = 256;
	public static final int MIN_CHAR = 0;

	public ArrayTrieNode(char c) {
		this.setChar(c);
		this.children = new ArrayTrieNode[this.MAX_CHAR - this.MIN_CHAR];
	}

	public void setChar(char c){
		this.value = (byte)c;
	}

	public char getChar() {
		return (char) this.value;
	}

	public ArrayTrieNode getChild(char c) {
		return children[(byte)c + 128 - MIN_CHAR];
	}

	public void setChild(char c, ArrayTrieNode n) {
		children[(byte)c + 128 - MIN_CHAR] = n;
	}

	/**
	 * Returns child node corresponding to character if it exists if not, it
	 * generates a new node
	 * 
	 * @param character
	 * @return child
	 */
	public ArrayTrieNode addGetChild(char c) {
		ArrayTrieNode n = this.getChild(c);
		if (n == null) {
			n = new ArrayTrieNode(c);
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
		for (ArrayTrieNode n : this.children) {
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
