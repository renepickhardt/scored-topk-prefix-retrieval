/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unikoblenz.stpr.ScoredArrayTrie;

import de.unikoblenz.stpr.ScoredArrayTrie.*;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author hartmann
 */
public class ScoredArrayTrieNodeTest {
    
    public ScoredArrayTrieNodeTest() {
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

    @Test
    public void testChildIndex(){
        SortedSet<Integer> indices = new TreeSet<Integer>();
        for(int i = ScoredArrayTrieNode.MIN_CHAR; i < ScoredArrayTrieNode.MAX_CHAR; i++){
            char c = (char) i;
            System.out.println("int "+ i + " char " + c + " byte " + (byte)c + " ci " + ScoredArrayTrieNode.childIndex(c));
            assertTrue(ScoredArrayTrieNode.childIndex(c) >= 0);
            indices.add(ScoredArrayTrieNode.childIndex(c));
        }
        
        assertTrue(indices.first() == 0);               
        assertTrue(indices.last().equals(indices.size() - 1));
        assertTrue(indices.size() == ScoredArrayTrieNode.MAX_CHAR - ScoredArrayTrieNode.MIN_CHAR);
    }
    
    /**
     * Test of getChild method, of class ScoredArrayTrieNode.
     */
    @Test
    public void testGetChild() {
        System.out.println("getChild");
        ScoredArrayTrieNode instance = new ScoredArrayTrieNode('.',0);

        for(int i = ScoredArrayTrieNode.MAX_CHAR; i < ScoredArrayTrieNode.MAX_CHAR; i++){
            char c = (char) i;
            assertNull(instance.getChild(c));
            ScoredArrayTrieNode n = new ScoredArrayTrieNode(c,0);
            instance.setChild(n);
            assertTrue(n == instance.getChild(n.getChar()));
        }
    }

    
}