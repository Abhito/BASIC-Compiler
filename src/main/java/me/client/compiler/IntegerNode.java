package me.client.compiler;

/**
 * The Integer AST Node Class
 * @author Abhinav Singhal
 * @version 1.0
 */
public class IntegerNode extends Node {
    /**
     * private number value
     */
    private final int integer;

    /**
     * Constructor for Integer Node
     * @param integer Integer input
     */
    public IntegerNode(int integer){
        this.integer = integer;
    }

    /**
     * Public accessor for integer
     * @return the number in the node
     */
    public int getInteger(){
        return integer;
    }

    /**
     * toString override
     * @return the number as a string.
     */
    public String toString() {
        return "IntegerNode(" + integer + ")";
    }
}
