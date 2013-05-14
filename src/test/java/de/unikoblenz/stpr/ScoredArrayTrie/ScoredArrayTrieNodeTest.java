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
    public void testChildIndex() {
        SortedSet<Integer> indices = new TreeSet<Integer>();
        for (int i = ScoredArrayTrieNode.MIN_CHAR; i < ScoredArrayTrieNode.MAX_CHAR; i++) {
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
        ScoredArrayTrieNode instance = new ScoredArrayTrieNode('.', 0);

        for (int i = ScoredArrayTrieNode.MAX_CHAR; i < ScoredArrayTrieNode.MAX_CHAR; i++) {
            char c = (char) i;
            assertNull(instance.getChild(c));
            ScoredArrayTrieNode n = new ScoredArrayTrieNode(c, 0);
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
        ScoredArrayTrieNode instance = new ScoredArrayTrieNode('.', 0);
        char c = 'A';
        ScoredArrayTrieNode n = instance.addGetChild(c);
        assertNotNull(n);
        assertEquals(n, instance.addGetChild(c));
    }

    public void checkMaxSubScore(ScoredArrayTrieNode n) {
        //System.out.println("Cchk SubScore for node " + n.toString());
        assertEquals(n.getMaxScore(), n.calcMaxScore());
        if (n.isLeaf()) {
            return;
        }
        for (ScoredArrayTrieNode c : n.getChildren()) {
            if (c == null) {
                continue;
            }
            checkMaxSubScore(c);
        }
    }

    @Test
    public void testRecSetMaxScore() {
        System.out.println("MaxScores");
        ScoredArrayTrie T = new ScoredArrayTrie();
        ScoredArrayTrieNode root = T.root;
        checkMaxSubScore(root);

        T.add("A", 0);
        T.add("A", 2);
        T.add("B", 2);
        T.add("C", 3);
        T.root.recSetMaxScore();
        checkMaxSubScore(root);
        System.out.println(T.toString());

        T.add("AAAAAA", 20);
        T.root.recSetMaxScore();
        checkMaxSubScore(root);
        System.out.println(T.toString());

        T.add("B", 30);
        T.root.recSetMaxScore();
        checkMaxSubScore(root);
        System.out.println(T.toString());

        T.add("A", 40);
        T.root.recSetMaxScore();
        checkMaxSubScore(root);
        System.out.println(T.toString());
    }

    public void checkTopChilds(ScoredArrayTrieNode r) {
        List<ScoredArrayTrieNode> topChilds = r.topChilds;
        // size ok?
        assertTrue(topChilds.size() <= ScoredArrayTrieNode.TOP_K);

        if (topChilds.size() == 0) {
            return;
        }

        // sorted?
        int lastScore = topChilds.get(0).getMaxScore();
        for (ScoredArrayTrieNode e : topChilds) {
            assertTrue(e.getMaxScore()<= lastScore);
            lastScore = e.getMaxScore();
        }
        
        assertEquals(r.getMaxScore(), Math.max(r.score, topChilds.get(0).getMaxScore()));
    }

    @Test
    public void testRecTopChilds() {
        System.out.println("TopChilds");
        ScoredArrayTrie T = new ScoredArrayTrie();
        ScoredArrayTrieNode root = T.root;
        root.recSetMaxScore();
        checkMaxSubScore(root);
        
        T.add("A", 0);
        T.add("A", 1);
        T.add("B", 2);
        T.add("C", 3);
        root.recSetMaxScore();
        root.recSetTopChilds();
        checkTopChilds(root);
        System.out.println(T.toString());

        // overflow Test
        int max = 0;
        for (int i = 0; i < ScoredArrayTrieNode.TOP_K + 5; i++){
            T.add(""+(char)((int)'A' + i), i);
            max = i;
        }
        root.recSetMaxScore();
        root.recSetTopChilds();
        checkTopChilds(root);
        assertEquals(max, root.topChilds.get(0).getScore()); 
        System.out.println(T.toString());
        
        T.add("AAAAAA", 20);
        root.recSetMaxScore();
        root.recSetTopChilds();
        System.out.println(T.toString());
        checkTopChilds(root);

        T.add("B", 30);
        root.recSetMaxScore();
        root.recSetTopChilds();
        checkTopChilds(root);
        System.out.println(T.toString());

        T.add("A", 40);
        root.recSetMaxScore();
        root.recSetTopChilds();
        checkTopChilds(root);
        System.out.println(T.toString());
    }
    
    @Test
    public void testInstertNewChild(){
        System.out.println("InsertNewChild");
        ScoredArrayTrieNode r = new ScoredArrayTrieNode('.', 0);
        
        ScoredArrayTrieNode c1 = new ScoredArrayTrieNode('A', 5);
        r.insertNewChild(c1);
        assertEquals(c1, r.getChild('A'));
        assertEquals(5, r.maxScore);
        assertArrayEquals(r.topChilds.toArray(), new ScoredArrayTrieNode[]{c1});
        checkTopChilds(r);
        checkMaxSubScore(r);
        
        ScoredArrayTrieNode c2 = new ScoredArrayTrieNode('B', 10);
        r.insertNewChild(c2);
        assertEquals(c2, r.getChild('B'));
        assertEquals(10, r.maxScore);
        assertArrayEquals(r.topChilds.toArray(), new ScoredArrayTrieNode[]{c2,c1});
        checkTopChilds(r);
        checkMaxSubScore(r);
        
        ScoredArrayTrieNode c3 = new ScoredArrayTrieNode('C', 7);
        r.insertNewChild(c3);
        assertEquals(c3, r.getChild('C'));
        assertEquals(10, r.maxScore);
        assertArrayEquals(r.topChilds.toArray(), new ScoredArrayTrieNode[]{c2,c3,c1});
        checkTopChilds(r);
        checkMaxSubScore(r);
    }
    
    
    @Test
    public void testUpdateChildNode(){
        System.out.println("UpdateChildNode");
        
        ScoredArrayTrieNode r = new ScoredArrayTrieNode('.', 0);
        
        ScoredArrayTrieNode c1 = new ScoredArrayTrieNode('A', 5);
        r.setChild(c1);
        ScoredArrayTrieNode c11 = new ScoredArrayTrieNode('B', 6);
        c1.setChild(c11);
        ScoredArrayTrieNode c111 = new ScoredArrayTrieNode('C', 7);
        c11.setChild(c111);
        ScoredArrayTrieNode c12 = new ScoredArrayTrieNode('C', 3);
        r.setChild(c12);
        ScoredArrayTrieNode c13 = new ScoredArrayTrieNode('D', 4);
        r.setChild(c13);
        
        r.recSetMaxScore();
        r.recSetTopChilds();
        System.out.println(r.toString());
        
        r.updateChildNode('A', 10);
        System.out.println(r.toString());
        assertEquals(10, c1.getScore());
        assertEquals(10, r.maxScore);
        checkMaxSubScore(r);
        checkTopChilds(r);

        r.updateChildNode('A', 1);
        System.out.println(r.toString());
        assertEquals(1, c1.getScore());
        assertEquals(7, r.maxScore);
        checkMaxSubScore(r);
        checkTopChilds(r);

        c1.updateChildNode('B', 1);
        System.out.println(r.toString());
        assertEquals(1, c11.getScore());
        assertEquals(7, r.maxScore);
        checkMaxSubScore(r);
        checkTopChilds(r);
    }
    
//    @Test
//    public void testSetTopChilds(){
//        ScoredArrayTrieNode r = new ScoredArrayTrieNode('.', 0);
//        // initializing test
//        r.recSetMaxScore();
//        r.recSetTopChilds();
//        assertEquals(0,r.topChilds.size());
//
//        // Test child handling
//        r.setChild(new ScoredArrayTrieNode('A', 1));
//        r.setChild(new ScoredArrayTrieNode('B', 2));
//        r.setChild(new ScoredArrayTrieNode('C', 3));
//        r.recSetTopChilds();
//        assertEquals(r.calcMaxScore(),r.recSetTopChilds()); // returns maxScore
//        testTopChildsValid(r.topChilds); 
//
//        assertEquals(Math.min(ScoredArrayTrieNode.TOP_K, 4),r.topChilds.size());
//        assertEquals('C',r.topChilds.get(0).node.getChar());
//        assertEquals('B',r.topChilds.get(1).node.getChar());
//        assertEquals('A',r.topChilds.get(2).node.getChar());
//        assertEquals('.',r.topChilds.get(3).node.getChar());
//        
//        assertEquals(3,r.topChilds.get(0).score);
//        assertEquals(2,r.topChilds.get(1).score);
//        assertEquals(1,r.topChilds.get(2).score);
//        assertEquals(0,r.topChilds.get(3).score);
//
//        // Add some subchilds
//        r.getChild('A').setChild(new ScoredArrayTrieNode('A', 10));
//        assertEquals(r.calcMaxScore(),r.recSetTopChilds()); // returns maxScore
//        testTopChildsValid(r.topChilds); 
//        assertEquals('A',r.topChilds.get(0).node.getChar());
//        assertEquals(10,r.topChilds.get(0).score);
//    }    
//    
//    @Test
//    public void testInsertTopChildEntry(){
//        ScoredArrayTrieNode n = new ScoredArrayTrieNode('.', 0);        
//        assertEquals(1,n.topChilds.size());
//        assertEquals(0,n.topChilds.get(0).score);
//        assertEquals(n,n.topChilds.get(0).node);
//        
//        ScoredArrayTrieNode m = new ScoredArrayTrieNode('.', 0);
//        n.insertTopChildEntry(new TopScoreEntry(m, 10));
//        assertEquals(2,n.topChilds.size());
//        assertEquals(10,n.topChilds.get(0).score);
//        assertEquals(m,n.topChilds.get(0).node);
//        assertEquals(0,n.topChilds.get(1).score);
//        assertEquals(n,n.topChilds.get(1).node);
//        
//        ScoredArrayTrieNode m2 = new ScoredArrayTrieNode('.', 0);
//        n.insertTopChildEntry(new TopScoreEntry(m2, 5));
//        assertEquals(3,n.topChilds.size());
//        assertEquals(10,n.topChilds.get(0).score);
//        assertEquals(m,n.topChilds.get(0).node);
//        assertEquals(5,n.topChilds.get(1).score);
//        assertEquals(m2,n.topChilds.get(1).node);
//        assertEquals(0,n.topChilds.get(2).score);
//        assertEquals(n,n.topChilds.get(2).node);
//
//        
//        for (int i = 0; i< ScoredArrayTrieNode.TOP_K + 5; i++){
//            ScoredArrayTrieNode m3 = new ScoredArrayTrieNode('.', 0);
//            n.insertTopChildEntry(new TopScoreEntry(m3, 1));
//        }
//        assertEquals(ScoredArrayTrieNode.TOP_K, n.topChilds.size());
//        assertEquals(10,n.topChilds.get(0).score);
//        assertEquals(m,n.topChilds.get(0).node);
//    }
//    
//    
//    @Test
//    public void testInsertChild(){
//        ScoredArrayTrieNode n = new ScoredArrayTrieNode('.', 0);
//        
//        ScoredArrayTrieNode m = new ScoredArrayTrieNode('A', 9);
//        n.insertChid(m);
//        assertEquals(2,n.topChilds.size());
//        assertEquals(9,n.topChilds.get(0).score);
//        assertEquals(m,n.topChilds.get(0).node);
//        assertEquals(0,n.topChilds.get(1).score);
//        assertEquals(n,n.topChilds.get(1).node); 
//
//        ScoredArrayTrieNode m3 = new ScoredArrayTrieNode('A', 10);
//        n.insertChid(m3);
//        assertEquals(2,n.topChilds.size());
//        assertEquals(10,n.topChilds.get(0).score);
//        assertEquals(m3,n.topChilds.get(0).node);
//        assertEquals(0,n.topChilds.get(1).score);
//        assertEquals(n,n.topChilds.get(1).node);
//        testTopChildsValid(n.topChilds);
//    
//        ScoredArrayTrieNode m2 = new ScoredArrayTrieNode('B', 5);
//        n.insertChid(m2);
//        assertEquals(3,n.topChilds.size());
//        assertEquals(10,n.topChilds.get(0).score);
//        assertEquals(m3,n.topChilds.get(0).node);
//        assertEquals(5,n.topChilds.get(1).score);
//        assertEquals(m2,n.topChilds.get(1).node);
//        assertEquals(0,n.topChilds.get(2).score);
//        assertEquals(n,n.topChilds.get(2).node);
//        testTopChildsValid(n.topChilds);
//        
//        for (int i = 0; i< ScoredArrayTrieNode.TOP_K + 5; i++){
//            ScoredArrayTrieNode m4 = new ScoredArrayTrieNode((char)((int)'D'+i), 5);
//            n.insertChid(m4);
//        }
//        assertEquals(ScoredArrayTrieNode.TOP_K, n.topChilds.size());
//        assertEquals(10,n.topChilds.get(0).score);
//        assertEquals(m3,n.topChilds.get(0).node);
//        
//        testTopChildsValid(n.topChilds);
//
//    }
//    
//    @Test
//    public void testRemoveTopEntries(){
//        ScoredArrayTrieNode n = new ScoredArrayTrieNode('.', 0);
//        ScoredArrayTrieNode m = null;
//        for (int i = 0; i< ScoredArrayTrieNode.TOP_K + 5; i++){
//            m = new ScoredArrayTrieNode((char)((int)'A'+i), 5+i);
//            n.insertChid(m);
//        }
//        assertEquals(ScoredArrayTrieNode.TOP_K, n.topChilds.size());
//        n.removeTopEntry(m);
//        assertEquals(ScoredArrayTrieNode.TOP_K - 1, n.topChilds.size());
//    }
//    
////    public void checkScores(List<TopScoreEntry> topChilds){
////        // scores correct?
////        for (TopScoreEntry e : topChilds) {
////            assertEquals(e.node.calcMaxScore(), e.score);
////        }
////    }
//    
}