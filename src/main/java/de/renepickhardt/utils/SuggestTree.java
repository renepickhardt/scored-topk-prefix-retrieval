package de.renepickhardt.utils;

/*
 * Copyright 2011-2013 Nicolai Diethelm
 *
 * This software is free software. You can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import java.util.Arrays;
import java.util.ConcurrentModificationException;

/**
 * A data structure for rank-sensitive autocomplete. It provides O(log <i>n</i>)
 * time for the basic operations such as searching for the top <i>k</i> highest
 * weighted autocomplete suggestions for a given prefix, modifying the weight of
 * a suggestion, inserting a new suggestion, or removing a suggestion. The
 * structure is based on a compressed ternary search tree of the suggestions,
 * where nodes (prefixes) with the same completions are merged into one node,
 * and where each node that corresponds to a suggestion stores the weight of the
 * suggestion and a reference to the suggestion string. In addition, each node
 * in the tree holds a rank-ordered array of references to the nodes of the top
 * <i>k</i> suggestions that start with the corresponding prefix.
 * <p>
 * The space consumption of the tree is determined by the number of nodes in the
 * tree and the average length of the array held by each node (the length of the
 * character sequence of a node does not affect space consumption because only
 * the first character is stored explicitly; the other characters are read from
 * the corresponding suggestion or a suggestion that is referenced instead). For
 * each suggestion inserted into the tree, at most one new node is added and at
 * most one existing node is split into two nodes. A tree with <i>n</i>
 * suggestions has thus less than 2<i>n</i> nodes. In the worst case, when the
 * tree has 2<i>n</i> - 1 nodes, each internal node of the corresponding trie
 * (prefix tree) has exactly two child nodes. If all leaf nodes of the trie are
 * at the same depth, i.e. if the height of the trie is log<sub>2</sub><i>n</i>,
 * the tree has <i>n</i> nodes with an array of length 1, <i>n</i>/2 nodes with
 * an array of length 2, <i>n</i>/4 nodes with an array of length 4, and so on
 * until the maximum length of <i>k</i> is reached. Assuming <i>k</i> is a power
 * of two, this gives a total array length of <i>n</i> + 2(<i>n</i>/2) +
 * 4(<i>n</i>/4) + ... + <i>k</i>(<i>n</i>/<i>k</i>) +
 * <i>k</i>(<i>n</i>/<i>k</i> - 1), which is approximately
 * <i>n</i>(log<sub>2</sub><i>k</i> + 2).
 * <p>
 * Ternary search trees are robust. Even in the worst case, when the suggestions
 * are inserted into the tree in lexicographic order, performance is usually
 * only slightly degraded. The reason for this is that not the entire tree
 * degenerates into a linked list, only each of the small binary search trees
 * within the ternary search tree does. However, for best performance, the
 * suggestions should be inserted or removed in a random order. This usually
 * produces a balanced tree where the search space is cut more or less in half
 * each time the search goes left or right.
 * <p>
 * This implementation is not synchronized. If multiple threads access a tree
 * concurrently, and at least one of the threads modifies the tree, it must be
 * synchronized externally. This is typically accomplished by synchronizing on
 * some object that naturally encapsulates the tree.
 * 
 * @version 27 April 2013
 */
public class SuggestTree {

	private final int k;
	private Node root;
	private int size;
	private boolean replaceWithSuccessor;

	/**
	 * Creates a tree that returns the top {@code k} highest weighted
	 * autocomplete suggestions for a given prefix.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified {@code k} value is less than 1
	 */
	public SuggestTree(int k) {
		if (k < 1) {
			throw new IllegalArgumentException();
		}
		this.k = k;
		this.root = null;
		this.size = 0;
		this.replaceWithSuccessor = false;
	}

