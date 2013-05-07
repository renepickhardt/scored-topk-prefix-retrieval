package de.renepickhardt.executables;

import java.io.BufferedReader;
import java.io.IOException;

import de.renepickhardt.utils.Config;
import de.renepickhardt.utils.IOHelper;
import de.unikoblenz.stpr.ScoredLinkedTrie.ScoredLinkedTrie;
import de.unikoblenz.stpr.interfaces.trie.TrieInterface;

public class Run {
	public static final String INPUT_FILE = Config.get().inputFile;

	public static void main(String[] args) throws IOException {
		// ScoredLinkedTrie T = new ScoredLinkedTrie();
		// T.add("A", 5);
		// T.add("B", 6);
		// T.add("AA", 7);
		// T.add("AAAA", 1);
		// T.add("AAAB", 2);
		// T.add("AAAC", 3);
		// T.add("AAB", 15);
		// T.add("AAAC", 19);
		// T.add("B", 16);
		// T.add("BAA", 17);
		// T.add("BAAA", 11);
		// T.add("BAAB", 12);
		// T.add("BAAC", 12);
		// T.add("BAB", 51);
		// T.add("BAAC", 91);
		//
		// IOHelper.log("Trie:");
		// T.root.getSetTopChildScores();
		// IOHelper.log(T.toString());
		// IOHelper.log("Top Scores:");
		// IOHelper.log( T.root.printTopChildScores() );
		// IOHelper.log("Trie:");
		// IOHelper.log(T.toString());

		// fillTrie(new LinkedTrie());
		fillScoredTrie(new ScoredLinkedTrie());
		//
		// fillTrie(new ArrayTrie());
		//
		// fillTrie(new HashSetTrie());
		//
		// fillTrie(new TreeSetTrie());
	}

	public static void fillTrie(TrieInterface T) throws IOException {
		IOHelper.log("Start testing: " + T.getClass().getName());

		BufferedReader br = IOHelper.openReadFile(INPUT_FILE);
		String line = "";
		int i = 0;
		long baseMemory = Runtime.getRuntime().totalMemory();
		while ((line = br.readLine()) != null) {
			T.add(line.split("\t")[0]);
			if (++i % 10000 == 0) {
				IOHelper.log("Items: " + i + "\t Memory:"
						+ (Runtime.getRuntime().totalMemory() - baseMemory));
			}
			if (i > 2000000) {
				break;
			}
		}
		T = null;
		Runtime.getRuntime().gc();
		br.close();
		IOHelper.log("Test finished.");
	}

	public static void fillScoredTrie(ScoredLinkedTrie T) throws IOException {
		IOHelper.log("Start testing: " + T.getClass().getName());

		BufferedReader br = IOHelper.openReadFile(INPUT_FILE);
		String line = "";
		int i = 0;
		long baseMemory = Runtime.getRuntime().totalMemory();
		while ((line = br.readLine()) != null) {
			String value = line.split("\t")[0];
			T.add(value.substring(0, Math.min(20, value.length())),
					Integer.parseInt(line.split("\t")[1]));
			if (++i % 10000 == 0) {
				IOHelper.log("Items: " + i + "\t Memory:"
						+ (Runtime.getRuntime().totalMemory() - baseMemory));
			}
			if (i > 10000000) {
				break;
			}
		}
		T.root.getSetTopChildScores();
		// IOHelper.log(T.toString());
		T = null;
		Runtime.getRuntime().gc();
		br.close();
		IOHelper.log("Test finished.");
	}
}
