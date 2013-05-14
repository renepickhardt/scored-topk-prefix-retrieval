/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unikoblenz.stpr.ArrayTrie;

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
public class ArrayTrieNodeTest {
    
    public ArrayTrieNodeTest() {
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
        for(int i = ArrayTrieNode.MIN_CHAR; i < ArrayTrieNode.MAX_CHAR; i++){
            char c = (char) i;
            System.out.println("int "+ i + " char " + c + " byte " + (byte)c + " ci " + ArrayTrieNode.childIndex(c));
            assertTrue(ArrayTrieNode.childIndex(c) >= 0);
            indices.add(ArrayTrieNode.childIndex(c));
        }
        
        assertTrue(indices.first() == 0);               
        assertTrue(indices.last().equals(indices.size() - 1));
        assertTrue(indices.size() == ArrayTrieNode.MAX_CHAR - ArrayTrieNode.MIN_CHAR);
    }
    
    /**
     * Test of getChild method, of class ArrayTrieNode.
     */
    @Test
    public void testGetChild() {
        System.out.println("getChild");
        ArrayTrieNode instance = new ArrayTrieNode('.');

        for(int i = ArrayTrieNode.MAX_CHAR; i < ArrayTrieNode.MAX_CHAR; i++){
            char c = (char) i;
            assertNull(instance.getChild(c));
            ArrayTrieNode n = new ArrayTrieNode(c);
            instance.setChild(n);
            assertTrue(n == instance.getChild(n.getChar()));
        }
    }

    /**
     * Test of addGetChild method, of class ArrayTrieNode.
     */
    @Test
    public void testAddGetChild() {
        System.out.println("addGetChild");
        ArrayTrieNode instance = new ArrayTrieNode('.');
        char c = 'A';
        ArrayTrieNode n = instance.addGetChild(c);
        assertNotNull(n);
        assertEquals(n, instance.addGetChild(c));
    }
}