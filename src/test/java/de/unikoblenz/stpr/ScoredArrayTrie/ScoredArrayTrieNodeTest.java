/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unikoblenz.stpr.ScoredArrayTrie;

import de.unikoblenz.stpr.ScoredArrayTrie.*;
import java.util.Collection;
import java.util.List;
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
            // System.out.println("int "+ i + " char " + c + " byte " + (byte)c + " ci " + ScoredArrayTrieNode.childIndex(c));
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

    /**
     * Test of addGetChild method, of class ScoredArrayTrieNode.
     */
    @Test
    public void testAddGetChild() {
        System.out.println("addGetChild");
        ScoredArrayTrieNode instance = new ScoredArrayTrieNode('.',0);
        char c = 'A';
        ScoredArrayTrieNode n = instance.addGetChild(c,0);
        assertNotNull(n);
        assertEquals(n, instance.addGetChild(c,0));
    }

    public void testTopChildsValid(List<TopScoreEntry> topChilds) {
        // size ok?
        assertTrue(topChilds.size() <= ScoredArrayTrieNode.TOP_K);

        if (topChilds.size() == 0) {
            return;
        }

        // sorted?
        int lastScore = topChilds.get(0).score;
        for (TopScoreEntry e : topChilds) {
            assertTrue(e.score <= lastScore);
            lastScore = e.score;
        }
        
    }

    
    @Test
    public void testInsertTopChildEntry(){
        ScoredArrayTrieNode n = new ScoredArrayTrieNode('.', 0);        
        assertEquals(1,n.topChilds.size());
        assertEquals(0,n.topChilds.get(0).score);
        assertEquals(n,n.topChilds.get(0).node);
        
        ScoredArrayTrieNode m = new ScoredArrayTrieNode('.', 0);
        n.insertTopChildEntry(new TopScoreEntry(m, 10));
        assertEquals(2,n.topChilds.size());
        assertEquals(10,n.topChilds.get(0).score);
        assertEquals(m,n.topChilds.get(0).node);
        assertEquals(0,n.topChilds.get(1).score);
        assertEquals(n,n.topChilds.get(1).node);
        
        ScoredArrayTrieNode m2 = new ScoredArrayTrieNode('.', 0);
        n.insertTopChildEntry(new TopScoreEntry(m2, 5));
        assertEquals(3,n.topChilds.size());
        assertEquals(10,n.topChilds.get(0).score);
        assertEquals(m,n.topChilds.get(0).node);
        assertEquals(5,n.topChilds.get(1).score);
        assertEquals(m2,n.topChilds.get(1).node);
        assertEquals(0,n.topChilds.get(2).score);
        assertEquals(n,n.topChilds.get(2).node);

        
        for (int i = 0; i< ScoredArrayTrieNode.TOP_K + 5; i++){
            ScoredArrayTrieNode m3 = new ScoredArrayTrieNode('.', 0);
            n.insertTopChildEntry(new TopScoreEntry(m3, 1));
        }
        assertEquals(ScoredArrayTrieNode.TOP_K, n.topChilds.size());
        assertEquals(10,n.topChilds.get(0).score);
        assertEquals(m,n.topChilds.get(0).node);
    }
    
    
    @Test
    public void testInsertChild(){
        ScoredArrayTrieNode n = new ScoredArrayTrieNode('.', 0);
        
        ScoredArrayTrieNode m = new ScoredArrayTrieNode('A', 9);
        n.insertChid(m);
        assertEquals(2,n.topChilds.size());
        assertEquals(9,n.topChilds.get(0).score);
        assertEquals(m,n.topChilds.get(0).node);
        assertEquals(0,n.topChilds.get(1).score);
        assertEquals(n,n.topChilds.get(1).node); 

        ScoredArrayTrieNode m3 = new ScoredArrayTrieNode('A', 10);
        n.insertChid(m3);
        assertEquals(2,n.topChilds.size());
        assertEquals(10,n.topChilds.get(0).score);
        assertEquals(m3,n.topChilds.get(0).node);
        assertEquals(0,n.topChilds.get(1).score);
        assertEquals(n,n.topChilds.get(1).node);
        testTopChildsValid(n.topChilds);
    
        ScoredArrayTrieNode m2 = new ScoredArrayTrieNode('B', 5);
        n.insertChid(m2);
        assertEquals(3,n.topChilds.size());
        assertEquals(10,n.topChilds.get(0).score);
        assertEquals(m3,n.topChilds.get(0).node);
        assertEquals(5,n.topChilds.get(1).score);
        assertEquals(m2,n.topChilds.get(1).node);
        assertEquals(0,n.topChilds.get(2).score);
        assertEquals(n,n.topChilds.get(2).node);
        testTopChildsValid(n.topChilds);
        
        for (int i = 0; i< ScoredArrayTrieNode.TOP_K + 5; i++){
            ScoredArrayTrieNode m4 = new ScoredArrayTrieNode((char)((int)'D'+i), 5);
            n.insertChid(m4);
        }
        assertEquals(ScoredArrayTrieNode.TOP_K, n.topChilds.size());
        assertEquals(10,n.topChilds.get(0).score);
        assertEquals(m3,n.topChilds.get(0).node);
        
        testTopChildsValid(n.topChilds);

    }
    
    @Test
    public void testRemoveTopEntries(){
        ScoredArrayTrieNode n = new ScoredArrayTrieNode('.', 0);
        ScoredArrayTrieNode m = null;
        for (int i = 0; i< ScoredArrayTrieNode.TOP_K + 5; i++){
            m = new ScoredArrayTrieNode((char)((int)'A'+i), 5+i);
            n.insertChid(m);
        }
        assertEquals(ScoredArrayTrieNode.TOP_K, n.topChilds.size());
        n.removeTopEntry(m);
        assertEquals(ScoredArrayTrieNode.TOP_K - 1, n.topChilds.size());
    }
    
