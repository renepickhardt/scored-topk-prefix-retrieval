package de.unikoblenz.stpr.implementation.trie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class TLNode {

    private byte value; // signed byte in [-128 .. 127]
    private LinkedList<TLNode> children;
    
    // ASCII range covered by children
    public static final int MAX_CHAR = 256;
    public static final int MIN_CHAR = 0;

    public TLNode(char c) {
        setChar(c);
    }

    public void setChar(char c) {
        this.value = (byte) c;
    }

    public char getChar() {
        return (char) value;
    }

    public TLNode getChild(char c) {
        if (children == null) {
            return null;
        }
        for (TLNode n : children) {
            if (n.getChar() == c) {
                return n;
            }
        }
        return null;
    }

    public void setChild(char c, TLNode n) {
        if (children == null) {
            children = new LinkedList<TLNode>();
        }
        children.addFirst(n);
    }

    /**
     * Returns child node corresponding to character if it exists if not, it
     * generates a new node
     *
     * @param character
     * @return child
     */
    public TLNode addGetChild(char c) {
        TLNode n = getChild(c);
        if (n == null) {
            n = new TLNode(c);
            setChild(c, n);
        }
        return n;
    }

    public String toString() {
        String out = "";
        for (String line : recString()) {
            out += line + "\n";
        }
        return out;
    }

    private ArrayList<String> recString() {
        ArrayList<String> lines = new ArrayList<String>();
        Boolean fisrtLine = true;
        for (TLNode n : children) {
            if (n == null) {
                continue;
            }
            for (String subline : n.recString()) {
                if (fisrtLine) {
                    lines.add(getChar() + subline);
                } else {
                    lines.add(" " + subline);
                }
                fisrtLine = false;
            }
        }
        if (fisrtLine) {
            lines.add("" + getChar());
        }
        return lines;
    }
}
