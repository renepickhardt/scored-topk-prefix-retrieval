package de.renepickhardt.executables;

import de.unikoblenz.stpr.LinkedTrie.LinkedTrie;
import de.unikoblenz.stpr.ArrayTrie.ArrayTrie;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Arrays;

import de.renepickhardt.utils.Config;
import de.renepickhardt.utils.IOHelper;
import de.unikoblenz.stpr.ScoredLinkedTrie.ScoredLinkedTrie;
import de.unikoblenz.stpr.interfaces.trie.TrieInterface;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Run {
    public static final String INPUT_FILE = Config.get().inputFile;

    public static void main(String[] args) throws IOException {
        ScoredLinkedTrie T = new ScoredLinkedTrie();
        T.root.getInsertChild('A', 4);
        T.root.getInsertChild('A', 4);
        T.root.getInsertChild('B', 5);
        IOHelper.log(T.toString());
        
        T.insertScored("AA", 6);
        T.insertScored("AAA", 3);
        T.insertScored("AAB", 2);
        T.insertScored("AAC", 4);
        
        IOHelper.log("Trie:");
        IOHelper.log(T.toString());
        IOHelper.log("Top Scores:");
        IOHelper.log( T.root.printTopChildScores() );
        IOHelper.log("Trie:");
        IOHelper.log(T.toString());
        
//        fillTrie(new LinkedTrie());
        
        fillScoredTrie(new ScoredLinkedTrie());
//        
//        fillTrie(new ArrayTrie());
//
//        fillTrie(new HashSetTrie());
//
//        fillTrie(new TreeSetTrie());
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

    public static void fillScoredTrie(ScoredLinkedTrie T) throws IOException {
        IOHelper.log("Start testing: " + T.getClass().getName());

        BufferedReader br = IOHelper.openReadFile(INPUT_FILE);
        String line = "";
        int i = 0;
        long baseMemory = Runtime.getRuntime().totalMemory();
        while ((line = br.readLine()) != null) {
            T.add(line.split("\t")[0],Integer.parseInt(line.split("\t")[1]));
            if (++i % 10000 == 0) {
                IOHelper.log("Items: " + i + "\t Memory:" + (Runtime.getRuntime().totalMemory() - baseMemory));
            }
            if (i > 100) {
                break;
            }
        }
        T.root.getSetTopScore();
        IOHelper.log(T.toString());
        T = null;
        Runtime.getRuntime().gc();
        br.close();
        IOHelper.log("Test finished.");
    }
}
