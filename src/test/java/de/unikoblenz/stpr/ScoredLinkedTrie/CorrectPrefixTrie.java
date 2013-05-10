package de.unikoblenz.stpr.ScoredLinkedTrie;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.renepickhardt.utils.Config;
import de.renepickhardt.utils.IOHelper;

public class CorrectPrefixTrie {
	public ScoredLinkedTrie T = new ScoredLinkedTrie();

	@Before
	public void before() {
		BufferedReader br = IOHelper.openReadFile(Config.get().inputFile);
		String line = "";
		int i = 0;
		long baseMemory = Runtime.getRuntime().totalMemory();

		try {
			while ((line = br.readLine()) != null) {
				String key = line.split("\t")[0];
				int value = Integer.parseInt(line.split("\t")[1]);
				this.T.insertScored(key, value);
				if (++i % 10000 == 0) {
					IOHelper.log("Items: " + i + "\t Memory:"
							+ (Runtime.getRuntime().totalMemory() - baseMemory));
				}
				if (i > 1000) {
					break;
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void test() {
		this.topScoresInDescendingOrder(this.T.root);
	}

	private void topScoresInDescendingOrder(ScoredLinkedTrieNode root) {
		for (int j = 0; j < root.topScores.length - 1; j++) {
			System.out.println(root.topScores[j] + " > "
					+ root.topScores[j + 1] + "?");
			assertTrue(root.topScores[j] >= root.topScores[j + 1]);
		}
		for (ScoredLinkedTrieNode tmp : root.getChildren()) {
			this.topScoresInDescendingOrder(tmp);
		}
	}
}
