package me.client.compiler;

/**
 * The String AST Node
 * @author Abhinav Singhal
 * @version 1.0
 */
public class StringNode extends Node{
    /**
     * Value contained by node
     */
    private final String value;

    /**
     * Construct StringNode
     * @param value The String saved
     */
    public StringNode(String value){
        this.value = value;
    }

    /**
     * Public Accessor
     * @return the value of the node
     */
    public String returnString(){
        return value;
    }

    /**
     * toString override
     * @return returns formatted string
     */
    public String toString() {
        return "StringNode(" + value + ")";
    }
}
