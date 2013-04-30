package de.unikoblenz.stpr.implementation.trie;
import de.unikoblenz.stpr.implementation.trie.BTrie;

public class BRun {
	public static void main(String[] args) {		
		BTrie T = new BTrie();
		T.add("A");
		T.add("AA");
		T.add("AB");
		T.add("AAAA");
		T.add("AAAB");
		T.add("AABA");
		T.add("AABB");
		T.add("AACA");
		T.add("B");
		T.add("BADA");
		
		System.out.println(T.toString());
	}

}
