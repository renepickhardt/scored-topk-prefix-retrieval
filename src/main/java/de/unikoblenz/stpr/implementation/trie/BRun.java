package de.unikoblenz.stpr.implementation.trie;

import de.unikoblenz.stpr.LinkedTrie.LinkedTrie;
import de.unikoblenz.stpr.ArrayTrie.ArrayTrie;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;

import de.renepickhardt.utils.Config;
import de.renepickhardt.utils.IOHelper;
import de.unikoblenz.stpr.interfaces.trie.TrieInterface;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class BRun {
    public static final String INPUT_FILE = Config.get().inputFile;

    public static void testTrie(TrieInterface T) throws IOException {
        IOHelper.log("Start testing: " + T.getClass().getName());

        BufferedReader br = IOHelper.openReadFile(INPUT_FILE);
        String line = "";
        int i = 0;
        long baseMemory = Runtime.getRuntime().totalMemory();
        while ((line = br.readLine()) != null) {
            T.add(line.split("\t")[0]);
            if (++i % 10000 == 0) {
                IOHelper.log("Items: " + i + "\t Memory:" + (Runtime.getRuntime().totalMemory() - baseMemory));
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

    public static void main(String[] args) throws IOException {
        testTrie(new LinkedTrie());

        testTrie(new ArrayTrie());

        testTrie(new HashSetTrie());

        testTrie(new TreeSetTrie());
    }
}
