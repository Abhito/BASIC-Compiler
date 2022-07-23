package me.client.compiler;

/**
 * NextNode contains Next Statements
 * @author Abhinav Singhal
 * @version 1.3
 */
public class NextNode extends StatementNode {
    /**
     * Variable that is after the Next Statement
     */
    private final VariableNode variable;
    /**
     * forNode is a reference to related ForNode
     */
    private ForNode forNode;
    /**
     * Constructor for the class
     * @param variable the identifier
     */
    public NextNode(VariableNode variable){
        this.variable = variable;
    }

    /**
     * public accessor for variable
     * @return variable
     */
    public VariableNode getVariable(){
        return variable;
    }

    /**
     * Link ForNode with this node
     * @param node ForNode to link
     */
    public void setFor(ForNode node){
        this.forNode = node;
    }

    /**
     * public accessor for the ForNode
     * @return The linked ForNode
     */
    public Node getFor(){ return forNode;}

    /**
     * ToString Override
     * @return returns formatted string
     */
    public String toString() {
        return "NextNode: " + variable.toString();
    }
}
