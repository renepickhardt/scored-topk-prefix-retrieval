package de.unikoblenz.stpr.implementation.trie;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;

import de.renepickhardt.utils.Config;
import de.renepickhardt.utils.IOHelper;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class BRun {
	public static void main(String[] args) throws IOException {
		
                BLinkedTrie T = new BLinkedTrie();
		
		BufferedReader br = IOHelper.openReadFile(Config.get().inputFile);
		String line = "";
		int i = 0;
		long baseMemory = Runtime.getRuntime().totalMemory();
		IOHelper.log("start test Linked trie");
		while ((line = br.readLine()) != null) {
			T.add(line.split("\t")[0]);
			// if (i++ % 50000 == 0) {
			// IOHelper.log(i + " lines indexed");
			// }
			if (++i % 10000 == 0) {
				IOHelper.log(Runtime.getRuntime().totalMemory() - baseMemory
						+ "\t" + i);
			}
			if (i > 2000000) {
				break;
			}
		}
		IOHelper.log("done test trie");
                System.in.read();
		T = null;
		Runtime.getRuntime().gc();
                br.close();


                BTrie U = new BTrie();
		br = IOHelper.openReadFile(Config.get().inputFile);
      		line = "";
		i = 0;
		baseMemory = Runtime.getRuntime().totalMemory();
		IOHelper.log("start test Array trie");
		while ((line = br.readLine()) != null) {
			U.add(line.split("\t")[0]);
			// if (i++ % 50000 == 0) {
			// IOHelper.log(i + " lines indexed");
			// }
			if (++i % 10000 == 0) {
				IOHelper.log(Runtime.getRuntime().totalMemory() - baseMemory
						+ "\t" + i);
			}
			if (i > 2000000) {
				break;
			}
		}
		IOHelper.log("done test array trie");
                System.in.read();
		U = null;
                br.close();
 		Runtime.getRuntime().gc();
                

		i = 0;
		br.close();
		br = IOHelper.openReadFile(Config.get().inputFile);
		HashSet<String> set = new HashSet<String>();
		IOHelper.log("start test hashset");
		baseMemory = Runtime.getRuntime().totalMemory();
		while ((line = br.readLine()) != null) {
			set.add(line.split("\t")[0]);
			// if (i++ % 50000 == 0) {
			// IOHelper.log(i + " lines indexed");
			// }
			if (++i % 10000 == 0) {
				IOHelper.log(Runtime.getRuntime().totalMemory() - baseMemory
						+ "\t" + i);
			}
			if (i > 2000000) {
				break;
			}

		}
		IOHelper.log("done test hashset");
                System.in.read();

		i = 0;
		br.close();
		set = null;
		Runtime.getRuntime().gc();
		br = IOHelper.openReadFile(Config.get().inputFile);
		TreeSet<String> tSet = new TreeSet<String>();
		IOHelper.log("start test treeset");
		baseMemory = Runtime.getRuntime().totalMemory();
		while ((line = br.readLine()) != null) {
			tSet.add(line.split("\t")[0]);
			// if (i++ % 50000 == 0) {
			// IOHelper.log(i + " lines indexed");
			// }
			if (++i % 10000 == 0) {
				IOHelper.log(Runtime.getRuntime().totalMemory() - baseMemory
						+ "\t" + i);
			}
			if (i > 2000000) {
				break;
			}

		}
		IOHelper.log("done test treeset");
		br.close();
                System.in.read();

		// IOHelper.log("start print"); // System.out.println(T.toString());

	}
}
