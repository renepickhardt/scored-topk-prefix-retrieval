/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unikoblenz.stpr.ArrayTrie;

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
public class ArrayTrieTest {
    
    public ArrayTrieTest() {
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
        ArrayTrie T = new ArrayTrie();
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
        for (int i = ArrayTrieNode.MIN_CHAR; i < ArrayTrieNode.MAX_CHAR; i++ ){
            range += (char)i;
        }
        testStrings.add(range);
        
        for (String s: testStrings){
            System.out.println("Testing "+s);
            assertNull(T.find(s));
            T.add(s);
            List<ArrayTrieNode> path = T.find(s);
            assertNotNull(path);

            for (int i = 0; i < s.length(); i++){
                assertEquals(path.get(i+1).getChar(),s.charAt(i));
            }
            
        }
        
        System.err.println(T.toString());
    }
    
    @Test
    public void testFind() {
        System.out.println("find");
        ArrayTrie T = new ArrayTrie();
        assertEquals(T.root, T.find("").get(0));
        assertEquals(T.find("").size(),1);
        assertNull(T.find("A"));
        T.add("A");
        assertNotNull(T.find("A"));
    }
    
    @Test
    public void bulidTrie(){
        System.out.println("find");
        ArrayTrie T = new ArrayTrie();
        
        T.add("AA");
        T.add("AB");
        T.add("AC");
        
        ArrayTrieNode A = T.root.getChild('A');
        ArrayTrieNode AA = A.getChild('A');
        ArrayTrieNode AB = A.getChild('B');
        ArrayTrieNode AC = A.getChild('C');
        
        assertNotNull(A);
        assertNotNull(AA);
        assertNotNull(AB);
        assertNotNull(AC);
        
        assertNull(A.getChild('X'));
                
        assertFalse(A.isLeaf());
        assertTrue(AA.isLeaf());
    }
}