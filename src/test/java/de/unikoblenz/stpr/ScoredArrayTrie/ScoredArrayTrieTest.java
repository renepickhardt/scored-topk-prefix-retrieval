/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unikoblenz.stpr.ScoredArrayTrie;

import de.unikoblenz.stpr.ScoredArrayTrie.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hartmann
 */
public class ScoredArrayTrieTest {
    
    public ScoredArrayTrieTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class ArrayTrie.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        ScoredArrayTrie T = new ScoredArrayTrie();
        ArrayList<String> testStrings = new ArrayList<String>();
                
        testStrings.add("A");
        
        // Long Strings
        String longString = "";
        for (int i = 0; i < 100; i++ ){
            longString += "A";
        }
        testStrings.add(longString);
        
        // Add string range
        String range = "";
        for (int i = ScoredArrayTrieNode.MIN_CHAR; i < ScoredArrayTrieNode.MAX_CHAR; i++ ){
            range += (char)i;
        }
        testStrings.add(range);
        
        for (String s: testStrings){
            System.out.println("Testing "+s);
            assertNull(T.find(s));
            T.add(s);
            List<ScoredArrayTrieNode> path = T.find(s);
            assertNotNull(path);

            for (int i = 0; i < s.length(); i++){
                assertEquals(path.get(i+1).getChar(),s.charAt(i));
            }
            
        }
        
        System.err.println(T.toString());
    }

    @Test
    public void testAddScore() {
        ScoredArrayTrie T = new ScoredArrayTrie();

        // Cannot change root score
        T.add("",1);
        assertTrue(T.root.getScore() == 0);

        T.add("A",1);
        assertTrue(T.root.getChild('A').getScore() == 1);
        T.add("A",2);
        assertTrue(T.root.getChild('A').getScore() == 2);

        T.add("AB",3);
        assertTrue(T.root.getChild('A').getScore() == 2);
        assertTrue(T.root.getChild('A').getChild('B').getScore() == 3);
    }

    
    @Test
    public void testFind() {
        System.out.println("find");
        ScoredArrayTrie T = new ScoredArrayTrie();
        assertEquals(T.root, T.find("").get(0));
        assertEquals(T.find("").size(),1);
        assertNull(T.find("A"));
        T.add("A");
        assertNotNull(T.find("A"));
    }
    
    @Test
    public void bulidTrie(){
        System.out.println("find");
        ScoredArrayTrie T = new ScoredArrayTrie();
        
        T.add("AA");
        T.add("AB");
        T.add("AC");
        
        ScoredArrayTrieNode A = T.root.getChild('A');
        ScoredArrayTrieNode AA = A.getChild('A');
        ScoredArrayTrieNode AB = A.getChild('B');
        ScoredArrayTrieNode AC = A.getChild('C');
        
        assertNotNull(A);
        assertNotNull(AA);
        assertNotNull(AB);
        assertNotNull(AC);
        
        assertNull(A.getChild('X'));
                
        assertFalse(A.isLeaf());
        assertTrue(AA.isLeaf());
    }
    
    @Test
    public void testMaxScore(){
        ScoredArrayTrie T = new ScoredArrayTrie();
        // empty trie
        assertEquals(T.root.calcMaxScore(), 0);

        T.add("A",0);
        assertEquals(T.root.calcMaxScore(),0);
                
        T.add("A",2);
        T.add("B",2);
        T.add("C",3);
        assertEquals(T.root.calcMaxScore(),3);
        
        T.add("AAAAAA",20);
        assertEquals(T.root.calcMaxScore(),20);

        T.add("B",30);
        assertEquals(T.root.calcMaxScore(),30);

        T.add("A",40);
        assertEquals(T.root.calcMaxScore(),40);
    }

    @Test
    public void testTopChilds(){
        ScoredArrayTrie T = new ScoredArrayTrie();
        T.add("A",1);
        T.add("B",2);
        T.add("C",3);
        
        List<TopScoreEntry> result = T.root.calcTopChilds();
        assertEquals(3, result.get(0).score);
        assertEquals(T.root.getChild('C'), result.get(0).node);
        assertEquals(2, result.get(1).score);
        assertEquals(T.root.getChild('B'), result.get(1).node);
        
        T.add("AA",4);
        result = T.root.calcTopChilds();
        assertEquals(result.get(0).score, 4);
        assertEquals(T.root.getChild('A'), result.get(0).node);
        
        T.add("AAB",5);
        result = T.root.calcTopChilds();
        assertEquals(result.get(0).score, 5);
        assertEquals(T.root.getChild('A'), result.get(0).node);

        T.add("BBBBBBBBBBBB",6);
        result = T.root.calcTopChilds();
        assertEquals(result.get(0).score, 6);
        assertEquals(T.root.getChild('B'), result.get(0).node);
        
        T = new ScoredArrayTrie();
        for (int i = ScoredArrayTrieNode.MIN_CHAR; i < ScoredArrayTrieNode.MAX_CHAR; i++) {
            T.add("" + (char) i, i);
        }
        
        result = T.root.calcTopChilds();
        assertTrue(ScoredArrayTrieNode.TOP_K >= result.size());
    }
}