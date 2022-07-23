package me.client.compiler;

/**
 * ForNode holds For Statements
 * @author Abhinav Singhal
 * @version 1.3
 */
public class ForNode extends StatementNode {
    /**
     * The variable and initial value
     */
    private final AssignmentNode variable;
    /**
     * limit represents max value, increment is what to increase value with
     */
    final private Node limit, increment;

    /**
     * Reference to the Statement after a NextNode
     */
    private Node nextNode;

    /**
     * Constructor without increment parameter
     * @param variable Initial variable
     * @param limit Max value
     */
    public ForNode(AssignmentNode variable, Node limit){
        this.variable = variable;
        this.limit = limit;
        this.increment = new IntegerNode(1);
    }

    /**
     * Constructor with all three parameters
     * @param variable initial variable
     * @param limit max value
     * @param increment STEP value
     */
    public ForNode(AssignmentNode variable, Node limit, Node increment){
        this.variable = variable;
        this.limit = limit;
        this.increment = increment;
    }

    /**
     * Sets the reference to next statement
     * @param node the statement after NextNode
     */
    public void nextStatement(Node node){
        this.nextNode = node;
    }

    /**
     * Public accessor for variable
     * @return variable
     */
    public AssignmentNode getVariable(){
        return variable;
    }

    /**
     * public accessor for limit
     * @return limit
     */
    public Node getLimit(){
        return limit;
    }

    /**
     * public accessor for increment
     * @return increment
     */
    public Node getIncrement(){
        return increment;
    }

    /**
     * public accessor for reference to the Node after NextNode
     * @return next of NEXTNODE
     */
    public Node getNextNode(){return nextNode;}

    /**
     * Override toString
     * @return a formatted string containing all values
     */
    public String toString() {
        return "ForNode(" + variable.toString() + ", " + limit.toString()
                + ", " + increment.toString() + ")";
    }
}
