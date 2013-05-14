package de.renepickhardt.executables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import de.renepickhardt.utils.Config;
import de.renepickhardt.utils.IOHelper;
import de.renepickhardt.utils.SuggestTree;
import de.renepickhardt.utils.SuggestTree.Node;
import de.unikoblenz.stpr.ScoredArrayTrie.ScoredArrayTrie;
import de.unikoblenz.stpr.ScoredArrayTrie.SearchResult;
import de.unikoblenz.stpr.interfaces.trie.TrieInterface;

public class Run {
	public static final String INPUT_FILE = Config.get().inputFile;

	public static void main(String[] args) throws IOException, Exception {

		// T.insertScored("AB", 6);
		// T.insertScored("AAA", 3);
		// T.insertScored("AAB", 2);
		// T.insertScored("AAC", 4);
		//
		// IOHelper.log("Trie:");
		// IOHelper.log(T.toString());
		// IOHelper.log("Top Scores:");
		// IOHelper.log(T.root.printTopChildScores());
		// IOHelper.log("Trie:");
		// IOHelper.log(T.toString());

		// fillTrie(new LinkedTrie());

		fillScoredTrie(new ScoredArrayTrie());
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

	public static void fillScoredTrie(ScoredArrayTrie T) throws IOException,
			Exception {
		IOHelper.log("Start testing: " + T.getClass().getName());

		BufferedReader br = IOHelper.openReadFile(INPUT_FILE);
		String line = "";
		int i = 0;
		long baseMemory = Runtime.getRuntime().totalMemory();
		SuggestTree tree = new SuggestTree(3);
		T.add("wissenIstMacht", 1000000);
		while ((line = br.readLine()) != null) {
			String key = line.split("\t")[0];
			int value = Integer.parseInt(line.split("\t")[1]);
			T.add(key, value);
			tree.put(key, value);
			if (++i % 10000 == 0) {
				IOHelper.log("Items: " + i + "\t Memory:"
						+ (Runtime.getRuntime().totalMemory() - baseMemory));
			}
			if (i > 1500000) {
				break;
			}
		}
		T.root.recSetMaxScore();
		T.root.recSetTopChilds();

		// IOHelper.log(T.toString());

		while (true) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String input;
			input = in.readLine();
			if (input.equals("-1")) {
				break;
			}
			long start = System.nanoTime();
			List<SearchResult> res = T.getTopK(input, 5);
			long end = System.nanoTime();
			System.out.println("\n" + (end - start) / 1000
					+ " micro seconds for suggestions with PREFIX TRIE");
			for (SearchResult entry : res) {
				IOHelper.log(entry.score + "\t" + entry.name);
			}

			System.out.println("suggestions with suggest tree:\n");
			start = System.nanoTime();
			Node resTree = tree.getSuggestions(input);
			end = System.nanoTime();
			System.out.println("\n" + (end - start) / 1000
					+ " micro seconds for suggestions with SUGGEST TREE");

			for (i = 0; i < resTree.size(); i++) {
				IOHelper.log(resTree.getWeight(i) + "\t"
						+ resTree.getSuggestion(i));
			}

		}

		// T = null;
		// Runtime.getRuntime().gc();
		// br.close();
		// IOHelper.log("Test finished.");
		// // IOHelper.log(T.toString());
	}
}
