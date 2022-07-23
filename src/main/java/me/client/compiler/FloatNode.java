package me.client.compiler;

/**
 * The Float AST Node Class
 * @author Abhinav Singhal
 * @version 1.0
 */
public class FloatNode extends Node {
    /**
     * number stored in Node
     */
    private final float number;

    /**
     * Constructor for FLoatNode class
     * @param number A float number that is going to be stored
     */
    public FloatNode(float number){
        this.number = number;
    }

    /**
     * Public accessor for number
     * @return float value
     */
    public float getFloat(){
        return number;
    }

    /**
     * toString Override
     * @return Prints value that is stored in Node
     */
    public String toString() {
        return "FloatNode(" + number + ")";
    }
}