	/**
	 * Returns the number of suggestions in this tree.
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Returns a list of the highest weighted suggestions in this tree that
	 * start with the specified prefix, or returns {@code null} if the tree
	 * contains no suggestion with the prefix.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified prefix is an empty string
	 * @throws NullPointerException
	 *             if the specified prefix is {@code null}
	 */
	public Node getSuggestions(String prefix) {
		if (prefix.isEmpty()) {
			throw new IllegalArgumentException();
		}
		int i = 0;
		Node n = this.root;
		while (n != null) {
			if (prefix.charAt(i) < n.firstChar) {
				n = n.left;
			} else if (prefix.charAt(i) > n.firstChar) {
				n = n.right;
			} else {
				for (i++; i < n.charEnd && i < prefix.length(); i++) {
					if (prefix.charAt(i) != n.suggestion.charAt(i)) {
						return null;
					}
				}
				if (i < prefix.length()) {
					n = n.mid;
				} else {
					return n;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the weight of the specified suggestion in this tree, or -1 if the
	 * tree does not contain the suggestion.
	 * 
	 * @throws NullPointerException
	 *             if the specified suggestion is {@code null}
	 */
	public int weightOf(String suggestion) {
		Node n = this.getNode(suggestion);
		return n != null ? n.weight : -1;
	}

	private Node getNode(String suggestion) {
		if (suggestion.isEmpty()) {
			return null;
		}
		int i = 0;
		Node n = this.root;
		while (n != null) {
			if (suggestion.charAt(i) < n.firstChar) {
				n = n.left;
			} else if (suggestion.charAt(i) > n.firstChar) {
				n = n.right;
			} else {
				for (i++; i < n.charEnd; i++) {
					if (i == suggestion.length()
							|| suggestion.charAt(i) != n.suggestion.charAt(i)) {
						return null;
					}
				}
				if (i < suggestion.length()) {
					n = n.mid;
				} else {
					return n.weight != -1 ? n : null;
				}
			}
		}
		return null;
	}

	/**
	 * Inserts the specified suggestion with the specified weight into this
	 * tree, or assigns the specified new weight to the suggestion if it is
	 * already present.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified suggestion is an empty string or the
	 *             specified weight is negative
	 * @throws NullPointerException
	 *             if the specified suggestion is {@code null}
	 */
	public void put(String suggestion, int weight) {
		if (suggestion.isEmpty() || weight < 0) {
			throw new IllegalArgumentException();
		}
		if (this.root == null) {
			this.root = new Node(suggestion, weight, 0, null);
			this.size++;
			return;
		}
		int i = 0;
		Node n = this.root;
		while (true) {
			if (suggestion.charAt(i) < n.firstChar) {
				if (n.left != null) {
					n = n.left;
				} else {
					n.left = new Node(suggestion, weight, i, n);
					this.insertIntoLists(n.left);
					this.size++;
					return;
				}
			} else if (suggestion.charAt(i) > n.firstChar) {
				if (n.right != null) {
					n = n.right;
				} else {
					n.right = new Node(suggestion, weight, i, n);
					this.insertIntoLists(n.right);
					this.size++;
					return;
				}
			} else {
				for (i++; i < n.charEnd; i++) {
					if (i == suggestion.length()
							|| suggestion.charAt(i) != n.suggestion.charAt(i)) {
						n = this.splitNode(n, i);
						break;
					}
				}
				if (i < suggestion.length()) {
					if (n.mid != null) {
						n = n.mid;
					} else {
						n.mid = new Node(suggestion, weight, i, n);
						this.insertIntoLists(n.mid);
						this.size++;
						return;
					}
				} else if (n.weight == -1) {
					n.suggestion = suggestion;
					n.weight = weight;
					this.insertIntoLists(n);
					this.size++;
					return;
				} else if (weight > n.weight) {
					n.weight = weight;
					this.updateListsIncreasedWeight(n);
					return;
				} else if (weight < n.weight) {
					n.weight = weight;
					this.updateListsDecreasedWeight(n);
					return;
				} else {
					return;
				}
			}
		}
	}

	private Node splitNode(Node n, int position) {
		Node[] list = n.list.length < this.k ? n.list : Arrays.copyOf(n.list,
				this.k);
		Node m = new Node(list, n, position);
		n.firstChar = n.suggestion.charAt(position);
		if (n.left != null) {
			n.left.parent = m;
		}
		n.left = null;
		if (n.right != null) {
			n.right.parent = m;
		}
		n.right = null;
		if (n == this.root) {
			this.root = m;
		} else if (n == n.parent.left) {
			n.parent.left = m;
		} else if (n == n.parent.mid) {
			n.parent.mid = m;
		} else {
			n.parent.right = m;
		}
		n.parent = m;
		return m;
	}

	private void insertIntoLists(Node suggestion) {
		for (Node n = suggestion, m = n.mid; n != null; m = n, n = n.parent) {
			if (n.mid == m && m != null) {
				Node[] list = n.list;
				if (list.length < this.k) {
					Node[] a = new Node[list.length + 1];
					int i = list.length;
					while (i > 0 && suggestion.weight > list[i - 1].weight) {
						a[i] = list[i - 1];
						i--;
					}
					a[i] = suggestion;
					System.arraycopy(list, 0, a, 0, i);
					n.list = a;
				} else if (suggestion.weight > list[this.k - 1].weight) {
					int i = this.k - 1;
					while (i > 0 && suggestion.weight > list[i - 1].weight) {
						list[i] = list[i - 1];
						i--;
					}
					list[i] = suggestion;
				} else {
					return;
				}
			}
		}
	}

	private void updateListsIncreasedWeight(Node suggestion) {
		int i = 0;
		for (Node n = suggestion, m = n.mid; n != null; m = n, n = n.parent) {
			if (n.mid == m && m != null) {
				Node[] list = n.list;
				while (i < this.k && suggestion != list[i]) {
					i++;
				}
				if (i == this.k && suggestion.weight <= list[i - 1].weight) {
					return;
				}
				int j = i < this.k ? i : i - 1;
				while (j > 0 && suggestion.weight > list[j - 1].weight) {
					list[j] = list[j - 1];
					j--;
				}
				list[j] = suggestion;
			}
		}
	}

	private void updateListsDecreasedWeight(Node suggestion) {
		int i = 0;
		for (Node n = suggestion, m = n.mid; n != null; m = n, n = n.parent) {
			if (n.mid == m && m != null) {
				Node[] list = n.list;
				while (i < this.k && suggestion != list[i]) {
					i++;
				}
				if (i == this.k) {
					return;
				}
				int j = i;
				while (j < list.length - 1
						&& suggestion.weight < list[j + 1].weight) {
					list[j] = list[j + 1];
					j++;
				}
				Node c;
				if (j == this.k - 1 && (c = this.listCandidate(n)) != null
						&& c.weight > suggestion.weight) {
					list[j] = c;
				} else {
					list[j] = suggestion;
				}
			}
		}
	}

	private Node listCandidate(Node n) {
		Node[] list = n.list;
		Node candidate = null;
		if (n.weight != -1) {
			int i = 0;
			while (i < this.k && n != list[i]) {
				i++;
			}
			if (i == this.k) {
				candidate = n;
			}
		}
		for (Node c = this.firstChild(n); c != null; c = this.nextChild(c)) {
			secondForLoop: for (int i = 0, j = 0; i < c.list.length; i++, j++) {
				Node suggestion = c.list[i];
				for (; j < this.k; j++) {
					if (suggestion == list[j]) {
						continue secondForLoop;
					}
				}
				if (candidate == null || candidate.weight < suggestion.weight) {
					candidate = suggestion;
				}
				break secondForLoop;
			}
		}
		return candidate;
	}

	private Node firstChild(Node n) {
		n = n.mid;
		if (n != null) {
			while (n.left != null) {
				n = n.left;
			}
		}
		return n;
	}

	private Node nextChild(Node child) {
		if (child.right != null) {
			Node n = child.right;
			while (n.left != null) {
				n = n.left;
			}
			return n;
		} else {
			Node n = child.parent;
			Node m = child;
			while (m == n.right) {
				m = n;
				n = n.parent;
			}
			return m == n.left ? n : null;
		}
	}

	/**
	 * Removes the specified suggestion from this tree, if present. The
	 * algorithm is symmetric, preserving the balance of the tree.
	 * 
	 * @throws NullPointerException
	 *             if the specified suggestion is {@code null}
	 */
	public void remove(String suggestion) {
		Node n = this.getNode(suggestion);
		if (n == null) {
			return;
		}
		n.weight = -1;
		this.size--;
		Node m = n;
		if (n.mid == null) {
			Node replacement = this.removeNode(n);
			if (replacement != null) {
				replacement.parent = n.parent;
			}
			if (n == this.root) {
				this.root = replacement;
			} else if (n == n.parent.mid) {
				n.parent.mid = replacement;
			} else {
				if (n == n.parent.left) {
					n.parent.left = replacement;
				} else {
					n.parent.right = replacement;
				}
				while (n != this.root && n != n.parent.mid) {
					n = n.parent;
				}
			}
			n = n.parent;
			if (n == null) {
				return;
			}
		}
		if (n.weight == -1 && n.mid.left == null && n.mid.right == null) {
			n = this.mergeWithChild(n);
			while (n != this.root && n != n.parent.mid) {
				n = n.parent;
			}
			n = n.parent;
			if (n == null) {
				return;
			}
		}
		this.removeFromLists(m, n);
	}

	private Node removeNode(Node n) {
		Node replacement;
		if (n.left == null) {
			replacement = n.right;
		} else if (n.right == null) {
			replacement = n.left;
		} else if (this.replaceWithSuccessor = !this.replaceWithSuccessor) {
			replacement = n.right;
			if (replacement.left != null) {
				while (replacement.left != null) {
					replacement = replacement.left;
				}
				replacement.parent.left = replacement.right;
				if (replacement.right != null) {
					replacement.right.parent = replacement.parent;
				}
				replacement.right = n.right;
				n.right.parent = replacement;
			}
			replacement.left = n.left;
			n.left.parent = replacement;
		} else {
			replacement = n.left;
			if (replacement.right != null) {
				while (replacement.right != null) {
					replacement = replacement.right;
				}
				replacement.parent.right = replacement.left;
				if (replacement.left != null) {
					replacement.left.parent = replacement.parent;
				}
				replacement.left = n.left;
				n.left.parent = replacement;
			}
			replacement.right = n.right;
			n.right.parent = replacement;
		}
		return replacement;
	}

	private Node mergeWithChild(Node n) {
		Node child = n.mid;
		child.firstChar = n.firstChar;
		child.left = n.left;
		if (child.left != null) {
			child.left.parent = child;
		}
		child.right = n.right;
		if (child.right != null) {
			child.right.parent = child;
		}
		child.parent = n.parent;
		if (n == this.root) {
			this.root = child;
		} else if (n == n.parent.left) {
			n.parent.left = child;
		} else if (n == n.parent.mid) {
			n.parent.mid = child;
		} else {
			n.parent.right = child;
		}
		return child;
	}

	private void removeFromLists(Node suggestion, Node firstList) {
		int i = 0;
		for (Node n = firstList, m = n.mid; n != null; m = n, n = n.parent) {
			if (n.mid == m) {
				if (n.weight == -1) {
					n.suggestion = n.mid.suggestion;
				}
				Node[] list = n.list;
				while (i < this.k && suggestion != list[i]) {
					i++;
				}
				if (i < this.k) {
					Node c;
					if (list.length == this.k
							&& (c = this.listCandidate(n)) != null) {
						for (int j = i; j < this.k - 1; j++) {
							list[j] = list[j + 1];
						}
						list[this.k - 1] = c;
					} else {
						int len = list.length;
						Node[] a = new Node[len - 1];
						System.arraycopy(list, 0, a, 0, i);
						System.arraycopy(list, i + 1, a, i, len - i - 1);
						n.list = a;
					}
				}
			}
		}
	}

	/**
	 * Removes all of the suggestions from this tree.
	 */
	public void clear() {
		this.root = null;
		this.size = 0;
	}

	/**
	 * Returns an iterator over the suggestions in this tree.
	 */
	public Iterator iterator() {
		return new Iterator();
	}

	/**
	 * An iterator over the suggestions in the tree. The iterator returns the
	 * suggestions in lexicographic order.
	 */
	public final class Iterator {

		private Node current;
		private boolean initialState;

		private Iterator() {
			this.current = null;
			this.initialState = true;
		}

		/**
		 * Returns the next suggestion in the iteration, or {@code null} if the
		 * iteration has no more suggestions.
		 * 
		 * @throws IllegalStateException
		 *             if the last call to {@code next} returned {@code null}
		 * @throws ConcurrentModificationException
		 *             if the last suggestion returned has been removed from the
		 *             tree
		 */
		public String next() {
			if (this.current == null) {
				if (!this.initialState) {
					throw new IllegalStateException();
				}
				if (SuggestTree.this.root != null) {
					this.current = this.firstSuggestion(SuggestTree.this.root);
				}
				this.initialState = false;
			} else if (this.current.weight == -1) {
				throw new ConcurrentModificationException();
			} else {
				this.current = this.nextSuggestion();
			}
			return this.current != null ? this.current.suggestion : null;
		}

		private Node firstSuggestion(Node n) {
			while (true) {
				while (n.left != null) {
					n = n.left;
				}
				if (n.weight == -1) {
					n = n.mid;
				} else {
					return n;
				}
			}
		}

		private Node nextSuggestion() {
			if (this.current.mid != null) {
				return this.firstSuggestion(this.current.mid);
			} else if (this.current.right != null) {
				return this.firstSuggestion(this.current.right);
			} else if (this.current.parent == null) {
				return null;
			}
			Node n = this.current.parent;
			Node m = this.current;
			while (m == n.right || m == n.mid && n.right == null) {
				m = n;
				n = n.parent;
				if (n == null) {
					return null;
				}
			}
			if (m == n.left) {
				return n.weight != -1 ? n : this.firstSuggestion(n.mid);
			} else {
				return this.firstSuggestion(n.right);
			}
		}

		/**
		 * Returns the weight of the last suggestion returned.
		 * 
		 * @throws IllegalStateException
		 *             if the {@code next} method has not yet been called or the
		 *             last call to {@code next} returned {@code null}
		 * @throws ConcurrentModificationException
		 *             if the last suggestion returned has been removed from the
		 *             tree
		 */
		public int weight() {
			if (this.current == null) {
				throw new IllegalStateException();
			}
			if (this.current.weight == -1) {
				throw new ConcurrentModificationException();
			}
			return this.current.weight;
		}
	}

	/**
	 * A rank-ordered list of autocomplete suggestions. The highest weighted
	 * suggestion is at index 0, the second highest weighted at index 1, and so
	 * on.
	 */
	public static final class Node {

		private Node[] list;
		private String suggestion;
		private int weight;
		private char firstChar;
		private final short charEnd;
		private Node left, mid, right, parent;

		private Node(String suggestion, int weight, int index, Node parent) {
			this.list = new Node[] { this };
			this.suggestion = suggestion;
			this.weight = weight;
			this.firstChar = suggestion.charAt(index);
			this.charEnd = (short) suggestion.length();
			this.left = this.mid = this.right = null;
			this.parent = parent;
		}

		private Node(Node[] list, Node n, int charEnd) {
			this.list = list;
			this.suggestion = n.suggestion;
			this.weight = -1;
			this.firstChar = n.firstChar;
			this.charEnd = (short) charEnd;
			this.left = n.left;
			this.mid = n;
			this.right = n.right;
			this.parent = n.parent;
		}

		/**
		 * Returns the suggestion at the specified position in this list.
		 * 
		 * @throws IndexOutOfBoundsException
		 *             if the {@code index} argument is negative or not less
		 *             than the number of suggestions in the list
		 */
		public String getSuggestion(int index) {
			return this.list[index].suggestion;
		}

		/**
		 * Returns the weight of the suggestion at the specified position in
		 * this list.
		 * 
		 * @throws IndexOutOfBoundsException
		 *             if the {@code index} argument is negative or not less
		 *             than the number of suggestions in the list
		 */
		public int getWeight(int index) {
			return this.list[index].weight;
		}

		/**
		 * Returns the number of suggestions in this list.
		 */
		public int size() {
			return this.list.length;
		}
	}
}