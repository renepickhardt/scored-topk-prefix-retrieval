package de.unikoblenz.stpr.implementation.trie;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import de.renepickhardt.utils.Config;
import de.renepickhardt.utils.IOHelper;
import de.unikoblenz.stpr.implementation.trie.BTrie;

public class BRun {
	public static void main(String[] args) throws IOException {
		
		BLinkedTrie T = new BLinkedTrie();
		
		BufferedReader br = IOHelper.openReadFile(Config.get().inputFile + ".1000");

		String line = "";
		int i = 0;
		while ((line = br.readLine() ) != null) {
			T.add(line.split("\t")[0]);
			if (i++ == 1000) break;
		}
		
		System.out.println(T.toString());
	}

}
