package de.unikoblenz.stpr.ArrayTrie;

import de.renepickhardt.utils.IOHelper;
import de.unikoblenz.stpr.interfaces.trie.TrieNodeInterface;
import java.util.ArrayList;

public class ArrayTrieNode implements TrieNodeInterface {

    // value of node is immutable signed byte in [-128 .. 127]
    // Character conversion is done via (byte) / (char)
    private final byte value;
    
    // Array for child nodes
    private ArrayTrieNode[] children;
    
    // ASCII range covered by children
    public static int MIN_CHAR = 44;
    public static int MAX_CHAR = 123;
    private static final int CHILD_ARRAY_LEN = MAX_CHAR - MIN_CHAR;

    public ArrayTrieNode(char c) {
        this.value = (byte) c;

        // we do not want to initialize an array here to save memory
        this.children = null;
    }

    public char getChar() {
        return (char) this.value;
    }

    /**
     * True if node has no children.
     * @return isLeaf
     */
    public boolean isLeaf() {
        if (children == null) {
            return true;
        }
        for (ArrayTrieNode n : children) {
            // Child found?
            if (n != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns array of children. Initializes variable if necessary.
     * @return children
     */
    public ArrayTrieNode[] getChildren() {
        if (children == null) {
            IOHelper.log("Initializing child Array");
            children = new ArrayTrieNode[CHILD_ARRAY_LEN];
        }
        return children;
    }

    public static int childIndex(char c) {
        int b = (int)(byte)c; // byte is signed!
        if (b < 0) {
            b = 256 + b;
        }
        return b - MIN_CHAR;
    }

    /**
     * Inserts Node n as child. 
     * @param node
     * @return overwite
     */
    public void setChild(ArrayTrieNode n)  {
        try{
        getChildren()[childIndex(n.getChar())] = n;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Character "+n.getChar()+" out of bounds.");
        }
    }

    public ArrayTrieNode getChild(char c) {
        if (children == null) { return null; }
        return getChildren()[childIndex(c)];
    }

    /**
     * Returns child node corresponding to character if it exists if not, it
     * generates a new node for this character and inserts it as a child
     * @param character
     * @return child
     */
    public ArrayTrieNode addGetChild(char c) {
        ArrayTrieNode n = this.getChild(c);
        if (n == null) {
            n = new ArrayTrieNode(c);
            this.setChild(n);
        }
        return n;
    }

    @Override
    public String toString() {
        String out = "";
        for (String line : this.recString()) {
            out += line + "\n";
        }
        return out;
    }

    private ArrayList<String> recString() {
        ArrayList<String> lines = new ArrayList<String>();
        Boolean fisrtLine = true;
        for (ArrayTrieNode n : getChildren()) {
            if (n == null) {
                continue;
            }
            for (String subline : n.recString()) {
                if (fisrtLine) {
                    lines.add(this.getChar() + subline);
                } else {
                    lines.add(" " + subline);
                }
                fisrtLine = false;
            }
        }
        if (fisrtLine) {
            lines.add("" + this.getChar());
        }
        return lines;
    }
}
