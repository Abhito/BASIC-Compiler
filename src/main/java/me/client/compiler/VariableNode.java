package me.client.compiler;

/**
 * The Variable AST Node
 * @author Abhinav Singhal
 * @version 1.0
 */
public class VariableNode extends Node{

    /**
     * Name of variable
     */
    private final String name;

    /**
     * Constructs a Variable Node
     * @param name name of variable
     */
    public VariableNode(String name){
        this.name = name;
    }

    /**
     * public accessor for variable
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * toString override
     * @return returns formatted string
     */
    public String toString() {
        return "VariableNode(" + name + ")";
    }
}
