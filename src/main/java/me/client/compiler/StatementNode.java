package me.client.compiler;

/**
 * Abstract StatementNote implements Node class
 * @author Abhinav Singhal
 * @version 1.2
 */
public abstract class StatementNode extends Node{
    /**
     * Reference to next Statement Node
     */
    private StatementNode next = null;

    /**
     * Set next Node
     * @param next the next statement
     */
    public void setNext(StatementNode next){
        this.next = next;
    }

    /**
     * public accessor for next
     * @return next
     */
    public StatementNode getNext(){
        return next;
    }

    /**
     * Print the next Node
     * @return String of next node
     */
    public String nextString(){
        return next.toString();
    }
}