//    public void checkScores(List<TopScoreEntry> topChilds){
//        // scores correct?
//        for (TopScoreEntry e : topChilds) {
//            assertEquals(e.node.calcMaxScore(), e.score);
//        }
//    }
    
    @Test
    public void testSetTopChilds(){
        ScoredArrayTrieNode r = new ScoredArrayTrieNode('.', 0);
        // initializing test
        assertEquals(r,r.topChilds.get(0).node);
        assertEquals(0,r.topChilds.get(0).score);
        
        // Test child handling
        r.setChild(new ScoredArrayTrieNode('A', 1));
        r.setChild(new ScoredArrayTrieNode('B', 2));
        r.setChild(new ScoredArrayTrieNode('C', 3));
        assertEquals(r.calcMaxScore(),r.recSetTopChilds()); // returns maxScore
        testTopChildsValid(r.topChilds); 

        assertEquals(Math.min(ScoredArrayTrieNode.TOP_K, 4),r.topChilds.size());
        assertEquals('C',r.topChilds.get(0).node.getChar());
        assertEquals('B',r.topChilds.get(1).node.getChar());
        assertEquals('A',r.topChilds.get(2).node.getChar());
        assertEquals('.',r.topChilds.get(3).node.getChar());
        
        assertEquals(3,r.topChilds.get(0).score);
        assertEquals(2,r.topChilds.get(1).score);
        assertEquals(1,r.topChilds.get(2).score);
        assertEquals(0,r.topChilds.get(3).score);

        // Add some subchilds
        r.getChild('A').setChild(new ScoredArrayTrieNode('A', 10));
        assertEquals(r.calcMaxScore(),r.recSetTopChilds()); // returns maxScore
        testTopChildsValid(r.topChilds); 
        assertEquals('A',r.topChilds.get(0).node.getChar());
        assertEquals(10,r.topChilds.get(0).score);
    }    
}