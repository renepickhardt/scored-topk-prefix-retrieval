package de.unikoblenz.stpr.implementation.trie;
import de.unikoblenz.stpr.implementation.trie.BTrie;

public class Run {
	public static void main(String[] args) {		
		BScoredTrie T = new BScoredTrie();
		T.add("A",0);
		T.add("AA",11);
		T.add("AB",12);
		T.add("AAAA",11);
		T.add("AAAB",1);
		T.add("AABA",2);
		T.add("AABB",3);
		T.add("AACA",5);
		T.add("B",2);
		T.add("BADA",3);
		
		System.out.println(T.toString());
	}

}
