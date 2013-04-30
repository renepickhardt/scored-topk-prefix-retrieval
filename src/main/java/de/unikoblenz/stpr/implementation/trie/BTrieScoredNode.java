package de.unikoblenz.stpr.implementation.trie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.lang3.tuple.Pair;

public class BTrieScoredNode {
	private char value;
	private BTrieScoredNode[] children;
	
	public final int MAX_CHAR = 122;
	public final int MIN_CHAR = 48;
	
	private int score;
	
	public BTrieScoredNode(char value, int score) {
		this.value = value;
		this.score = score;
		this.children = new BTrieScoredNode[MAX_CHAR - MIN_CHAR];
	}

	public char getChar(){
		return value;
	}

	public int getScore(){
		return score;
	}

	public void setScore(int score){
		this.score = score;
	}
	
	public BTrieScoredNode getChild(char c) {
		return children[(int) c - MIN_CHAR];
	}

	public void setChild(char c, BTrieScoredNode n) {
		children[(int) c - MIN_CHAR] = n;
	}

	public BTrieScoredNode addChild(char c, int score) {
		BTrieScoredNode n = new BTrieScoredNode(c, score);
		setChild(c, n);
		return n;
	}
	
	/**
	 * Returns child node corresponding to character if it exists if not, it
	 * generates a new node
	 * 
	 * @param character
	 * @return child
	 */
	public BTrieScoredNode addGetChild(char c) {
		BTrieScoredNode n = getChild(c);
		if (n == null) {
			n = new BTrieScoredNode(c,0);
			setChild(c, n);
		}
		return n;
	}
	
	public int maxChildScore(){
		int max = score;
		for (BTrieScoredNode n: children){
			if (n == null) {continue;}
			max = Math.max(max, n.maxChildScore());
		}
		return max;
	}
		
	private BTrieScoredNode[] topNodes;
	public void setTopNodes(){
		PriorityQueue<Pair<Integer,BTrieScoredNode>> P = new PriorityQueue<Pair<Integer,BTrieScoredNode>>();
		for (BTrieScoredNode n : children){
			if (n == null) {continue;}
			n.setTopNodes();
			P.add(Pair.of(n.getTopScore(), n));
		}
		
		int K_TOP = Math.max(5, P.size());
		topNodes = new BTrieScoredNode[K_TOP];
		for (int i = 0; i < K_TOP; i++){
			topNodes[i] = P.poll().getRight();
		}
	}
	
	public int getTopScore(){
		if (topNodes.length > 0) {
			return topNodes[0].getScore	();
		} else {
			return -1;
		}
	}
	
	public String toString() {
		String out = "";
		for (String line : 	recString()){
			out += line + "\n";
		}
		return out;
	}

	public ArrayList<String> recString() {
		ArrayList<String> lines = new ArrayList<String>();
		Boolean fisrtLine = true;
		for (BTrieScoredNode n : children) {
			if (n == null) {
				continue;
			}
			for (String subline : n.recString()) {
				if (fisrtLine) {
					lines.add(value + "[" + maxChildScore() + "]" + subline);
				} else {
					lines.add("    " + subline);
				}
				fisrtLine = false;
			}
		}
		if (fisrtLine) {
			lines.add("" + value + "{"+ score + "}");
		}
		return lines;
	}

	public List<Pair<Character, BTrieScoredNode>> getChildren(){
		LinkedList<Pair<Character, BTrieScoredNode>> out = new LinkedList<Pair<Character, BTrieScoredNode>>();
		for (int i = MIN_CHAR; i < MAX_CHAR; i++) {
			if (children[i] == null) { continue; }
			out.add(Pair.of((char) (i + MIN_CHAR), children[i]));
		}
		return out;
	}
}
